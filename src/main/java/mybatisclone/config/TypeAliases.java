package mybatisclone.config;

public class TypeAliases {
    public TypeAlias[] typeAlias;

    public String getType(String alias) {
        for (TypeAlias a : typeAlias) {
            if (a.alias.equals(alias)) {
                return a.type;
            }
        }

        return alias;
    }
}
