package pro.bigbro.models.clients;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Client {
    @Id
    private long id;
    private long cityId;
    private String name;
    private String phone;
    private String email;
    @Column(length = 3000)
    private String comment;
    private int hasLink;

    public Client() {
    }

    public Client(long id, long cityId, String name, String phone,
                  String email, String comment, int hasLink) {
        this.id = id;
        this.cityId = cityId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.comment = comment;
        this.hasLink = hasLink;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getHasLink() {
        return hasLink;
    }

    public void setHasLink(int hasLink) {
        this.hasLink = hasLink;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", cityId=" + cityId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", comment='" + comment + '\'' +
                ", hasLink=" + hasLink +
                '}';
    }
}
