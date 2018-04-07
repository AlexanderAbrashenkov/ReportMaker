package pro.bigbro.models.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Service {
    @Id
    @GeneratedValue
    @JsonIgnore
    private long id;
    @JsonProperty("id")
    private int serviceId;
    private long cityId;
    private String title;
    @JsonProperty("category_id")
    private long categoryId;
    @JsonProperty("price_min")
    private double priceMin;
    @JsonProperty("price_max")
    private double priceMax;
    private int active;

    public Service() {
    }

    public Service(long id, int serviceId, long cityId, String title, long categoryId, double priceMin, double priceMax, int active) {
        this.id = id;
        this.serviceId = serviceId;
        this.cityId = cityId;
        this.title = title;
        this.categoryId = categoryId;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
