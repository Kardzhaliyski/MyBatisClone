package mybatisclone.session;

import mybatisclone.config.Configuration;
import mybatisclone.config.mappers.Delete;
import mybatisclone.config.mappers.Insert;
import mybatisclone.config.mappers.Select;
import mybatisclone.config.mappers.Update;
import mybatisclone.dao.GenericDao;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SqlSession implements Closeable {
    Connection connection;
    Configuration configuration;
    GenericDao dao;

    public SqlSession(Connection connection, Configuration configuration) {
        this.connection = connection;
        this.configuration = configuration;
        dao = GenericDao.getInstance();
    }

    <T> T selectOne(String statement) throws Exception {
        Select select = configuration.mappers.parsedMappers.getSelect(statement);
        Class<T> clazz;
        if (select.resultType != null) {
            clazz = (Class<T>) Class.forName(select.resultType);
        } else {
            clazz = null;
        }

        return dao.selectObject(select.sql, clazz, null);
    }

    public <T> T selectOne(String statement, Object parameter) throws Exception {
        Select select = configuration.mappers.parsedMappers.getSelect(statement);
        Class<T> clazz;
        if (select.resultType != null) {
            clazz = (Class<T>) Class.forName(select.resultType);
        } else if(select.resultMap != null) {
            String resultMapName = select.resultMap;
            configuration.mappers.parsedMappers.
            clazz = null;
        }

        return dao.selectObject(select.sql, clazz, parameter);
    }

    public <E> List<E> selectList(String statement, Object parameter) throws Exception {
        Select select = configuration.mappers.parsedMappers.getSelect(statement);
        Class<E> clazz;
        if (select.resultType != null) {
            clazz = (Class<E>) Class.forName(select.resultType);
        } else {
            clazz = null;
        }

        return dao.selectList(select.sql, clazz, parameter);
    }


    public int insert(String statement, Object parameter) {
        Insert insert = configuration.mappers.parsedMappers.getInsert(statement);
        try {
            return dao.insert(insert.sql, parameter);
        } catch (Exception e) {
            throw new RuntimeException(e); //todo
        }
    }

    public int update(String statement, Object parameter) {
        Update update = configuration.mappers.parsedMappers.getUpdate(statement);
        try {
            return dao.update(update.sql, parameter);//todo
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int delete(String statement, Object parameter) {
        Delete delete = configuration.mappers.parsedMappers.getDelete(statement);
        try {
            return dao.delete(delete.sql, parameter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            connection = null;
        }
    }

    public <T> T getMapper(Class<T> type) {
        return configuration.mappers.getMapper(type);
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);//todo fix exception
        }
    }
}
