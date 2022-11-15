package mybatisclone.config;

public class DataSource {
    public String type;
    public Property[] property;

    public String getProperty(String name) {
        for (Property p : property) {
            if (p.name.equals(name)) {
                return p.value;
            }
        }

        return null;
    }
}
