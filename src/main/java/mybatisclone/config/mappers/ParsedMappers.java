package mybatisclone.config.mappers;

import mybatisclone.utils.XMLParser;

import java.util.HashMap;
import java.util.Map;

public class ParsedMappers {
    private Map<String, ParsedMapper> mappers = new HashMap<>();

    public void parse(String path) throws Exception {
        ParsedMapper parse = XMLParser.parse(ParsedMapper.class, path);
        mappers.put(path, parse);
    }

    public ParsedMapper getMapper(String name) {
        return mappers.get(name);
    }

    public Select getSelect(String id) {
        int lastDotIndex = id.lastIndexOf('.');
        if (lastDotIndex != -1) {
            String namespace = id.substring(0, lastDotIndex);
            String methodId = id.substring(lastDotIndex + 1, id.length());
            ParsedMapper parsedMapper = mappers.get(namespace);
            if(parsedMapper == null) {
                return null;
                // TODO: 11/10/2022  throw exception
            }

            for (Select select : parsedMapper.select) {
                if(select.id.equals(id)) {
                    return select;
                }
            }

            return null;
        }

        Select select = null;
        ParsedMapper mapper = null;
        for (ParsedMapper m : mappers.values()) {
            for (Select s : m.select) {
                if (s.id.equals(id)) {
                    if(select != null) {
                        throw new IllegalStateException();
                        // TODO: 11/10/2022 throw exception
                    }

                    select = s;
                    mapper = m;
                }
            }
        }

        // TODO: 11/10/2022 throw
        return select;
    }

    public Insert getInsert(String id) {
        int lastDotIndex = id.lastIndexOf('.');
        if (lastDotIndex != -1) {
            String namespace = id.substring(0, lastDotIndex);
            String methodId = id.substring(lastDotIndex + 1, id.length());
            ParsedMapper parsedMapper = mappers.get(namespace);
            if(parsedMapper == null) {
                return null;
                // TODO: 11/10/2022  throw exception
            }

            for (Insert o : parsedMapper.insert) {
                if(o.id.equals(id)) {
                    return o;
                }
            }

            return null;
        }

        Insert insert = null;
        for (ParsedMapper m : mappers.values()) {
            for (Insert o : m.insert) {
                if (o.id.equals(id)) {
                    if(insert != null) {
                        // TODO: 11/10/2022 throw exception
                    }

                    insert = o;
                }
            }
        }

        // TODO: 11/10/2022 throw
        return insert;
    }

    public Update getUpdate(String id) {
        int lastDotIndex = id.lastIndexOf('.');
        if (lastDotIndex != -1) {
            String namespace = id.substring(0, lastDotIndex);
            String methodId = id.substring(lastDotIndex + 1, id.length());
            ParsedMapper parsedMapper = mappers.get(namespace);
            if(parsedMapper == null) {
                return null;
                // TODO: 11/10/2022  throw exception
            }

            for (Update o : parsedMapper.update) {
                if(o.id.equals(id)) {
                    return o;
                }
            }

            return null;
        }

        Update update = null;
        for (ParsedMapper m : mappers.values()) {
            for (Update o : m.update) {
                if (o.id.equals(id)) {
                    if(update != null) {
                        // TODO: 11/10/2022 throw exception
                    }

                    update = o;
                }
            }
        }

        // TODO: 11/10/2022 throw
        return update;
    }

    public Delete getDelete(String id) {
        int lastDotIndex = id.lastIndexOf('.');
        if (lastDotIndex != -1) {
            String namespace = id.substring(0, lastDotIndex);
            String methodId = id.substring(lastDotIndex + 1, id.length());
            ParsedMapper parsedMapper = mappers.get(namespace);
            if(parsedMapper == null) {
                return null;
                // TODO: 11/10/2022  throw exception
            }

            for (Delete o : parsedMapper.delete) {
                if(o.id.equals(id)) {
                    return o;
                }
            }

            return null;
        }

        Delete delete = null;
        for (ParsedMapper m : mappers.values()) {
            for (Delete o : m.delete) {
                if (o.id.equals(id)) {
                    if(delete != null) {
                        // TODO: 11/10/2022 throw exception
                    }

                    delete = o;
                }
            }
        }

        // TODO: 11/10/2022 throw
        return delete;
    }

}
