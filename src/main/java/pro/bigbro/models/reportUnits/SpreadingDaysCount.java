package pro.bigbro.models.reportUnits;

public class SpreadingDaysCount {
    private int week;
    private int dayOfWeek;
    private int daysCount;

    public SpreadingDaysCount() {
    }

    public SpreadingDaysCount(int week, int dayOfWeek, int daysCount) {
        this.week = week;
        this.dayOfWeek = dayOfWeek;
        this.daysCount = daysCount;
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

    public int getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(int daysCount) {
        this.daysCount = daysCount;
    }
}
