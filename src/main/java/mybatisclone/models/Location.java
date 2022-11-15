package mybatisclone.models;


public class Location {
    private int locationId;
    private String streetAddress;
    private String postalCode;
    private String city;
    private String stateProvince;
    private Country country;

    public Location(int locationId, String streetAddress, String postalCode, String city, String stateProvince, Country country) {
        this.locationId = locationId;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.city = city;
        this.stateProvince = stateProvince;
        this.country = country;
    }


}
