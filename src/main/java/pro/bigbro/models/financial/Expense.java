package pro.bigbro.models.financial;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Expense {
    @Id
    private long id;

    private String title;

    public Expense() {
    }

    public Expense(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
