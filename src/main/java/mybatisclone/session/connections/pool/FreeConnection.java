package mybatisclone.session.connections.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class FreeConnection {
    private static final int CONNECTION_REFRESH_TIME = 600000; //10min
    public static final Timer timer = new Timer();
    private final TimerTask refresh = new TimerTask() {
        @Override
        public void run() {
            try {
                connection.createStatement().executeQuery("SELECT 1");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public LocalDateTime created;
    private Connection connection;

    public FreeConnection(Connection connection) {
        this.connection = connection;
        timer.schedule(refresh, CONNECTION_REFRESH_TIME, CONNECTION_REFRESH_TIME);
        created = LocalDateTime.now();
    }

    public Connection getConnection() {
        refresh.cancel();

        Connection c = connection;
        connection = null;
        return c;
    }
}
