package actors;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.json.JSONArray;

@Entity
@DiscriminatorValue("ELDERLY")
public class Elderly extends Person {
    
    
    @ManyToMany(fetch=FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name="CAREGIVER_ELDERLY",joinColumns={@JoinColumn(name="elderly_username")},inverseJoinColumns={@JoinColumn(name="caregiver_username")})
    private Set<Caregiver> caregivers;
    
    @ManyToMany(fetch=FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name="ELDERLY_FAMILY",joinColumns={@JoinColumn(name="elderly_username")},inverseJoinColumns={@JoinColumn(name="family_username")})
    private Set<FamilyMember> familyMembers;
    
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="username")
    private Set<HealthMonitor> healthMonitors;

    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="username")
    private Set<MedicationMonitor> medicationMonitors;
    
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="username")
    private Set<DoctorAppointment> doctorAppointments;

    public Elderly() {
        super();
        this.caregivers = new HashSet<>();
        this.familyMembers = new HashSet<>();
        this.doctorAppointments = new HashSet<>();
        this.healthMonitors = new HashSet<>();
        this.medicationMonitors = new HashSet<>();
    }
    
    @Override
    public String getType()
    {
        return "ELDERLY";   
    }

    public Set<Caregiver> getCaregivers() {
        return caregivers;
    }

    public void setCaregivers(Set<Caregiver> caregivers) {
        this.caregivers = caregivers;
    }

    public Set<FamilyMember> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(Set<FamilyMember> familyMembers) {
        this.familyMembers = familyMembers;
    }
    
        public Set<HealthMonitor> getHealthMonitors() {
        return healthMonitors;
    }

    
    public void setHealthMonitors(Set<HealthMonitor> healthMonitors) {
        this.healthMonitors = healthMonitors;
    }

    
    public Set<MedicationMonitor> getMedicationMonitors() {
        return medicationMonitors;
    }

    
    public void setMedicationMonitors(Set<MedicationMonitor> medicationMonitors) {
        this.medicationMonitors = medicationMonitors;
    }

    
    public Set<DoctorAppointment> getDoctorAppointments() {
        return doctorAppointments;
    }

    
    public void setDoctorAppointments(Set<DoctorAppointment> doctorAppointments) {
        this.doctorAppointments = doctorAppointments;
    }
    
    
    public JSONArray getFamilyMembersInfo()
    {
        JSONArray tmp = new JSONArray();
        Iterator <FamilyMember> iterator = familyMembers.iterator();
        while(iterator.hasNext())
        {
            tmp.put(iterator.next().getPersonInfo());   
        }
        return tmp;
    }
    
    public JSONArray getCaregiversInfo()
    {
        JSONArray tmp = new JSONArray();
        Iterator <Caregiver> iterator = caregivers.iterator();
        while(iterator.hasNext())
        {
            tmp.put(iterator.next().getPersonInfo());
        }
        return tmp;
    }
    
    public JSONArray getDoctorAppointmentsInfo()
    {
        JSONArray tmp = new JSONArray();
        Iterator <DoctorAppointment> iterator = doctorAppointments.iterator();
        while(iterator.hasNext())
        {
            tmp.put(iterator.next().getMonitorInfo());
        }
        return tmp;
    }
    
    public JSONArray getHealthMonitorsInfo()
    {
        JSONArray tmp = new JSONArray();
        Iterator <HealthMonitor> iterator = healthMonitors.iterator();
        while(iterator.hasNext())
        {
            tmp.put(iterator.next().getMonitorInfo());
        }
        return tmp;
    }
    
    public JSONArray getMedicationMonitorsInfo()
    {
        JSONArray tmp = new JSONArray();
        Iterator <MedicationMonitor> iterator = medicationMonitors.iterator();
        while(iterator.hasNext())
        {
            tmp.put(iterator.next().getMonitorInfo());
        }
        return tmp;
    }
  
}
