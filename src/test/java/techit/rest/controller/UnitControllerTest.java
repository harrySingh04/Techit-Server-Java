package techit.rest.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import techit.model.Ticket;
import techit.model.Unit;
import techit.model.User;
import techit.model.dao.TicketDao;
import techit.model.dao.UnitDao;
import techit.model.dao.UserDao;
import techit.model.helper.UserRole;
import techit.rest.security.SecurityUtil;

@Test(groups = "UserControllerTest")
@ContextConfiguration(locations = { "classpath:applicationContext.xml",
   "classpath:techit-servlet.xml" })
@WebAppConfiguration
public class UnitControllerTest
   extends AbstractTransactionalTestNGSpringContextTests {
   
   private MockMvc mockMvc;
   
   @Autowired
   private WebApplicationContext wac;
   
   @Autowired
   UserDao userDao;
   @Autowired
   TicketDao ticketDao;
   @Autowired
   UnitDao unitDao;
   
   int sysadminId;
   int userId;
   int unit1Id;
   int technician1Id;
   int technician2Id;
   int supervisorId;
   int ticket1Id;
   
   @BeforeClass
   public void init() {
      mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
   }
   
   @BeforeMethod
   public void beforeTest() throws JsonProcessingException, Exception {
      User sysadmin = new User();
      sysadmin.setFirstName("System");
      sysadmin.setLastName("Administrator");
      sysadmin.setUsername("sysadmin");
      sysadmin.setEmail("sysadmin@mail.com");
      sysadmin.setPassword(SecurityUtil.encryptPassword("abcd"));
      sysadmin.setUserRole(UserRole.SYS_ADMIN);
      sysadmin = userDao.saveUser(sysadmin);
      sysadminId = sysadmin.getId();
      
      Unit unit1 = new Unit();
      unit1.setUnitName("SampleUnit-1");
      unit1.setLocation("Los Angeles");
      unit1.setDescription("Sample unit for testing-1");
      unit1 = unitDao.saveUnit(unit1);
      
      unit1Id = unit1.getId();
      
      Unit unit2 = new Unit();
      unit2.setUnitName("SampleUnit-2");
      unit2.setLocation("Denver");
      unit2.setDescription("Sample unit for testing-2");
      unit2 = unitDao.saveUnit(unit2);
      
      User user = new User();
      user.setFirstName("Gen");
      user.setLastName("Mat");
      user.setUsername("gmat");
      user.setEmail("gmat@mail.com");
      user.setPassword(SecurityUtil.encryptPassword("abcd"));
      user.setUserRole(UserRole.USER);
      user = userDao.saveUser(user);
      userId = user.getId();
      
      User technician1 = new User();
      technician1.setFirstName("Tech");
      technician1.setLastName("Mat1");
      technician1.setUsername("tmat1");
      technician1.setEmail("tmat1@mail.com");
      technician1.setPassword(SecurityUtil.encryptPassword("abcd"));
      technician1.setUserRole(UserRole.TECHNICIAN);
      technician1 = userDao.saveUser(technician1);
      technician1Id = technician1.getId();
      
      unit1.addTechnician(technician1);
      unitDao.saveUnit(unit1);
      
      User technician2 = new User();
      technician2.setFirstName("Tech");
      technician2.setLastName("Mat2");
      technician2.setUsername("tmat2");
      technician2.setEmail("tmat2@mail.com");
      technician2.setPassword(SecurityUtil.encryptPassword("abcd"));
      technician2.setUserRole(UserRole.TECHNICIAN);
      technician2 = userDao.saveUser(technician2);
      technician2Id = technician2.getId();
      
      unit2.addTechnician(technician2);
      unitDao.saveUnit(unit2);
      
      User supervisor = new User();
      supervisor.setFirstName("SuperTech");
      supervisor.setLastName("Mat");
      supervisor.setUsername("stmat");
      supervisor.setEmail("stmat@mail.com");
      supervisor.setPassword(SecurityUtil.encryptPassword("abcd"));
      supervisor.setUserRole(UserRole.SUPERVISOR);
      supervisor = userDao.saveUser(supervisor);
      supervisorId = supervisor.getId();
      
      Ticket ticket1 = new Ticket();
      ticket1.setDetails("SampleTicket-1");
      ticket1.setRequester(user);
      ticket1.setPhone("123");
      ticket1.setCreationDate(new Date());
      ticket1.setTechnicians(
         Arrays.asList(new User[] { technician1, technician2 }));
      ticket1.setSupervisors(Arrays.asList(new User[] { supervisor }));
      ticket1.setUnit(unit1);
      ticket1 = ticketDao.saveTicket(ticket1);
      
      ticket1Id = ticket1.getId();
      
      Ticket ticket2 = new Ticket();
      ticket2.setDetails("SampleTicket-2");
      ticket2.setRequester(user);
      ticket2.setPhone("123");
      ticket2.setCreationDate(new Date());
      ticket2.setTechnicians(Arrays.asList(new User[] { technician1 }));
      ticket2.setSupervisors(Arrays.asList(new User[] { supervisor }));
      ticket2.setUnit(unit1);
      ticket2 = ticketDao.saveTicket(ticket2);
      
      ticketDao.saveTicket(ticket2);
      
      user.addTicket(ticket1);
      user.addTicket(ticket2);
      
      userDao.saveUser(user);
      
      unit1.addTicket(ticket1);
      unit1.addTicket(ticket2);
      
      unitDao.saveUnit(unit1);
      
      technician1.addAssignedTicket(ticket1);
      technician1.addAssignedTicket(ticket2);
      
      userDao.saveUser(technician1);
      
      technician2.addAssignedTicket(ticket1);
      
      userDao.saveUser(technician2);
   }
   
   void printCompleteResponse(ResultActions resultActions) throws Exception {
      if (resultActions != null) {
         resultActions.andDo(print());
      }
   }
   
   @Test
   public void testGetAllUnitsPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("gmat");
      mockMvc.perform(get("/units").accept(MediaType.APPLICATION_JSON).header(
         "Authorization", "Bearer " + jwt)).andExpect(
            status().isOk()).andExpect(
               jsonPath("$[0].location").value("Los Angeles")).andExpect(
                  jsonPath("$[1].location").value("Denver"));
   }
   
   @Test
   public void testGetAllUnitsFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("unknownuser");
      mockMvc.perform(get("/units").accept(MediaType.APPLICATION_JSON).header(
         "Authorization", "Bearer " + jwt)).andExpect(
            status().isUnauthorized()).andExpect(
               jsonPath("$.message").value(
                  "Access denied! User token not valid."));
   }
   
   @Test
   public void testCreateUnitPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("sysadmin");
      
      Unit testUnit = new Unit();
      
      testUnit.setUnitName("TestUnit-1");
      testUnit.setLocation("Seatle");
      testUnit.setDescription("Test Unit");
      testUnit.setEmail("testunit@mail.com");
      testUnit.setPhoneNumber("111");
      
      ObjectMapper mapper = new ObjectMapper();
      String unitJsonString = mapper.writeValueAsString(testUnit);
      
      mockMvc.perform(post("/units").accept(MediaType.APPLICATION_JSON).header(
         "Authorization", "Bearer " + jwt).contentType(
            MediaType.APPLICATION_JSON).content(unitJsonString)).andExpect(
               status().isOk()).andExpect(
                  jsonPath("$.unitName").value("TestUnit-1"));
   }
   
   @Test
   public void testCreateUnitFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("gmat");
      
      Unit testUnit = new Unit();
      
      testUnit.setUnitName("TestUnit-1");
      testUnit.setLocation("Seatle");
      testUnit.setDescription("Test Unit");
      testUnit.setEmail("testunit@mail.com");
      testUnit.setPhoneNumber("111");
      
      ObjectMapper mapper = new ObjectMapper();
      String unitJsonString = mapper.writeValueAsString(testUnit);
      
      mockMvc.perform(post("/units").accept(MediaType.APPLICATION_JSON).header(
         "Authorization", "Bearer " + jwt).contentType(
            MediaType.APPLICATION_JSON).content(unitJsonString)).andExpect(
               status().isForbidden()).andExpect(
                  jsonPath("$.message").value(
                     "The logged in user is not authroized to create/update units."));
   }
   
   @Test
   public void testGetUnitTicketsPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("sysadmin");
      
      mockMvc.perform(get("/units/" + unit1Id + "/tickets").accept(
         MediaType.APPLICATION_JSON).header("Authorization",
            "Bearer " + jwt)).andExpect(status().isOk()).andExpect(
               jsonPath("$", hasSize(2))).andExpect(
                  jsonPath("$[0].details").value("SampleTicket-1"));
   }
   
   @Test
   public void testGetUnitTicketsFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("sysadmin");
      
      mockMvc.perform(get("/units/" + -1 + "/tickets").accept(
         MediaType.APPLICATION_JSON).header("Authorization",
            "Bearer " + jwt)).andExpect(status().isNotFound()).andExpect(
               jsonPath("$.message").value(
                  "No Unit found in the system with id -1"));
   }
   
   @Test
   public void testGetUnitTechniciansPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("sysadmin");
      
      mockMvc.perform(get("/units/" + unit1Id + "/technicians").accept(
         MediaType.APPLICATION_JSON).header("Authorization",
            "Bearer " + jwt)).andExpect(status().isOk()).andExpect(
               jsonPath("$", hasSize(1))).andExpect(
                  jsonPath("$[0].id").value(technician1Id));
   }
   
   @Test
   public void testGetUnitTechniciansFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("sysadmin");
      
      mockMvc.perform(get("/units/" + -1 + "/technicians").accept(
         MediaType.APPLICATION_JSON).header("Authorization",
            "Bearer " + jwt)).andExpect(status().isNotFound()).andExpect(
               jsonPath("$.message").value(
                  "No Unit found in the system with id -1"));
   }
}
