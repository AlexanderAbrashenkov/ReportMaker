package pro.bigbro.models.goods;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import pro.bigbro.models.clients.Client;
import pro.bigbro.models.masters.Staff;

import javax.persistence.*;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class GoodsTransaction {
    @Id
    private long id;

    private long cityId;

    @JsonProperty("type_id")
    private long typeId;

    private String type;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    private Good good;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    private Unit unit;

    private long amount;

    @JsonProperty("create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @JsonProperty("cost_per_unit")
    private double costPerUnit;

    private double cost;

    private double discount;

    @Column(length = 1000)
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonProperty("master")
    private Staff staff;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    private Client client;

    public GoodsTransaction() {
    }

    public GoodsTransaction(long id, long cityId, long typeId, String type,
                            Good good,  Unit unit, long amount, Date createDate,
                            double costPerUnit, double cost, double discount, String comment,
                            Staff staff, Client client) {
        this.id = id;
        this.cityId = cityId;
        this.typeId = typeId;
        this.type = type;
        this.good = good;
        this.unit = unit;
        this.amount = amount;
        this.createDate = createDate;
        this.costPerUnit = costPerUnit;
        this.cost = cost;
        this.discount = discount;
        this.comment = comment;
        this.staff = staff;
        this.client = client;
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

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public double getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(double costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "GoodsTransaction{" +
                "id=" + id +
                ", cityId=" + cityId +
                ", typeId=" + typeId +
                ", type='" + type + '\'' +
                ", good=" + good +
                ", unit=" + unit +
                ", amount=" + amount +
                ", createDate=" + createDate +
                ", costPerUnit=" + costPerUnit +
                ", cost=" + cost +
                ", discount=" + discount +
                ", comment='" + comment + '\'' +
                ", staff=" + staff +
                ", client=" + client +
                '}';
    }
}
