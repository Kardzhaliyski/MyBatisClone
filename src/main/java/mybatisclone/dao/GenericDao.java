package mybatisclone.dao;

import mybatisclone.dao.utils.ParsedSQL;
import mybatisclone.dao.mappers.AnnotationMapperHandler;
import mybatisclone.session.connections.pool.DatabaseConnectionPool;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class GenericDao {
    private static GenericDao instance;

    public static GenericDao getInstance() {
        if (instance == null) {
            instance = new GenericDao();
        }

        return instance;
    }

    DatabaseConnectionPool dbPool;

    private GenericDao() {
        this.dbPool = DatabaseConnectionPool.getInstance();
    }

    public <T> List<T> selectList(String sql, Class<T> c, Object params) throws Exception {
        ParsedSQL parsedSQL = new ParsedSQL(sql);
        ResultSet rs;
        try (Connection conn = dbPool.getConnection()) {
            PreparedStatement stat = conn.prepareStatement(parsedSQL.sql);
            populateStatement(stat, params, parsedSQL);
            rs = stat.executeQuery();
        }

        ArrayList<T> results = new ArrayList<>();
        while (rs.next()) {
            results.add(populateObject(rs, c));
        }

        return results;
    }

    public <T> T selectObject(String sql, Class<T> c, Object params) throws Exception {
        List<T> ts = selectList(sql, c, params);
        if (ts.size() > 1) {
            throw new IllegalArgumentException("More results returned! expected 1, recieved: " + ts.size());
        }
        if (ts == null || ts.size() == 0) {
            return null;
        }

        return ts.get(0);
    }

    private <T> T populateObject(ResultSet rs, Class<T> c) throws Exception {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        if (IsDefaultObject(c)) {
            if (metaData.getColumnTypeName(1).equals("DATE")) {
                return (T) rs.getDate(1).toLocalDate();
            } else if (metaData.getColumnTypeName(1).equals("DECIMAL")) {
                return (T) (Double) rs.getDouble(1);
            } else {
                return (T) rs.getObject(1);
            }
        }

        T t = c.getConstructor().newInstance();
        Field[] fields = c.getFields();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = changeNamingConvention(metaData.getColumnName(i));
            Field field = getField(fields, columnName);
            if (field == null) {
                continue;
            }

            Object value = null;
            if (metaData.getColumnTypeName(i).equals("DATE")) {
                value = rs.getDate(i).toLocalDate();
            } else if (metaData.getColumnTypeName(i).equals("DECIMAL")) {
                value = rs.getDouble(i);
            } else {
                value = rs.getObject(i);
            }

            field.set(t, value);
        }

        return t;
    }

    private <T> boolean IsDefaultObject(Class<T> c) {
        if (c == null
                || c.isPrimitive()
                || Number.class.isAssignableFrom(c)
                || c.equals(Boolean.class)
                || c.equals(String.class)) {
            return true;
        }

        return false;
    }

    public Field getField(Field[] fields, String fieldName) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().equals(fieldName)) {
                return fields[i];
            }
        }

        return null;
    }

    private String changeNamingConvention(String columnName) {
        int index = -1;
        while ((index = columnName.indexOf("_")) > -1) {
            char c = columnName.charAt(index + 1);
            char u = Character.toUpperCase(c);
            columnName = columnName.replace("_" + c, String.valueOf(u));
        }

        return columnName;
    }

    public int insert(String sql, Object parameter) throws Exception {
        return update(sql, parameter);
    }

    public int update(String sql, Object params) throws Exception {
        ParsedSQL parsedSQL = new ParsedSQL(sql);

        try (Connection conn = dbPool.getConnection()) {
            PreparedStatement stat = conn.prepareStatement(parsedSQL.sql);
            populateStatement(stat, params, parsedSQL);
            return stat.executeUpdate();
        }
    }

    public int delete(String sql, Object params) throws Exception {
        return update(sql, params);
    }

    private static void populateStatement(PreparedStatement stat, Object params, ParsedSQL parsedSQL) throws Exception {
        if (parsedSQL.paramNames.size() == 0) {
            return;
        }

        ArrayList<String> paramNames = parsedSQL.paramNames;
        //todo should work with more then 1 param - arg0 arg1 arg2 ...
        if (paramNames.size() == 1 && paramNames.get(0).startsWith("arg")) {
            stat.setObject(1, params);
        } else {
            Class<?> clazz = params.getClass();
            for (int i = 0; i < paramNames.size(); i++) {
                Field field = clazz.getField(paramNames.get(i));
                field.setAccessible(true);
                Object value = field.get(params);

                stat.setObject(i + 1, value);
            }
        }
    }

    public <T> T getMapper(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class[]{clazz},
                new AnnotationMapperHandler(this));
    }
}
