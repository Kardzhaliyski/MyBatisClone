package mybatisclone.config;

public class MapperLocation {
    public String _class;
    public String resource;
    public String url;

    public String getId() {
        if(_class != null) {
            return _class;
        }

        if(resource != null) {
            return resource;
        }

        if(url != null) {
            return url;
        }

        return null;
    }
}
