package pro.bigbro.models.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Service {
    @Id
    private long id;
    private long cityId;
    private String title;
    @JsonProperty("category_id")
    private long categoryId;
    @JsonProperty("price_min")
    private double priceMin;
    @JsonProperty("price_max")
    private double priceMax;

    public Service() {
    }

    public Service(long id, long cityId, String title, long categoryId, double priceMin, double priceMax) {
        this.id = id;
        this.cityId = cityId;
        this.title = title;
        this.categoryId = categoryId;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public double getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(double priceMin) {
        this.priceMin = priceMin;
    }

    public double getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(double priceMax) {
        this.priceMax = priceMax;
    }
}
