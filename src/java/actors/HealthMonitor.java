package actors;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import org.json.JSONObject;

@Entity
@DiscriminatorValue("HealthMonitor")
public class HealthMonitor extends Monitor{
    
    @Column(name = "bloodPressure")
    private boolean bloodPressure;
    @Column(name = "bloodSugar")
    private boolean bloodSugar;
    @Column(name = "heartBeat")
    private boolean heartBeat;
    
    public HealthMonitor()
    {
        super();
    }

    public boolean isBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(boolean bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public boolean isBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(boolean bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public boolean isHeartBeat() {
        return heartBeat;
    }


    public void setHeartBeat(boolean heartBeat) {
        this.heartBeat = heartBeat;
    }

    public JSONObject getMonitorInfo()
    {
        JSONObject tmp = new JSONObject();
        try
        {
            tmp.put("id", getId());
            tmp.put("note", getNote());
            tmp.put("schedule", getSchedule());
            tmp.put("bloodPressure", bloodPressure);
            tmp.put("bloodSugar", bloodSugar);
            tmp.put("heartBeat", heartBeat);
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        return tmp;
    }
}
