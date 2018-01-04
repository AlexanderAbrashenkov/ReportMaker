package pro.bigbro.models.reportUnits;

import com.google.common.collect.ComparisonChain;

public class WorkingMonth implements Comparable<WorkingMonth> {
    private int monthNum;
    private int yearNum;

    public WorkingMonth() {
    }

    public WorkingMonth(int monthNum, int yearNum) {
        this.monthNum = monthNum;
        this.yearNum = yearNum;
    }

    public int getMonthNum() {
        return monthNum;
    }

    public void setMonthNum(int monthNum) {
        this.monthNum = monthNum;
    }

    public int getYearNum() {
        return yearNum;
    }

    public void setYearNum(int yearNum) {
        this.yearNum = yearNum;
    }

    @Override
    public int compareTo(WorkingMonth o) {
        return ComparisonChain.start()
                .compare(this.yearNum, o.yearNum)
                .compare(this.monthNum, o.monthNum)
                .result();
    }
}
