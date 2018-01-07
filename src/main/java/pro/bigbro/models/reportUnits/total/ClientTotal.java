package pro.bigbro.models.reportUnits.total;

public class ClientTotal {
    private int cityId;
    private String cityName;
    private int clients;

    public ClientTotal() {
    }

    public ClientTotal(int cityId, String cityName, int clients) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.clients = clients;
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

    public int getClients() {
        return clients;
    }

    public void setClients(int clients) {
        this.clients = clients;
    }
}
