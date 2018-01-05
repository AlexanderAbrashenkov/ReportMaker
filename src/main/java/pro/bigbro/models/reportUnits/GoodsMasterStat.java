package pro.bigbro.models.reportUnits;

public class GoodsMasterStat {
    private int masterId;
    private String masterName;
    private int month;
    private int year;
    private double sales;
    private double salesPerClient;

    public GoodsMasterStat() {
    }

    public GoodsMasterStat(int masterId, String masterName, int month, int year, double sales, double salesPerClient) {
        this.masterId = masterId;
        this.masterName = masterName;
        this.month = month;
        this.year = year;
        this.sales = sales;
        this.salesPerClient = salesPerClient;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getSales() {
        return sales;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }

    public double getSalesPerClient() {
        return salesPerClient;
    }

    public void setSalesPerClient(double salesPerClient) {
        this.salesPerClient = salesPerClient;
    }
}
