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
public class UserDaoTest extends AbstractTransactionalTestNGSpringContextTests {
   
   @Autowired
   UserDao userDao;
   @Autowired
   UnitDao unitDao;
   @Autowired
   TicketDao ticketDao;
   
   int userId;
   int technician1Id;
   int technician2Id;
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
      userId = userDao.saveUser(user).getId();
      
      User technician1 = new User();
      technician1.setFirstName("Tech");
      technician1.setLastName("Mat1");
      technician1.setUsername("tmat1");
      technician1.setEmail("tmat1@mail.com");
      technician1.setPassword("abcd");
      technician1.setUserRole(UserRole.TECHNICIAN);
      technician1 = userDao.saveUser(technician1);
      technician1Id = technician1.getId();
      
      User technician2 = new User();
      technician2.setFirstName("Tech");
      technician2.setLastName("Mat2");
      technician2.setUsername("tmat2");
      technician2.setEmail("tmat2@mail.com");
      technician2.setPassword("abcd");
      technician2.setUserRole(UserRole.TECHNICIAN);
      technician2 = userDao.saveUser(technician2);
      technician2Id = technician2.getId();
      
      User supervisor = new User();
      supervisor.setFirstName("SuperTech");
      supervisor.setLastName("Mat");
      supervisor.setUsername("stmat");
      supervisor.setEmail("stmat@mail.com");
      supervisor.setPassword("abcd");
      supervisor.setUserRole(UserRole.SUPERVISOR);
      supervisor = userDao.saveUser(supervisor);
      supervisorId = supervisor.getId();
      
      Unit unit = new Unit();
      unit.setUnitName("SampleUnit");
      unit.setLocation("Los Angeles");
      unit.setDescription("Sample unit for testing");
      unit = unitDao.saveUnit(unit);
      
      Ticket ticket1 = new Ticket();
      ticket1.setDetails("SampleTicket-1");
      ticket1.setRequester(userDao.getUser(userId));
      ticket1.setPhone("123");
      ticket1.setCreationDate(new Date());
      ticket1.setTechnicians(
         Arrays.asList(new User[] { technician1, technician2 }));
      ticket1.setSupervisors(Arrays.asList(new User[] { supervisor }));
      ticket1.setUnit(unit);
      ticket1 = ticketDao.saveTicket(ticket1);
      
      Ticket ticket2 = new Ticket();
      ticket2.setDetails("SampleTicket-2");
      ticket2.setRequester(userDao.getUser(userId));
      ticket2.setPhone("123");
      ticket2.setCreationDate(new Date());
      ticket2.setTechnicians(Arrays.asList(new User[] { technician1 }));
      ticket2.setSupervisors(Arrays.asList(new User[] { supervisor }));
      ticket2.setUnit(unit);
      
      ticket2 = ticketDao.saveTicket(ticket2);
   }
   
   @Test
   public void testGetUser() {
      User user = userDao.getUser(userId);
      
      assert (user != null) && (user.getId() != null);
      
      assert user.getUsername() == "gmat";
      
      assert user.getFirstName() == "Gen";
      
      assert user.getLastName() == "Mat";
   }
   
   @Test
   public void testGetTechnician() {
      User user = userDao.getTechnician(technician1Id);
      
      assert (user != null) && (user.getId() != null);
      
      assert user.getUsername() == "tmat1";
      
      assert user.getFirstName() == "Tech";
      
      assert user.getLastName() == "Mat1";
   }
   
   @Test
   public void testGetSupervisor() {
      User user = userDao.getSupervisor(supervisorId);
      
      assert (user != null) && (user.getId() != null);
      
      assert user.getUsername() == "stmat";
      
      assert user.getFirstName() == "SuperTech";
      
      assert user.getLastName() == "Mat";
   }
   
   @Test
   public void testGetAllUsers() {
      List<User> users = userDao.getUsers();
      
      assert (users != null) && (users.size() == 4);
      
      assert users.get(0).getUserRole() == UserRole.USER;
      
      assert users.get(1).getUserRole() == UserRole.TECHNICIAN;
      
      assert users.get(2).getUserRole() == UserRole.TECHNICIAN;
      
      assert users.get(3).getUserRole() == UserRole.SUPERVISOR;
   }
   
   @Test
   public void testSaveUser() {
      User user = new User();
      user.setUsername("Tom");
      user.setPassword("abcd");
      user.setFirstName("TomFirst");
      user.setLastName("TomSecond");
      
      user = userDao.saveUser(user);
      
      assert user.getId() != null;
   }
   
   @Test
   public void testGetUserByUsername() {
      User res = userDao.getUser("tmat1");
      
      assert (res != null) && (res.getId() != null);
      
      assert res.getFirstName() == "Tech";
   }
   
   @Test
   public void testGetTechnicianByUsername() {
      User user = userDao.getTechnician("tmat2");
      
      assert (user != null) && (user.getId() != null);
      
      assert user.getUsername() == "tmat2";
      
      assert user.getFirstName() == "Tech";
      
      assert user.getLastName() == "Mat2";
   }
   
   @Test
   public void testGetSupervisorByUsername() {
      User user = userDao.getSupervisor("stmat");
      
      assert (user != null) && (user.getId() != null);
      
      assert user.getUsername() == "stmat";
      
      assert user.getFirstName() == "SuperTech";
      
      assert user.getLastName() == "Mat";
   }
}
