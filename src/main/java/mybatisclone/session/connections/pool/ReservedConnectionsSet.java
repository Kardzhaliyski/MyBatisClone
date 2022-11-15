package mybatisclone.session.connections.pool;


import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ReservedConnectionsSet implements Closeable {
    private static final int RESERVATION_TIME_LIMIT = 10;
    private final TimerTask reservationCheck = new TimerTask() {
        @Override
        public void run() {
            if (buffer.size() == 0) {
                return;
            }

            for (ReservedConnection c : buffer) {
                if (c.created.plusMinutes(RESERVATION_TIME_LIMIT).isBefore(LocalDateTime.now())) {
                    throw new IllegalStateException("Reservation time limit exceeded!");
                }
            }
        }
    };
    private final Timer timer;
    private Set<ReservedConnection> buffer;
    private final DatabaseConnectionPool databaseConnectionPool;

    public ReservedConnectionsSet(DatabaseConnectionPool manager) {
        this.buffer = new HashSet<>();
        timer = new Timer();
        timer.schedule(reservationCheck, 60000, 60000);
        this.databaseConnectionPool = manager;
    }

    public Connection reserve(Connection c) {
        var r = new ReservedConnection(databaseConnectionPool, c);
        buffer.add(r);
        return r;
    }

    public Connection release(Connection connection) {
        if (!(connection instanceof ReservedConnection)) {
            throw new IllegalArgumentException("Connection is not from this pool!");
        }

        ReservedConnection o = (ReservedConnection) connection;
        if (o.isReleased()) {
            return null;
        }

        if (!buffer.remove(o)) {
            throw new IllegalStateException("Connection is not from this pool!");
        }

        Connection c = o.getConnection();
        o.release();
        return c;
    }


    @Override
    public void close() throws IOException {
        timer.cancel();
        for (ReservedConnection c : buffer) {
            try {
                c.getConnection().close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
