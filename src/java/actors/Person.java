package actors;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import org.json.JSONObject;



@Entity
@Table(name = "USERS")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.STRING)
public class Person implements Serializable {
    
    @Id
    @Column (name = "username", unique=true)
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "first_name")
    private String first_name;
    @Column(name = "last_name")
    private String last_name;
    @Column(name = "mobile_number")
    private String mobile_number;
    @Column(name = "email_address")
    private String email_address;

    public String getType()
    {
        return "PERSON";
    }
    
    public Person() {
        
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }
    
    public JSONObject getPersonInfo()
    {
        JSONObject tmp = new JSONObject();
        try
        {
            tmp.put("username", username);
            tmp.put("first_name", first_name);
            tmp.put("last_name", last_name);
            tmp.put("mobile_number", mobile_number);
            tmp.put("email_address", email_address);
            if(this instanceof Caregiver)
                tmp.put("type", "CAREGIVER");
            if(this instanceof Elderly)
                tmp.put("type", "ELDERLY");
            if(this instanceof FamilyMember)
                tmp.put("type", "FAMILY_MEMBER");
            
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        return tmp;
    }
}
