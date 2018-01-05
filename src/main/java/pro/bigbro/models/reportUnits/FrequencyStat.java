package pro.bigbro.models.reportUnits;

public class FrequencyStat {
    private String masterName;
    private int reportType;
    private int comebackTimeType;
    private int clientType;
    private int month;
    private int year;
    private double clientsOrTime;

    public FrequencyStat() {
    }

    public FrequencyStat(String masterName, int month, int year, double clientsOrTime) {
        this.masterName = masterName;
        this.month = month;
        this.year = year;
        this.clientsOrTime = clientsOrTime;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    public int getComebackTimeType() {
        return comebackTimeType;
    }

    public void setComebackTimeType(int comebackTimeType) {
        this.comebackTimeType = comebackTimeType;
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
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

    public double getClientsOrTime() {
        return clientsOrTime;
    }

    public void setClientsOrTime(double clientsOrTime) {
        this.clientsOrTime = clientsOrTime;
    }
}
