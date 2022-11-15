package mybatisclone.session;

import mybatisclone.config.Configuration;
import mybatisclone.config.DataSource;
import mybatisclone.session.connections.pool.DatabaseConnectionPool;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlSessionFactory implements Closeable {
    private Configuration configuration;
    private DatabaseConnectionPool connectionPool;


    SqlSessionFactory(Configuration configuration, DatabaseConnectionPool connectionPool) {
        this.configuration = configuration;
        this.connectionPool = connectionPool;
    }

    public SqlSession openSession() {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e); //todo fix exception
        }

        return new SqlSession(connection, configuration);
    }

    private Connection getConnection() throws SQLException {
        if (connectionPool != null) {
            return connectionPool.getConnection();
        }

        DataSource dataSource = configuration.environments.getDefaultEnvironment().dataSource;
        String url = dataSource.getProperty("url");
        String username = dataSource.getProperty("username");
        String password = dataSource.getProperty("password");

        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public void close() throws IOException {
        connectionPool.close();
    }

//  SqlSession openSession(boolean autoCommit);
//
//  SqlSession openSession(Connection connection);
//
//  SqlSession openSession(TransactionIsolationLevel level);
//
//  SqlSession openSession(ExecutorType execType);
//
//  SqlSession openSession(ExecutorType execType, boolean autoCommit);
//
//  SqlSession openSession(ExecutorType execType, TransactionIsolationLevel level);
//
//  SqlSession openSession(ExecutorType execType, Connection connection);
//
//  Configuration getConfiguration();
}
