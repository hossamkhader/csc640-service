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
import org.json.JSONArray;


@Entity
@DiscriminatorValue("FAMILY_MEMBER")
public class FamilyMember extends Person {
    
    @ManyToMany(fetch=FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name="ELDERLY_FAMILY",joinColumns={@JoinColumn(name="family_username")},inverseJoinColumns={@JoinColumn(name="elderly_username")})
    private Set<Elderly> elderlys;

    public FamilyMember() {
        super();
        this.elderlys = new HashSet<>();
    }
    
    @Override
    public String getType()
    {
        return "FAMILY_MEMBER";
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
