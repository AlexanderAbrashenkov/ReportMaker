package pro.bigbro.models.financial;

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
public class FinancialTransaction {
    @Id
    private long id;

    private long cityId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Expense expense;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private double amount;

    @Column(length = 1000)
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonProperty("master")
    private Staff staff;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    private Client client;

    @JsonProperty("sold_item_id")
    private long soldItemId;

    @JsonProperty("sold_item_type")
    private String soldItemType;

    public FinancialTransaction() {
    }

    public FinancialTransaction(long id, long cityId, Expense expense,
                                Date date, double amount, String comment, Staff staff,
                                Client client, long soldItemId, String soldItemType) {
        this.id = id;
        this.cityId = cityId;
        this.expense = expense;
        this.date = date;
        this.amount = amount;
        this.comment = comment;
        this.staff = staff;
        this.client = client;
        this.soldItemId = soldItemId;
        this.soldItemType = soldItemType;
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

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public long getSoldItemId() {
        return soldItemId;
    }

    public void setSoldItemId(long soldItemId) {
        this.soldItemId = soldItemId;
    }

    public String getSoldItemType() {
        return soldItemType;
    }

    public void setSoldItemType(String soldItemType) {
        this.soldItemType = soldItemType;
    }

    @Override
    public String toString() {
        return "FinancialTransaction{" +
                "id=" + id +
                ", cityId=" + cityId +
                ", expense=" + expense +
                ", date=" + date +
                ", amount=" + amount +
                ", comment='" + comment + '\'' +
                ", staff=" + staff +
                ", client=" + client +
                ", soldItemId=" + soldItemId +
                ", soldItemType=" + soldItemType +
                '}';
    }
}
