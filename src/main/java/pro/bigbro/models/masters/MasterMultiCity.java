package pro.bigbro.models.masters;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MasterMultiCity {
    @Id
    @GeneratedValue
    private int id;
    private String cityGroupName;
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityGroupName() {
        return cityGroupName;
    }

    public void setCityGroupName(String cityGroupName) {
        this.cityGroupName = cityGroupName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
