package mybatisclone.session;

import mybatisclone.config.*;
import mybatisclone.session.connections.pool.DatabaseConnectionPool;
import mybatisclone.utils.XMLParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlSessionFactoryBuilder {
    private static final Pattern replacementPattern = Pattern.compile("\\$\\{(\\w+)}");

    public SqlSessionFactoryBuilder() {
    }

    public SqlSessionFactory build(Configuration config) {
        return null;
    }

    public SqlSessionFactory build(Reader reader) throws Exception {
        return build(reader, null, null);
    }

    public SqlSessionFactory build(Reader reader, String environment) throws Exception {
        return build(reader, environment, null);
    }

    public SqlSessionFactory build(Reader reader, Properties properties) throws Exception {
        return build(reader, null, properties);
    }

    public SqlSessionFactory build(Reader reader, String environment, Properties properties) throws Exception {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return reader.read();
            }
        };

        return build(inputStream, environment, properties);
    }

    public SqlSessionFactory build(InputStream inputStream) throws Exception {
        return build(inputStream, null, null);
    }

    public SqlSessionFactory build(InputStream inputStream, String environment) throws Exception {
        return build(inputStream, environment, null);
    }

    public SqlSessionFactory build(InputStream inputStream, Properties properties) throws Exception {
        return build(inputStream, null, properties);
    }

    public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) throws Exception {
        Configuration configuration = XMLParser.parseConfiguration(inputStream);
        DataSource dataSource = configuration.environments.getDefaultEnvironment().dataSource;
        if (properties != null) {
            for (Property property : dataSource.property) {
                Matcher matcher = replacementPattern.matcher(property.value);
                if (matcher.find()) {
                    String param = matcher.group(1);
                    property.value = (String) properties.get(param);
                }
            }
        }

        DatabaseConnectionPool connectionPool = DatabaseConnectionPool.getInstance();
        if (connectionPool == null) {
            String url = dataSource.getProperty("url");
            String username = dataSource.getProperty("username");
            String password = dataSource.getProperty("password");
            DatabaseConnectionPool.init(url, username, password);
            connectionPool = DatabaseConnectionPool.getInstance();
        }

        for (MapperLocation l : configuration.mappers.mapper) {
            if (l.resource != null) {
                configuration.mappers.parsedMappers.parse(l.resource);
            }
        }

        return new SqlSessionFactory(configuration, connectionPool);
    }
}
