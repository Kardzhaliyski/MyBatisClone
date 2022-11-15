package mybatisclone.models;

public class Country {
    private String countryId;
    private String countryName;
    private Region region;

    public Country() {
    }

    public Country(String countryId, String countryName, Region region) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.region = region;
    }
}
