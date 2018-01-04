package pro.bigbro.models.clients;

public class ClientContainer {
    private int count;
    private Client[] data;

    public ClientContainer() {
    }

    public ClientContainer(int count, Client[] data) {
        this.count = count;
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Client[] getData() {
        return data;
    }

    public void setData(Client[] data) {
        this.data = data;
    }
}
