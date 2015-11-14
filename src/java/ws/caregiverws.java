package ws;

import actors.Caregiver;
import actors.DoctorAppointment;
import actors.Elderly;
import actors.FamilyMember;
import actors.HealthMonitor;
import actors.MedicationMonitor;
import actors.Monitor;
import actors.Person;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.json.JSONObject;
import com.sun.org.apache.xml.internal.security.utils.Base64;


@WebService(serviceName = "caregiverws")
public class caregiverws {
    
    @Resource
    WebServiceContext wsctx;
    
    private static final String dataSourceName="csc640";
    private static Configuration configuration;
    
    public caregiverws()
    {
        configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Caregiver.class);
        configuration.addAnnotatedClass(Elderly.class);
        configuration.addAnnotatedClass(FamilyMember.class);
        configuration.addAnnotatedClass(Monitor.class);
        configuration.addAnnotatedClass(DoctorAppointment.class);
        configuration.addAnnotatedClass(HealthMonitor.class);
        configuration.addAnnotatedClass(MedicationMonitor.class);
    }

    private static Caregiver readData(String username)
    {
        SessionFactory sessionFactory;
        ServiceRegistry serviceRegistry;
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Caregiver caregiver = (Caregiver) session.get(Caregiver.class, username);
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
        return caregiver;
    }
    
    private String doAuthenticate()
    {
        Connection connection;
	PreparedStatement statement;
	ResultSet resultSet;
        String tmp = null;
        DataSource datasource;
        Context context;
        HttpServletRequest request = (HttpServletRequest) wsctx.getMessageContext().get(MessageContext.SERVLET_REQUEST);
        String credentials = null;
        try
        {
            credentials = new String(Base64.decode(request.getHeader("authorization").substring(6).trim()),Charset.forName("UTF-8"));
        }
        catch(Exception e)
        {
            
        }
        String username = credentials.split(":",2)[0];
        String password = credentials.split(":",2)[1];
        try
        {
            context = new InitialContext();
            datasource = (DataSource) context.lookup("java:comp/env/jdbc/" + dataSourceName);
            connection = datasource.getConnection();
            statement = connection.prepareStatement("SELECT username,type FROM USERS WHERE username= ? AND password= ?");
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            if (resultSet.next())
            {
                tmp = resultSet.getString("username");
            }
            resultSet.close();
            statement.close();
            connection.close();
        }
        catch (NamingException | SQLException e)
        {
            System.err.println(e.getMessage());
        }
        return tmp;
    }
    
    
    @WebMethod(operationName = "getType")
    public String getType()
    {
        if (doAuthenticate() == null)
            return null;
        Person person = readData(doAuthenticate());
        return person.getType();
    }
    
    
    @WebMethod(operationName = "getPersonInfo")
    public String getPersonInfo()
    {
        if (doAuthenticate() == null)
            return null;
        Caregiver caregiver = readData(doAuthenticate());
        return caregiver.getPersonInfo().toString();
    }


    @WebMethod(operationName = "getElderlysInfo")
    public String getElderlysInfo()
    {
        if (doAuthenticate() == null)
            return null;
        Caregiver caregiver = readData(doAuthenticate());
        return caregiver.getElderlysInfo().toString();
    }


    @WebMethod(operationName = "getElderlyInfo")
    public String getElderlyInfo(@WebParam(name = "username") String username)
    {
        if (doAuthenticate() == null)
            return null;
        Caregiver caregiver = readData(doAuthenticate());
        Elderly elderly;
        Iterator <Elderly> iterator = caregiver.getElderlys().iterator();
        while(iterator.hasNext())
        {
            elderly = iterator.next();
            if (elderly.getUsername().equals(username))
            {
                return elderly.getPersonInfo().toString();
            }
        }
        return null;
    }
    
    @WebMethod(operationName = "getElderlyDoctorAppointments")
    public String getElderlyDoctorAppointments(@WebParam(name = "username") String username)
    {
        if (doAuthenticate() == null)
            return null;
        Caregiver caregiver = readData(doAuthenticate());
        Elderly elderly;
        Iterator <Elderly> iterator = caregiver.getElderlys().iterator();
        while(iterator.hasNext())
        {
            elderly = iterator.next();
            if (elderly.getUsername().equals(username))
            {
                return elderly.getDoctorAppointmentsInfo().toString();
            }   
        }
        return null;
    }
    
    @WebMethod(operationName = "getElderlyHealthMonitors")
    public String getElderlyHealthMonitors(@WebParam(name = "username") String username)
    {
        if (doAuthenticate() == null)
            return null;
        Caregiver caregiver = readData(doAuthenticate());
        Elderly elderly;
        Iterator <Elderly> iterator = caregiver.getElderlys().iterator();
        while(iterator.hasNext())
        {
            elderly = iterator.next();
            if (elderly.getUsername().equals(username))
            {
                return elderly.getHealthMonitorsInfo().toString();
            }
        }
        return null;
    }
    
    @WebMethod(operationName = "getElderlyMedicationMonitors")
    public String getElderlyMedicationMonitors(@WebParam(name = "username") String username)
    {
        if (doAuthenticate() == null)
            return null;
        Caregiver caregiver = readData(doAuthenticate());
        Elderly elderly;
        Iterator <Elderly> iterator = caregiver.getElderlys().iterator();
        while(iterator.hasNext())
        {
            elderly = iterator.next();
            if (elderly.getUsername().equals(username))
            {
                return elderly.getMedicationMonitorsInfo().toString();
            }   
        }
        return null;
    }
    
    @WebMethod(operationName = "getElderlyCaregivers")
    public String getElderlyCaregivers(@WebParam(name = "username") String username)
    {
        if (doAuthenticate() == null)
            return null;
        Caregiver caregiver = readData(doAuthenticate());
        Elderly elderly;
        Iterator <Elderly> iterator = caregiver.getElderlys().iterator();
        while(iterator.hasNext())
        {
            elderly = iterator.next();
            if (elderly.getUsername().equals(username))
            {
                return elderly.getCaregiversInfo().toString();
            }
        }
        return null;
    }
    
    @WebMethod(operationName = "getElderlyFamilyMembers")
    public String getElderlyFamilyMembers(@WebParam(name = "username") String username)
    {
        if (doAuthenticate() == null)
            return null;
        Caregiver caregiver = readData(doAuthenticate());
        Elderly elderly;
        Iterator <Elderly> iterator = caregiver.getElderlys().iterator();
        while(iterator.hasNext())
        {
            elderly = iterator.next();
            if (elderly.getUsername().equals(username))
            {
                return elderly.getFamilyMembersInfo().toString();
            }
        }
        return null;
    }
    
    
    @WebMethod(operationName = "addOrUpdatePerson")
    public boolean addOrUpdatePerson(@WebParam(name = "person") String personInfo, @WebParam(name = "elderly_username") String elderly_username )
    {
        if (doAuthenticate() == null)
            return false;
        JSONObject person = new JSONObject(personInfo);
        SessionFactory sessionFactory;
        ServiceRegistry serviceRegistry;
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        if(person.getString("type").equals("CAREGIVER"))
        {
            Caregiver tmp = new Caregiver();
            tmp.setEmail_address(person.getString("email_address"));
            tmp.setFirst_name(person.getString("first_name"));
            tmp.setLast_name(person.getString("last_name"));
            tmp.setMobile_number(person.getString("mobile_number"));
            tmp.setPassword(person.getString("password"));
            tmp.setUsername(person.getString("username"));
            Caregiver caregiver = (Caregiver) session.get(Caregiver.class, doAuthenticate());
            Iterator <Elderly> e_iterator = caregiver.getElderlys().iterator();
            Iterator <Caregiver> c_iterator;
            boolean flag = false;
            Caregiver c = null;
            Elderly e = null;
            
            while(e_iterator.hasNext())
            {
                e = e_iterator.next();
                if(e.getUsername().equals(elderly_username))
                {
                    c_iterator = e.getCaregivers().iterator();
                    while(c_iterator.hasNext())
                    {
                        c = c_iterator.next();
                        if(c.getUsername().equals(person.getString("username")))
                        {
                            c.setEmail_address(person.getString("email_address"));
                            c.setFirst_name(person.getString("first_name"));
                            c.setLast_name(person.getString("last_name"));
                            c.setMobile_number(person.getString("mobile_number"));
                            c.setPassword(person.getString("password"));
                            flag = true;
                            break;
                        }
                    }
                    break;
                }  
            }
            if (!flag)
            {
                e.getCaregivers().add(tmp);
            }
            session.saveOrUpdate(caregiver);
        }
        
        
        if(person.getString("type").equals("ELDERLY"))
        {
            Elderly tmp = new Elderly();
            tmp.setEmail_address(person.getString("email_address"));
            tmp.setFirst_name(person.getString("first_name"));
            tmp.setLast_name(person.getString("last_name"));
            tmp.setMobile_number(person.getString("mobile_number"));
            tmp.setPassword(person.getString("password"));
            tmp.setUsername(person.getString("username"));
            Caregiver caregiver = (Caregiver) session.get(Caregiver.class, doAuthenticate());
            Iterator <Elderly> iterator = caregiver.getElderlys().iterator();
            boolean flag = false;
            Elderly elderly;
            while(iterator.hasNext())
            {
                elderly = iterator.next();
                if (elderly.getUsername().equals(person.getString("username")))
                {
                    elderly.setEmail_address(person.getString("email_address"));
                    elderly.setFirst_name(person.getString("first_name"));
                    elderly.setLast_name(person.getString("last_name"));
                    elderly.setMobile_number(person.getString("mobile_number"));
                    elderly.setPassword(person.getString("password"));
                    flag = true;
                    break;
                }
            }
            if(!flag)
            {
                caregiver.getElderlys().add(tmp);
            }
            session.saveOrUpdate(caregiver);
        }
        
        if(person.getString("type").equals("FAMILY_MEMBER"))
        {
            FamilyMember  tmp = new FamilyMember();
            tmp.setEmail_address(person.getString("email_address"));
            tmp.setFirst_name(person.getString("first_name"));
            tmp.setLast_name(person.getString("last_name"));
            tmp.setMobile_number(person.getString("mobile_number"));
            tmp.setPassword(person.getString("password"));
            tmp.setUsername(person.getString("username"));
            Caregiver caregiver = (Caregiver) session.get(Caregiver.class, doAuthenticate());
            Iterator <Elderly> e_iterator = caregiver.getElderlys().iterator();
            Iterator <FamilyMember> f_iterator;
            boolean flag = false;
            FamilyMember f = null;
            Elderly e = null;
            
            while(e_iterator.hasNext())
            {
                e = e_iterator.next();
                if(e.getUsername().equals(elderly_username))
                {
                    f_iterator = e.getFamilyMembers().iterator();
                    while(f_iterator.hasNext())
                    {
                        f = f_iterator.next();
                        if(f.getUsername().equals(person.getString("username")))
                        {
                            f.setEmail_address(person.getString("email_address"));
                            f.setFirst_name(person.getString("first_name"));
                            f.setLast_name(person.getString("last_name"));
                            f.setMobile_number(person.getString("mobile_number"));
                            f.setPassword(person.getString("password"));
                            flag = true;
                            break;
                        }
                    }
                    break;
                }  
            }
            if (!flag)
            {
                e.getFamilyMembers().add(tmp);
            }
            session.saveOrUpdate(caregiver);
        }
        
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
        return true;
    }
    
    
    
    @WebMethod(operationName = "deletePerson")
    public boolean deletePerson(@WebParam(name = "type") String type, @WebParam(name = "username") String username )
    {
        if (doAuthenticate() == null)
            return false;
        SessionFactory sessionFactory;
        ServiceRegistry serviceRegistry;
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        
        if(type.equals("CAREGIVER"))
        {
            Caregiver caregiver = (Caregiver) session.get(Caregiver.class, doAuthenticate());
            Iterator <Elderly> e_iterator = caregiver.getElderlys().iterator();
            Iterator <Caregiver> c_iterator;
            Caregiver c;
            Elderly e;
            while(e_iterator.hasNext())
            {
                e = e_iterator.next();
                c_iterator = e.getCaregivers().iterator();
                while(c_iterator.hasNext())
                {
                    c = c_iterator.next();
                    if(c.getUsername().equals(username))
                    {
                        e.getCaregivers().remove(c);
                        break;
                    }
                }
            }
        }
        if(type.equals("ELDERLY"))
        {
            Caregiver caregiver = (Caregiver) session.get(Caregiver.class, doAuthenticate());
            Iterator <Elderly> iterator = caregiver.getElderlys().iterator();       
            Elderly elderly;
            while(iterator.hasNext())
            {
                elderly = iterator.next();
                if (elderly.getUsername().equals(username))
                {
                    caregiver.getElderlys().remove(elderly);
                    break;
                }
            }
            session.saveOrUpdate(caregiver);
        }
        if(type.equals("FAMILY_MEMBER"))
        {
            Caregiver caregiver = (Caregiver) session.get(Caregiver.class, doAuthenticate());
            Iterator <Elderly> e_iterator = caregiver.getElderlys().iterator();
            Iterator <FamilyMember> f_iterator;
            FamilyMember f;
            Elderly e;
            while(e_iterator.hasNext())
            {
                e = e_iterator.next();
                f_iterator = e.getFamilyMembers().iterator();
                while(f_iterator.hasNext())
                {
                    f = f_iterator.next();
                    if(f.getUsername().equals(username))
                    {
                        e.getFamilyMembers().remove(f);
                        break;
                    }
                }
            }
        }
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
        return true;
    }
    

    @WebMethod(operationName = "addOrUpdateMonitor")
    public boolean addOrUpdateMonitor(@WebParam(name = "monitor") String monitor, @WebParam(name = "username") String username )
    {
        if (doAuthenticate() == null)
            return false;
        SessionFactory sessionFactory;
        ServiceRegistry serviceRegistry;
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        JSONObject tmp = new JSONObject(monitor);
        
        
        Caregiver caregiver = (Caregiver) session.get(Caregiver.class, doAuthenticate());
        Iterator <Elderly> iterator = caregiver.getElderlys().iterator();       
        Elderly elderly;
        HealthMonitor healthMonitor = null;
        MedicationMonitor medicationMonitor = null;
        DoctorAppointment doctorAppointment = null;
        
        while(iterator.hasNext())
        {
            elderly = iterator.next();
            if (elderly.getUsername().equals(username))
            {
                if(tmp.getString("id").isEmpty())
                {
                    if(tmp.getString("type").equals("HealthMonitor"))
                    {
                        healthMonitor = new HealthMonitor();
                        healthMonitor.setBloodPressure(tmp.getBoolean("bloodPressure"));
                        healthMonitor.setBloodSugar(tmp.getBoolean("bloodSugar"));
                        healthMonitor.setHeartBeat(tmp.getBoolean("heartBeat"));
                        healthMonitor.setNote(tmp.getString("note"));
                        healthMonitor.setSchedule(tmp.getString("schedule"));
                        elderly.getHealthMonitors().add(healthMonitor);
                        session.saveOrUpdate(healthMonitor);
                    }   
                    if(tmp.getString("type").equals("MedicationMonitor"))
                    {
                        medicationMonitor = new MedicationMonitor();
                        medicationMonitor.setMedicationName(tmp.getString("medicationName"));
                        medicationMonitor.setNote(tmp.getString("note"));
                        medicationMonitor.setSchedule(tmp.getString("schedule"));
                        elderly.getMedicationMonitors().add(medicationMonitor);
                        session.saveOrUpdate(medicationMonitor);
                    }
                    if(tmp.getString("type").equals("DoctorAppointment"))
                    {
                        doctorAppointment = new DoctorAppointment();
                        doctorAppointment.setDoctorName(tmp.getString("doctorName"));
                        doctorAppointment.setNote(tmp.getString("note"));
                        doctorAppointment.setSchedule(tmp.getString("schedule"));
                        elderly.getDoctorAppointments().add(doctorAppointment);
                        session.saveOrUpdate(doctorAppointment);
                    }   
                    
                    
                }
                
                else
                {
                    if(tmp.getString("type").equals("HealthMonitor"))
                    {
                        Iterator <HealthMonitor> i = elderly.getHealthMonitors().iterator();
                        while(i.hasNext())
                        {
                            healthMonitor = i.next();
                            if(healthMonitor.getId().toString()== tmp.get("id"))
                            {
                                healthMonitor.setBloodPressure(tmp.getBoolean("bloodPressure"));
                                healthMonitor.setBloodSugar(tmp.getBoolean("bloodSugar"));
                                healthMonitor.setHeartBeat(tmp.getBoolean("heartBeat"));
                                healthMonitor.setNote(tmp.getString("note"));
                                healthMonitor.setSchedule(tmp.getString("schedule"));
                                session.saveOrUpdate(healthMonitor);
                                break;
                            }
                        }
                    }
                    if(tmp.getString("type").equals("MedicationMonitor"))
                    {
                        Iterator <MedicationMonitor> i = elderly.getMedicationMonitors().iterator();
                        while(i.hasNext())
                        {
                            medicationMonitor = i.next();
                            if(medicationMonitor.getId().toString()== tmp.get("id"))
                            {
                                medicationMonitor.setMedicationName(tmp.getString("medicationName"));
                                medicationMonitor.setNote(tmp.getString("note"));
                                medicationMonitor.setSchedule(tmp.getString("schedule"));
                                elderly.getMedicationMonitors().add(medicationMonitor);
                                session.saveOrUpdate(medicationMonitor);
                                break;
                            }
                        }
                    }
                    if(tmp.getString("type").equals("DoctorAppointment"))
                    {
                        Iterator <DoctorAppointment> i = elderly.getDoctorAppointments().iterator();
                        while(i.hasNext())
                        {
                            doctorAppointment = i.next();
                            if(doctorAppointment.getId().toString()== tmp.get("id"))
                            {
                                doctorAppointment.setDoctorName(tmp.getString("doctorName"));
                                doctorAppointment.setNote(tmp.getString("note"));
                                doctorAppointment.setSchedule(tmp.getString("schedule"));
                                elderly.getDoctorAppointments().add(doctorAppointment);
                                session.saveOrUpdate(doctorAppointment);
                                break;
                            }
                        }
                    }
                }
                break;
            }
        }
        
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
        return true;
    }

    
    @WebMethod(operationName = "deleteMonitor")
    public boolean deleteMonitor(@WebParam(name = "id") String id,  @WebParam(name = "username") String username)
    {
        if (doAuthenticate() == null)
            return false;
        SessionFactory sessionFactory;
        ServiceRegistry serviceRegistry;
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Caregiver caregiver = (Caregiver) session.get(Caregiver.class, doAuthenticate());
        Iterator <Elderly> iterator = caregiver.getElderlys().iterator();       
        Elderly elderly= null;
        while(iterator.hasNext())
        {
            elderly = iterator.next();
            if (elderly.getUsername().equals(username))
            {
                Iterator <HealthMonitor> i = elderly.getHealthMonitors().iterator();
                HealthMonitor healthMonitor = null;
                while(i.hasNext())
                {
                    healthMonitor = i.next();
                    if(healthMonitor.getId().toString().equals(id))
                    {
                        session.delete(healthMonitor);
                        break;
                    }
                    
                }
                Iterator <MedicationMonitor> j = elderly.getMedicationMonitors().iterator();
                MedicationMonitor medicationMonitor = null;
                while(j.hasNext())
                {
                    medicationMonitor = j.next();
                    if(medicationMonitor.getId().toString().equals(id))
                    {
                        session.delete(medicationMonitor);
                        break;
                    }
                    
                }
                Iterator <DoctorAppointment> k = elderly.getDoctorAppointments().iterator();
                DoctorAppointment doctorAppointment = null;
                while(k.hasNext())
                {
                   doctorAppointment = k.next();
                    if(doctorAppointment.getId().toString().equals(id))
                    {
                        session.delete(doctorAppointment);
                        break;
                    }
                }
                break;
            }
        }

    session.getTransaction().commit();
    session.close();
    sessionFactory.close();
    StandardServiceRegistryBuilder.destroy(serviceRegistry);
    return true;
    }
    
    
    
    
}
