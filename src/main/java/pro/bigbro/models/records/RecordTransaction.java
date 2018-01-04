package pro.bigbro.models.records;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import pro.bigbro.models.clients.Client;
import pro.bigbro.models.masters.Staff;
import pro.bigbro.models.services.ConcreteService;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordTransaction {
    @Id
    private long id;

    @JsonProperty("company_id")
    private long cityId;

    @JsonProperty("services")
    @OneToMany(cascade = CascadeType.ALL)
    private List<ConcreteService> serviceList;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    private Staff staff;

    //todo add visit counter
    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    private Client client;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date date;

    @Temporal(TemporalType.TIMESTAMP)
    private Date datetime;

    @JsonProperty("create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(length = 1000)
    private String comment;

    private int attendance;

    private int length;

    @JsonProperty("created_user_id")
    private long createdUserId;

    private int deleted;

    private int visitNumber;

    private int daysBetweenVisits;

    private int clientHasNextVisit;

    public RecordTransaction() {
    }


    public RecordTransaction(long id, long cityId, List<ConcreteService> serviceList, Staff staff, Client client, Date date,
                             Date datetime, Date createDate, String comment, int attendance,int length, long createdUserId, int deleted) {
        this.id = id;
        this.cityId = cityId;
        this.serviceList = serviceList;
        this.staff = staff;
        this.client = client;
        this.date = date;
        this.datetime = datetime;
        this.createDate = createDate;
        this.comment = comment;
        this.attendance = attendance;
        this.length = length;
        this.createdUserId = createdUserId;
        this.deleted = deleted;
    }

    public RecordTransaction(long id, long cityId, List<ConcreteService> serviceList, Staff staff, Client client,
                             Date date, Date datetime, Date createDate, String comment,
                             int attendance, int length, long createdUserId,
                             int deleted, int visitNumber, int daysBetweenVisits, int clientHasNextVisit) {
        this.id = id;
        this.cityId = cityId;
        this.serviceList = serviceList;
        this.staff = staff;
        this.client = client;
        this.date = date;
        this.datetime = datetime;
        this.createDate = createDate;
        this.comment = comment;
        this.attendance = attendance;
        this.length = length;
        this.createdUserId = createdUserId;
        this.deleted = deleted;
        this.visitNumber = visitNumber;
        this.daysBetweenVisits = daysBetweenVisits;
        this.clientHasNextVisit = clientHasNextVisit;
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

    public List<ConcreteService> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<ConcreteService> serviceList) {
        this.serviceList = serviceList;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(long createdUserId) {
        this.createdUserId = createdUserId;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(int visitNumber) {
        this.visitNumber = visitNumber;
    }

    public int getDaysBetweenVisits() {
        return daysBetweenVisits;
    }

    public void setDaysBetweenVisits(int daysBetweenVisits) {
        this.daysBetweenVisits = daysBetweenVisits;
    }

    public int getClientHasNextVisit() {
        return clientHasNextVisit;
    }

    public void setClientHasNextVisit(int clientHasNextVisit) {
        this.clientHasNextVisit = clientHasNextVisit;
    }
}
