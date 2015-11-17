package actors;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import org.hibernate.annotations.DiscriminatorOptions;
import org.json.JSONObject;

@Entity
@Table(name = "MONITORS_RESPONSE")
public class MonitorResponse implements Serializable {
    
    @Id  
    @GeneratedValue(strategy=GenerationType.AUTO)  
    private Long reponse_id;
    
    @Column (name="id")
    private Long id;
        
    @Column(name = "time")
    private String time;
    
    @Column(name = "bloodPressure")
    private String bloodPressure;
    @Column(name = "bloodSugar")
    private String bloodSugar;
    @Column(name = "heartBeat")
    private String heartBeat;

    @Column(name = "type")
    private String type;
    
    @Column(name = "done")
    private boolean done;
    
    
    public MonitorResponse()
    {
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    


    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(String bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public String getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(String heartBeat) {
        this.heartBeat = heartBeat;
    }
    
    public boolean isDone() {
        return done;
    }
    
    public void setDone(boolean done) {
        this.done = done;
    }

    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    
    public JSONObject getMonitorResponseInfo()
    {
        JSONObject tmp = new JSONObject();
        try
        {
            tmp.put("id", getId());
            tmp.put("time", time);
            tmp.put("type", type);
            tmp.put("done", done);
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
