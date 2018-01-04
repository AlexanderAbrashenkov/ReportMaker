package pro.bigbro.models.jdbc;

public class ServiceTypeJdbc {
    private long serviceId;
    private String title;

    public ServiceTypeJdbc(long serviceId, String title) {
        this.serviceId = serviceId;
        this.title = title;
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
}
