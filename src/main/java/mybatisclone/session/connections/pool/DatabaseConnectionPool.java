package mybatisclone.session.connections.pool;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class DatabaseConnectionPool implements Closeable {
    private static final int UNUSED_CONNECTION_TIME_CHECK = 180000;
    private static final int DEFAULT_MIN_SIZE = 2;
    private static final int DEFAULT_MAX_SIZE = 5;
    public static DatabaseConnectionPool instance;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    private final int minSize;
    private final int maxSize;
    private int connCount;
    private final FreeConnectionQueue freePool;
    private final ReservedConnectionsSet reservePool;
    private final Timer timer;
    private final TimerTask closeUnusedConnections = new TimerTask() {
        @Override
        public void run() {
            closeUnusedConnections();
        }
    };

    private DatabaseConnectionPool(String dbUrl, String dbUser, String dbPassword, int minSize, int maxSize) throws SQLException {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.freePool = new FreeConnectionQueue(maxSize);
        this.reservePool = new ReservedConnectionsSet(this);
        this.timer = new Timer();
        timer.schedule(closeUnusedConnections, UNUSED_CONNECTION_TIME_CHECK, UNUSED_CONNECTION_TIME_CHECK);

        for (int i = 0; i < minSize; i++) {
            freePool.add(makeNewConnection());
        }
    }

    public static void init(String dbUrl, String dbUser, String dbPassword) throws SQLException {
        init(dbUrl, dbUser, dbPassword, DEFAULT_MIN_SIZE, DEFAULT_MAX_SIZE);
    }

    public static void init(String dbUrl, String dbUser, String dbPassword, int minSize, int maxSize) throws SQLException {
        if (instance != null) {
            throw new IllegalStateException("DatabaseConnectionPool is already instantiated!");
        }

        instance = new DatabaseConnectionPool(dbUrl, dbUser, dbPassword, minSize, maxSize);
    }

    public static DatabaseConnectionPool getInstance() {
        return instance;
    }

    private Connection makeNewConnection() throws SQLException {
        Connection c = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        connCount++;
        return c;
    }

    public Connection getConnection() throws SQLException {
        Connection connection;
        if (freePool.isEmpty()) {
            if (connCount == maxSize) {
                throw new IllegalStateException("No free connections!");
            }

            connection = makeNewConnection();
        } else {
            connection = freePool.get();
        }

        return reservePool.reserve(connection);
    }

    public boolean release(Connection connection) {
        Connection c = reservePool.release(connection);
        if (c == null) {
            return false;
        }

        freePool.add(c);
        return true;
    }

    private void closeUnusedConnections() {
        if (connCount == minSize) {
            return;
        }

        if (freePool.size <= 1) {
            return;
        }

        try {
            int closedConnections = freePool.tryClosing(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
       timer.cancel();
       freePool.close();
       reservePool.close();
    }
}
