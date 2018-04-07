package pro.bigbro.models.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceCategory {
    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;
    @JsonProperty("id")
    private int categoryId;
    private int cityId;
    private String title;
    @Column(updatable = false)
    private boolean masterCategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isMasterCategory() {
        return masterCategory;
    }

    public void setMasterCategory(boolean masterCategory) {
        this.masterCategory = masterCategory;
    }
}
