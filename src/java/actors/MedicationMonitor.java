package actors;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import org.json.JSONObject;

@Entity
@DiscriminatorValue("MedicationMonitor")
public class MedicationMonitor extends Monitor {
    
    @Column(name = "medicationName")
    private String medicationName;
    
    public MedicationMonitor()
    {
        super();
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }
    
    public JSONObject getMonitorInfo()
    {
        JSONObject tmp = new JSONObject();
        try
        {
            tmp.put("id", getId());
            tmp.put("note", getNote());
            tmp.put("schedule", getSchedule());
            tmp.put("medicationName", medicationName);
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        return tmp;
    }
}
