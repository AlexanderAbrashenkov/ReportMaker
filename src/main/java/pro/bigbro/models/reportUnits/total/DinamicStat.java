package pro.bigbro.models.reportUnits.total;

import com.google.common.collect.ComparisonChain;

public class DinamicStat implements Comparable<DinamicStat> {
    private int mon;
    private int year;
    private int cityId;
    private String cityName;
    private double data;

    public DinamicStat() {
    }

    public DinamicStat(int mon, int year, int cityId, String cityName, double data) {
        this.mon = mon;
        this.year = year;
        this.cityId = cityId;
        this.cityName = cityName;
        this.data = data;
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

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    @Override
    public int compareTo(DinamicStat o) {
        return ComparisonChain.start()
                .compare(this.cityId, o.cityId)
                .compare(this.year, o.year)
                .compare(this.mon, o.mon)
                .result();
    }
}
