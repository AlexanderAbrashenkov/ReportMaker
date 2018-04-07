package pro.bigbro.models.services;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ServiceLib {
    @Id
    @GeneratedValue
    private long id;
    private long serviceId;
    private int cutType;
    private Integer serviceGroupId;

    public ServiceLib() {
    }

    public ServiceLib(long serviceId, int cutType) {
        this.serviceId = serviceId;
        this.cutType = cutType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public int getCutType() {
        return cutType;
    }

    public void setCutType(int cutType) {
        this.cutType = cutType;
    }

    public Integer getServiceGroupId() {
        return serviceGroupId;
    }

    public void setServiceGroupId(Integer serviceGroupId) {
        this.serviceGroupId = serviceGroupId;
    }
}
