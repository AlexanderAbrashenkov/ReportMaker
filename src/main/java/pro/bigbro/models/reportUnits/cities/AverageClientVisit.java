package pro.bigbro.models.reportUnits.cities;

public class AverageClientVisit {
    private int month;
    private int year;
    private double averageVisit;

    public AverageClientVisit() {
    }

    public AverageClientVisit(int month, int year, double averageVisit) {
        this.month = month;
        this.year = year;
        this.averageVisit = averageVisit;
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

    public double getAverageVisit() {
        return averageVisit;
    }

    public void setAverageVisit(double averageVisit) {
        this.averageVisit = averageVisit;
    }
}
