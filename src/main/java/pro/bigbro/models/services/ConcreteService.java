package pro.bigbro.models.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ConcreteService {
    @Id
    @GeneratedValue
    private long uid;
    @JsonProperty("id")
    private long serviceId;
    private String title;
    private double cost;
    @JsonProperty("cost_per_unit")
    private double costPerUnit;
    private double discount;
    @JsonProperty("first_cost")
    private double firstCost;
    private long amount;
    private int cutType;

    public ConcreteService() {
    }

    public ConcreteService(long uid, long serviceId, String title, double cost, double costPerUnit,
                           double discount, double firstCost, long amount) {
        this.uid = uid;
        this.serviceId = serviceId;
        this.title = title;
        this.cost = cost;
        this.costPerUnit = costPerUnit;
        this.discount = discount;
        this.firstCost = firstCost;
        this.amount = amount;
    }

    public ConcreteService(long serviceId, String title, double cost, double costPerUnit, double discount,
                           double firstCost, long amount, int cutType) {
        this.serviceId = serviceId;
        this.title = title;
        this.cost = cost;
        this.costPerUnit = costPerUnit;
        this.discount = discount;
        this.firstCost = firstCost;
        this.amount = amount;
        this.cutType = cutType;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(double costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getFirstCost() {
        return firstCost;
    }

    public void setFirstCost(double firstCost) {
        this.firstCost = firstCost;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getCutType() {
        return cutType;
    }

    public void setCutType(int cutType) {
        this.cutType = cutType;
    }
}
