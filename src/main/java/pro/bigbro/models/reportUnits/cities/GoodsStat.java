package pro.bigbro.models.reportUnits.cities;

public class GoodsStat {
    private int mon;
    private int year;
    private int goodId;
    private String goodTitle;
    private double sales;

    public GoodsStat() {
    }

    public GoodsStat(int mon, int year, int goodId, String goodTitle, double sales) {
        this.mon = mon;
        this.year = year;
        this.goodId = goodId;
        this.goodTitle = goodTitle;
        this.sales = sales;
    }

    public int getMon() {
        return mon;
    }

    public void setMon(int mon) {
        this.mon = mon;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public String getGoodTitle() {
        return goodTitle;
    }

    public void setGoodTitle(String goodTitle) {
        this.goodTitle = goodTitle;
    }

    public double getSales() {
        return sales;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }
}
