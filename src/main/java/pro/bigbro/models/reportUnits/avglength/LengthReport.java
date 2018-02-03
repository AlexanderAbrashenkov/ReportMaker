package pro.bigbro.models.reportUnits.avglength;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class LengthReport {
    @Id
    @GeneratedValue
    private int id;
    private int month;
    private int year;
    private int cityId;
    private String cityName;
    private double avgSmena;
    private double avgServ;

    public LengthReport() {
    }

    public LengthReport(int month, int year, int cityId, String cityName) {
        this.month = month;
        this.year = year;
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getAvgSmena() {
        return avgSmena;
    }

    public void setAvgSmena(double avgSmena) {
        this.avgSmena = avgSmena;
    }

    public double getAvgServ() {
        return avgServ;
    }

    public void setAvgServ(double avgServ) {
        this.avgServ = avgServ;
    }
}
