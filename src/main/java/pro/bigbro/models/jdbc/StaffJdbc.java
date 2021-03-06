package pro.bigbro.models.jdbc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;

public class StaffJdbc {
    private long id;
    private long cityId;
    private String name;
    private String title;
    private String specialization;
    private int hidden;
    private int fired;
    private int status;
    private String useInRecords;

    public StaffJdbc() {
    }

    public StaffJdbc(long id, long cityId, String name, String title, String specialization,
                     int hidden, int fired, int status, String useInRecords) {
        this.id = id;
        this.cityId = cityId;
        this.name = name;
        this.title = title;
        this.specialization = specialization;
        this.hidden = hidden;
        this.fired = fired;
        this.status = status;
        this.useInRecords = useInRecords;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getHidden() {
        return hidden;
    }

    public void setHidden(int hidden) {
        this.hidden = hidden;
    }

    public int getFired() {
        return fired;
    }

    public void setFired(int fired) {
        this.fired = fired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUseInRecords() {
        return useInRecords;
    }

    public void setUseInRecords(String useInRecords) {
        this.useInRecords = useInRecords;
    }

    @Override
    public String toString() {
        return "StaffJdbc{" +
                "id=" + id +
                ", cityId=" + cityId +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", specialization='" + specialization + '\'' +
                ", hidden=" + hidden +
                ", fired=" + fired +
                ", status=" + status +
                ", useInRecords='" + useInRecords + '\'' +
                '}';
    }
}
