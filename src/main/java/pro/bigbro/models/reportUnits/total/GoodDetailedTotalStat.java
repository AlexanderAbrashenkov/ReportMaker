package pro.bigbro.models.reportUnits.total;

public class GoodDetailedTotalStat {
    private String title;
    private double sales;
    private double amount;
    private double part;

    public GoodDetailedTotalStat() {
    }

    public GoodDetailedTotalStat(String title, double sales, double amount, double part) {
        this.title = title;
        this.sales = sales;
        this.amount = amount;
        this.part = part;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public double getPart() {
        return part;
    }

    public void setPart(double part) {
        this.part = part;
    }
}
