package actors;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import org.json.JSONArray;


@Entity
@DiscriminatorValue("CAREGIVER")
public class Caregiver extends Person{
    
    @ManyToMany(fetch=FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name="CAREGIVER_ELDERLY",joinColumns={@JoinColumn(name="caregiver_username")},inverseJoinColumns={@JoinColumn(name="elderly_username")})
    private Set<Elderly> elderlys;

    public Caregiver() {
        super();
        this.elderlys = new HashSet<>();
    }
    
    @Override
    public String getType()
    {
        return "CAREGIVER"; 
    }
    

    public Set<Elderly> getElderlys() {
        return elderlys;
    }

    public void setElderlys(Set<Elderly> elderlys) {
        this.elderlys = elderlys;
    }
   
    public JSONArray getElderlysInfo()
    {
        JSONArray tmp = new JSONArray();
        Iterator <Elderly> iterator = elderlys.iterator();
        while(iterator.hasNext())
        {
            tmp.put(iterator.next().getPersonInfo());   
        }
        return tmp;
    }
    
    
}
