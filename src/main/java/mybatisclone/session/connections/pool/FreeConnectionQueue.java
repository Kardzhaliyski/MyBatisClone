package mybatisclone.session.connections.pool;


import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FreeConnectionQueue implements Closeable {
    private static final int UNUSED_CONNECTION_CLOSING_TIME_LIMIT_IN_MIN = 15;
    FreeConnection[] buffer;
    int head = 0;
    int tail = 0;

    int size;

    public FreeConnectionQueue(int maxSize) {
        buffer = new FreeConnection[maxSize];
    }

    public void add(Connection connection) {
        FreeConnection c = new FreeConnection(connection);
        buffer[tail++] = c;
        if (tail == buffer.length) {
            tail = 0;
        }

        size++;
    }

    public Connection get() {
        if (size == 0) {
            throw new IllegalStateException();
        }

        FreeConnection c = buffer[head++];
        if (head == buffer.length) {
            head = 0;
        }

        size--;
        return c.getConnection();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int tryClosing(int count) throws SQLException {
        int closedConnection = 0;
        for (int i = 0; i < count; i++) {
            var unusedTimeLimit = buffer[head].created
                    .plusMinutes(UNUSED_CONNECTION_CLOSING_TIME_LIMIT_IN_MIN);
            if (unusedTimeLimit.isBefore(LocalDateTime.now())) {
                get().close();
                closedConnection++;
                continue;
            }

            return closedConnection;
        }

        return closedConnection;
    }

    @Override
    public void close() throws IOException {
        while (size > 0) {
            try {
                get().close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        FreeConnection.timer.cancel();
    }
}
