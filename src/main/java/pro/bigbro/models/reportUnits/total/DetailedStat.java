package pro.bigbro.models.reportUnits.total;

public class DetailedStat {
    private int cityId;
    private String cityName;
    private String title;
    private double price;
    private double sales;
    private double amount;
    private double fact;
    private double part;

    public DetailedStat() {
    }

    public DetailedStat(int cityId, String cityName, String title, double price, double sales, double amount, double fact, double part) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.title = title;
        this.price = price;
        this.sales = sales;
        this.amount = amount;
        this.fact = fact;
        this.part = part;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSales() {
        return sales;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getFact() {
        return fact;
    }

    public void setFact(double fact) {
        this.fact = fact;
    }

    public double getPart() {
        return part;
    }

    public void setPart(double part) {
        this.part = part;
    }
}
