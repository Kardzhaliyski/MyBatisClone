package mybatisclone.config;

import mybatisclone.config.mappers.ParsedMappers;
import mybatisclone.dao.GenericDao;

import java.util.HashMap;
import java.util.Map;

public class Mappers {
    public MapperLocation[] mapper;
    public Map<String, Object> annotationMappers = new HashMap<>();
    public ParsedMappers parsedMappers = new ParsedMappers();

    public <T> T getMapper(Class<T> clazz) {
        if(annotationMappers.containsKey(clazz.getName())) {
            return (T) annotationMappers.get(clazz.getName());
        }

        MapperLocation mapp = null;
        for (MapperLocation m : mapper) {
            if (m.getId().equals(clazz.getName())) {
                mapp = m;
                break;
            }
        }

        if(mapp == null) {
            throw new IllegalStateException("Type " + clazz + " is not known.");
        }

        if(mapp._class != null) {
            T parsed = GenericDao.getInstance().getMapper(clazz);
            annotationMappers.put(clazz.getName(), parsed);
            return parsed;
        } else if(mapp.resource != null) {

        }

        return null;
    }


    public boolean contains(String id) {
        for (MapperLocation m : mapper) {
            if (m.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

}
