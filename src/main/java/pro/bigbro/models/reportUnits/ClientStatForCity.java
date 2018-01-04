package pro.bigbro.models.reportUnits;

public class ClientStatForCity {
    private int month;
    private int year;
    private int clientCount;

    public ClientStatForCity() {
    }

    public ClientStatForCity(int month, int year, int clientCount) {
        this.month = month;
        this.year = year;
        this.clientCount = clientCount;
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

    public int getClientCount() {
        return clientCount;
    }

    public void setClientCount(int clientCount) {
        this.clientCount = clientCount;
    }
}
