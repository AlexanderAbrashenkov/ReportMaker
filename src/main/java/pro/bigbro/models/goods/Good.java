package pro.bigbro.models.goods;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Good {
    @Id
    private long id;
    private String title;

    public Good() {
    }

    public Good(long id, String title) {
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
        return "Good{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
