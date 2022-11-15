package mybatisclone.config.mappers;

public class ParsedMapper {
    public String namespace;
    public Select[] select;
    public Insert[] insert;
    public Update[] update;
    public Delete[] delete;
    public ResultMap[] resultMap;
}
