package actors;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import org.json.JSONObject;

@Entity
@DiscriminatorValue("DoctorAppointment")
public class DoctorAppointment extends Monitor {
    
    @Column(name = "doctorName")
    private String doctorName;
    
    
    public DoctorAppointment()
    {
        super();
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    
    public JSONObject getMonitorInfo()
    {
        JSONObject tmp = new JSONObject();
        try
        {
            tmp.put("id", getId());
            tmp.put("note", getNote());
            tmp.put("schedule", getSchedule());
            tmp.put("doctorName", doctorName);
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        return tmp;
    }  
}
