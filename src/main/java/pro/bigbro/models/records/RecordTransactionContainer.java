package pro.bigbro.models.records;

import javax.persistence.Entity;

public class RecordTransactionContainer {
    private int count;
    private RecordTransaction[] data;

    public RecordTransactionContainer() {
    }

    public RecordTransactionContainer(int count, RecordTransaction[] data) {
        this.count = count;
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public RecordTransaction[] getData() {
        return data;
    }

    public void setData(RecordTransaction[] data) {
        this.data = data;
    }
}
