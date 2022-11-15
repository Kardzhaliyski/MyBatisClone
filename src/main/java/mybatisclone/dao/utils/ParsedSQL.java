package mybatisclone.dao.utils;

import java.util.ArrayList;

public class ParsedSQL {
    public String sql;
    public ArrayList<String> paramNames;

    public ParsedSQL(String sql) {
        paramNames = new ArrayList<>();
        parse(sql);
    }

    private void parse(String sql) {
        StringBuilder sb = new StringBuilder();
        boolean parsingParam = false;
        int paramStart = -1;
        for (int i = 0; i < sql.length(); i++) {
            char ch = sql.charAt(i);
            if (paramStart != -1) {
                if (ch != '}') {
                    continue;
                }

                String paramName = sql.substring(paramStart + 2, i);
                paramNames.add(paramName);
                paramStart = -1;
                continue;
            }

            if (ch != '#') {
                sb.append(ch);
                continue;
            }

            paramStart = i;
            sb.append("?");
        }

        this.sql = sb.toString();
    }
}
