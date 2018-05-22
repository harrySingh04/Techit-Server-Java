package techit.model.dao;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import techit.model.Ticket;
import techit.model.Unit;
import techit.model.User;
import techit.model.helper.UserRole;

@Test(groups = "UserDaoTest")
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UnitDaoTest extends AbstractTransactionalTestNGSpringContextTests {
   
   @Autowired
   UnitDao unitDao;
   @Autowired
   UserDao userDao;
   @Autowired
   TicketDao ticketDao;
   
   int sampleUnitId;
   int userId;
   int technicianId;
   int supervisorId;
   
   @BeforeMethod
   public void beforeMethod() {
      User user = new User();
      user.setFirstName("Gen");
      user.setLastName("Mat");
      user.setUsername("gmat");
      user.setEmail("gmat@mail.com");
      user.setPassword("abcd");
      user.setUserRole(UserRole.USER);
      user = userDao.saveUser(user);
      userId = user.getId();
      
      User technician = new User();
      technician.setFirstName("Tech");
      technician.setLastName("Mat");
      technician.setUsername("tmat");
      technician.setEmail("tmat@mail.com");
      technician.setPassword("abcd");
      technician.setUserRole(UserRole.TECHNICIAN);
      technician = userDao.saveUser(technician);
      technicianId = technician.getId();
      
      User supervisor = new User();
      supervisor.setFirstName("SuperTech");
      supervisor.setLastName("Mat");
      supervisor.setUsername("stmat");
      supervisor.setEmail("stmat@mail.com");
      supervisor.setPassword("abcd");
      supervisor.setUserRole(UserRole.SUPERVISOR);
      supervisor = userDao.saveUser(supervisor);
      supervisorId = supervisor.getId();
      
      Ticket ticket1 = new Ticket();
      ticket1.setDetails("SampleTicket-1");
      ticket1.setRequester(userDao.getUser(userId));
      ticket1.setPhone("123");
      ticket1.setCreationDate(new Date());
      ticket1 = ticketDao.saveTicket(ticket1);
      
      Ticket ticket2 = new Ticket();
      ticket2.setDetails("SampleTicket-2");
      ticket2.setRequester(userDao.getUser(userId));
      ticket2.setPhone("123");
      ticket2.setCreationDate(new Date());
      ticket2 = ticketDao.saveTicket(ticket2);
      
      Unit unit1 = new Unit();
      unit1.setUnitName("SampleUnit-1");
      unit1.setLocation("California");
      unit1.setPhoneNumber("789");
      unit1.setTechnicians(Arrays.asList(new User[] { technician }));
      unit1.setSupervisors(Arrays.asList(new User[] { supervisor }));
      unit1.addTicket(ticket1);
      unit1 = unitDao.saveUnit(unit1);
      sampleUnitId = unit1.getId();
      
      Unit unit2 = new Unit();
      unit2.setUnitName("SampleUnit-2");
      unit2.setLocation("Nevada");
      unit2.setPhoneNumber("101");
      unit2.setSupervisors(Arrays.asList(new User[] { supervisor }));
      unit2.addTicket(ticket1);
      unit2.addTicket(ticket2);
      unit2 = unitDao.saveUnit(unit2);
   }
   
   @Test
   public void testSaveUnit() {
      Unit unit = new Unit();
      unit.setUnitName("TestUnit");
      unit.setLocation("Florida");
      unit.setPhoneNumber("123");
      
      unit = unitDao.saveUnit(unit);
      
      assert (unit != null) && (unit.getId() != null);
   }
   
   @Test
   public void testGetUnit() {
      Unit unit = unitDao.getUnit(sampleUnitId);
      
      assert (unit != null) && (unit.getId() != null);
      
      assert (unit.getTechnicians() != null)
         && (unit.getTechnicians().size() == 1);
      
      assert (unit.getSupervisors() != null)
         && (unit.getSupervisors().size() == 1);
      
      assert (unit.getTickets() != null) && (unit.getTickets().size() == 1);
   }
   
   @Test
   public void testGetAllUnits() {
      List<Unit> units = unitDao.getUnits();
      
      assert (units != null) && (units.size() == 2);
      
      assert (units.get(0).getTechnicians() != null)
         && (units.get(0).getTechnicians().size() == 1);
      
      assert (units.get(0).getSupervisors() != null)
         && (units.get(0).getSupervisors().size() == 1);
      
      assert (units.get(0).getTickets() != null)
         && (units.get(0).getTickets().size() == 1);
      
      assert (units.get(1).getTechnicians() == null)
         || (units.get(1).getTechnicians().size() == 0);
      
      assert (units.get(1).getSupervisors() != null)
         && (units.get(1).getSupervisors().size() == 1);
      
      assert (units.get(1).getTickets() != null)
         && (units.get(1).getTickets().size() == 2);
   }
}
