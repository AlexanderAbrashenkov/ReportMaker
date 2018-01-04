package pro.bigbro.models.goods;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Unit {
    @Id
    private long id;
    private String title;
    @JsonProperty("short_title")
    private String shortTitle;

    public Unit() {
    }

    public Unit(long id, String title, String shortTitle) {
        this.id = id;
        this.title = title;
        this.shortTitle = shortTitle;
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

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", shortTitle='" + shortTitle + '\'' +
                '}';
    }
}
