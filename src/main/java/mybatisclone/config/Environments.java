package mybatisclone.config;

public class Environments {
    public String _default;
    public Enviorment[] environment;

    public Enviorment getDefaultEnvironment() {
        return getEnvironment(_default);
    }

    public Enviorment getEnvironment(String id) {
        if (id == null) {
            return null;
        }

        for (Enviorment e : environment) {
            if (e.id.equals(id)) {
                return e;
            }
        }

        return null;
    }
}
