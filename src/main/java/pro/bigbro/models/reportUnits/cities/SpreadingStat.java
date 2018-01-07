package pro.bigbro.models.reportUnits.cities;

public class SpreadingStat {
    private int mon;
    private int year;
    private int week;
    private int dayOfWeek;
    private int clients;

    public SpreadingStat() {
    }

    public SpreadingStat(int mon, int year, int week, int dayOfWeek, int clients) {
        this.mon = mon;
        this.year = year;
        this.week = week;
        this.dayOfWeek = dayOfWeek;
        this.clients = clients;
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

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getClients() {
        return clients;
    }

    public void setClients(int clients) {
        this.clients = clients;
    }
}
