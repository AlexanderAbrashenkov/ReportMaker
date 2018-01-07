package pro.bigbro.models.reportUnits.total;

public class DataTotal {
    private int cityId;
    private String cityName;
    private double value;

    public DataTotal() {
    }

    public DataTotal(int cityId, String cityName, double value) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.value = value;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
