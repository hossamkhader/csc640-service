package ws;

import actors.Caregiver;
import actors.DoctorAppointment;
import actors.Elderly;
import actors.FamilyMember;
import actors.HealthMonitor;
import actors.MedicationMonitor;
import actors.Monitor;
import actors.MonitorResponse;
import actors.Person;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import java.util.Iterator;


@WebService(serviceName = "elderlyws")
public class elderlyws {
    
    @Resource
    WebServiceContext wsctx;
    
    private static final String dataSourceName="csc640";
    private static Configuration configuration;
    
    public elderlyws()
    {
        configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Caregiver.class);
        configuration.addAnnotatedClass(Elderly.class);
        configuration.addAnnotatedClass(FamilyMember.class);
        configuration.addAnnotatedClass(Monitor.class);
        configuration.addAnnotatedClass(DoctorAppointment.class);
        configuration.addAnnotatedClass(HealthMonitor.class);
        configuration.addAnnotatedClass(MedicationMonitor.class);
        configuration.addAnnotatedClass(MonitorResponse.class);
    }

    private static Elderly readData(String username)
    {
        SessionFactory sessionFactory;
        ServiceRegistry serviceRegistry;
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Elderly elderly = (Elderly) session.get(Elderly.class, username);
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
        return elderly;
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
        Elderly elderly = readData(doAuthenticate());
        return elderly.getPersonInfo().toString();
    }
   
    @WebMethod(operationName = "getElderlyDoctorAppointments")
    public String getElderlyDoctorAppointments()
    {
        if (doAuthenticate() == null)
            return null;
        Elderly elderly = readData(doAuthenticate());
        return elderly.getDoctorAppointmentsInfo().toString();
    }
    
    @WebMethod(operationName = "getElderlyHealthMonitors")
    public String getElderlyHealthMonitors()
    {
        if (doAuthenticate() == null)
            return null;
        Elderly elderly = readData(doAuthenticate());
        return elderly.getHealthMonitorsInfo().toString();
        
    }
    
    @WebMethod(operationName = "getElderlyMedicationMonitors")
    public String getElderlyMedicationMonitors()
    {
        if (doAuthenticate() == null)
            return null;
        Elderly elderly = readData(doAuthenticate());
        return elderly.getMedicationMonitorsInfo().toString();
    }
    
    @WebMethod(operationName = "setMonitorResponse")
    public String setMonitorResponse(@WebParam(name = "monitorResponse") String monitorResponse)
    {
        if (doAuthenticate() == null)
            return null;
        SessionFactory sessionFactory;
        ServiceRegistry serviceRegistry;
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        JSONObject tmp = new JSONObject(monitorResponse);
        Elderly elderly = (Elderly) session.get(Elderly.class, doAuthenticate());
        HealthMonitor healthMonitor;
        DoctorAppointment doctorAppointment;
        MedicationMonitor medicationMonitor;
        MonitorResponse tmp_monitorResponse = new MonitorResponse(); 
        if (tmp.getString("type").equals("HealthMonitor"))
        {
            Iterator <HealthMonitor> i = elderly.getHealthMonitors().iterator();
            while(i.hasNext())
            {
                healthMonitor = i.next();
                if(healthMonitor.getId().toString().equals(tmp.get("monitor_id")))
                {
                    
                    tmp_monitorResponse.setType(tmp.getString("type"));
                    tmp_monitorResponse.setBloodPressure(tmp.getString("bloodPressure"));
                    tmp_monitorResponse.setBloodSugar(tmp.getString("bloodSugar"));
                    tmp_monitorResponse.setHeartBeat(tmp.getString("heartBeat"));
                    tmp_monitorResponse.setDone(tmp.getBoolean("done"));
                    healthMonitor.getMonitorResponse().add(tmp_monitorResponse);
                    session.saveOrUpdate(healthMonitor);
                    session.saveOrUpdate(tmp_monitorResponse);
                    break;
                }
            }
        }
        
        if (tmp.getString("type").equals("DoctorAppointment"))
        {
            Iterator <DoctorAppointment> i = elderly.getDoctorAppointments().iterator();
            while(i.hasNext())
            {
                doctorAppointment = i.next();
                if(doctorAppointment.getId().toString().equals(tmp.get("monitor_id")))
                {
                    tmp_monitorResponse.setType(tmp.getString("type"));
                    tmp_monitorResponse.setDone(tmp.getBoolean("done"));
                    doctorAppointment.getMonitorResponse().add(tmp_monitorResponse);
                    session.saveOrUpdate(doctorAppointment);
                    break;
                }
            }
        }
        
        if (tmp.getString("type").equals("MedicationMonitor"))
        {
            Iterator <MedicationMonitor> i = elderly.getMedicationMonitors().iterator();
            while(i.hasNext())
            {
                medicationMonitor = i.next();
                if(medicationMonitor.getId().toString().equals( tmp.get("monitor_id")))
                {
                    tmp_monitorResponse.setType(tmp.getString("type"));
                    tmp_monitorResponse.setDone(tmp.getBoolean("done"));
                    medicationMonitor.getMonitorResponse().add(tmp_monitorResponse);
                    session.saveOrUpdate(medicationMonitor);
                    break;
                }
            }
        }
        
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
        return null;
    }
    
}
