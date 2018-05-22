package techit.model.dao;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import techit.exception.TicketNotFoundException;
import techit.model.Ticket;
import techit.model.Unit;
import techit.model.User;
import techit.model.helper.UserRole;

@Test(groups = "TicketDaoTest")
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TicketDaoTest
   extends AbstractTransactionalTestNGSpringContextTests {
   
   @Autowired
   UserDao userDao;
   @Autowired
   TicketDao ticketDao;
   @Autowired
   UnitDao unitDao;
   
   int userId;
   int technician1Id;
   int technician2Id;
   int supervisorId;
   int sampleTicketId;
   
   @BeforeMethod
   public void beforeTest() throws TicketNotFoundException {
      User user = new User();
      user.setFirstName("Gen");
      user.setLastName("Mat");
      user.setUsername("gmat");
      user.setEmail("gmat@mail.com");
      user.setPassword("abcd");
      user.setUserRole(UserRole.USER);
      user = userDao.saveUser(user);
      userId = user.getId();
      
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
      
      sampleTicketId = ticketDao.saveTicket(ticket1).getId();
      
      Ticket ticket2 = new Ticket();
      ticket2.setDetails("SampleTicket-2");
      ticket2.setRequester(userDao.getUser(userId));
      ticket2.setPhone("123");
      ticket2.setCreationDate(new Date());
      ticket2.setTechnicians(Arrays.asList(new User[] { technician1 }));
      ticket2.setSupervisors(Arrays.asList(new User[] { supervisor }));
      ticket2.setUnit(unit);
      
      ticketDao.saveTicket(ticket2);
   }
   
   @Test
   public void testSaveTicket() {
      
      Ticket ticket = new Ticket();
      ticket.setDetails("TestTicket");
      ticket.setRequester(userDao.getUser(userId));
      ticket.setPhone("123");
      ticket.setCreationDate(new Date());
      ticket.setTechnicians(
         Arrays.asList(new User[] { userDao.getTechnician(technician1Id) }));
      
      ticket = ticketDao.saveTicket(ticket);
      
      assert ticket.getId() != null;
   }
   
   @Test
   public void testGetTicket() {
      
      Ticket ticket = ticketDao.getTicket(sampleTicketId);
      
      assert (ticket != null) && (ticket.getId() != null);
      
      assert (ticket.getTechnicians() != null)
         && (ticket.getTechnicians().size() == 2);
      
      Set<String> technicianNameSet = new HashSet<>();
      ticket.getTechnicians().forEach(
         t -> technicianNameSet.add(t.getUsername()));
      
      assert (technicianNameSet.size() == 2)
         && technicianNameSet.contains("tmat1")
         && technicianNameSet.contains("tmat2");
      
      assert (ticket.getUnit() != null) && (ticket.getUnit().getId() != null);
   }
   
   @Test
   public void testGetAllTickets() {
      
      List<Ticket> tickets = ticketDao.getTickets();
      
      assert (tickets != null) && (tickets.size() == 2);
      
      assert (tickets.get(0).getTechnicians() != null)
         && (tickets.get(0).getTechnicians().size() == 2);
      
      assert (tickets.get(0).getSupervisors() != null)
         && (tickets.get(0).getSupervisors().size() == 1);
      
      assert (tickets.get(1).getTechnicians() != null)
         && (tickets.get(1).getTechnicians().size() == 1);
      
      assert (tickets.get(1).getSupervisors() != null)
         && (tickets.get(1).getSupervisors().size() == 1);
   }
}
