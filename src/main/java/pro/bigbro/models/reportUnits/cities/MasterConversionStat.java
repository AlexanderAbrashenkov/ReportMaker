package pro.bigbro.models.reportUnits.cities;

public class MasterConversionStat {
    private String masterName;
    private int comebackTimeType;
    private int month;
    private int year;
    private int clients;

    public MasterConversionStat() {
    }

    public MasterConversionStat(String masterName, int month, int year, int clients) {
        this.masterName = masterName;
        this.month = month;
        this.year = year;
        this.clients = clients;
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

    public int getClients() {
        return clients;
    }

    public void setClients(int clients) {
        this.clients = clients;
    }

    public int getComebackTimeType() {
        return comebackTimeType;
    }

    public void setComebackTimeType(int comebackTimeType) {
        this.comebackTimeType = comebackTimeType;
    }
}
