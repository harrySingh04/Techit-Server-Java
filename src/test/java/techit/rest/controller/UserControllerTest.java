package techit.rest.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import techit.common.TestUser;
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
public class UserControllerTest
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
      
      User technician2 = new User();
      technician2.setFirstName("Tech");
      technician2.setLastName("Mat2");
      technician2.setUsername("tmat2");
      technician2.setEmail("tmat2@mail.com");
      technician2.setPassword(SecurityUtil.encryptPassword("abcd"));
      technician2.setUserRole(UserRole.TECHNICIAN);
      technician2 = userDao.saveUser(technician2);
      technician2Id = technician2.getId();
      
      User supervisor = new User();
      supervisor.setFirstName("SuperTech");
      supervisor.setLastName("Mat");
      supervisor.setUsername("stmat");
      supervisor.setEmail("stmat@mail.com");
      supervisor.setPassword(SecurityUtil.encryptPassword("abcd"));
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
      ticket1.setRequester(user);
      ticket1.setPhone("123");
      ticket1.setCreationDate(new Date());
      ticket1.setTechnicians(
         Arrays.asList(new User[] { technician1, technician2 }));
      ticket1.setSupervisors(Arrays.asList(new User[] { supervisor }));
      ticket1.setUnit(unit);
      ticket1 = ticketDao.saveTicket(ticket1);
      
      ticket1Id = ticket1.getId();
      
      Ticket ticket2 = new Ticket();
      ticket2.setDetails("SampleTicket-2");
      ticket2.setRequester(user);
      ticket2.setPhone("123");
      ticket2.setCreationDate(new Date());
      ticket2.setTechnicians(Arrays.asList(new User[] { technician1 }));
      ticket2.setSupervisors(Arrays.asList(new User[] { supervisor }));
      ticket2.setUnit(unit);
      ticket2 = ticketDao.saveTicket(ticket2);
      
      ticketDao.saveTicket(ticket2);
      
      user.addTicket(ticket1);
      user.addTicket(ticket2);
      
      userDao.saveUser(user);
      
      unit.addTicket(ticket1);
      unit.addTicket(ticket2);
      
      unitDao.saveUnit(unit);
      
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
   public void testGetUserByIdPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("gmat");
      mockMvc.perform(
         get("/users/{id}", userId).accept(MediaType.APPLICATION_JSON).header(
            "Authorization", "Bearer " + jwt)).andExpect(
               status().isOk()).andExpect(jsonPath("$.id").value(userId));
   }
   
   @Test
   public void testGetUserByIdFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("gmat");
      mockMvc.perform(
         get("/users/{id}", -1).accept(MediaType.APPLICATION_JSON).header(
            "Authorization", "Bearer " + jwt)).andExpect(
               status().isNotFound()).andExpect(
                  jsonPath("$.message").value(
                     "No User found in the system with id -1"));
   }
   
   @Test
   public void testCreateUserPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("sysadmin");
      
      User testUser = new TestUser();
      
      testUser.setFirstName("Test");
      testUser.setLastName("User");
      testUser.setUsername("testuser");
      testUser.setEmail("testuser@mail.com");
      testUser.setPassword("abcd");
      testUser.setUserRole(UserRole.USER);
      
      ObjectMapper mapper = new ObjectMapper();
      String userJsonString = mapper.writeValueAsString(testUser);
      
      mockMvc.perform(post("/users").accept(MediaType.APPLICATION_JSON).header(
         "Authorization", "Bearer " + jwt).contentType(
            MediaType.APPLICATION_JSON).content(userJsonString)).andExpect(
               status().isOk()).andExpect(
                  jsonPath("$.username").value("testuser"));
   }
   
   @Test
   public void testCreateUserFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("gmat");
      
      User testUser = new TestUser();
      
      testUser.setFirstName("Test");
      testUser.setLastName("User");
      testUser.setUsername("testuser");
      testUser.setEmail("testuser@mail.com");
      testUser.setPassword("abcd");
      testUser.setUserRole(UserRole.USER);
      
      ObjectMapper mapper = new ObjectMapper();
      String userJsonString = mapper.writeValueAsString(testUser);
      
      mockMvc.perform(post("/users").accept(MediaType.APPLICATION_JSON).header(
         "Authorization", "Bearer " + jwt).contentType(
            MediaType.APPLICATION_JSON).content(userJsonString)).andExpect(
               status().isForbidden()).andExpect(
                  jsonPath("$.message").value(
                     "The logged in user is not authroized to create/update users."));
   }
   
   @Test
   public void testEditUserPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("sysadmin");
      
      User user = new TestUser();
      user.setFirstName("Gen");
      user.setLastName("Mat");
      user.setUsername("gmat");
      user.setEmail("new@mail.com");
      user.setPassword(SecurityUtil.encryptPassword("abcd"));
      user.setUserRole(UserRole.USER);
      
      ObjectMapper mapper = new ObjectMapper();
      String userJsonString = mapper.writeValueAsString(user);
      
      mockMvc.perform(
         put("/users/" + userId).accept(MediaType.APPLICATION_JSON).header(
            "Authorization", "Bearer " + jwt).contentType(
               MediaType.APPLICATION_JSON).content(userJsonString)).andExpect(
                  status().isOk()).andExpect(
                     jsonPath("$.email").value("new@mail.com"));
   }
   
   @Test
   public void testEditUserFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("gmat");
      
      User user = new TestUser();
      user.setFirstName("Gen");
      user.setLastName("Mat");
      user.setUsername("gmat");
      user.setEmail("new@mail.com");
      user.setPassword(SecurityUtil.encryptPassword("abcd"));
      user.setUserRole(UserRole.USER);
      
      ObjectMapper mapper = new ObjectMapper();
      String userJsonString = mapper.writeValueAsString(user);
      
      mockMvc.perform(
         put("/users/" + -1).accept(MediaType.APPLICATION_JSON).header(
            "Authorization", "Bearer " + jwt).contentType(
               MediaType.APPLICATION_JSON).content(userJsonString)).andExpect(
                  status().isNotFound()).andExpect(
                     jsonPath("$.message").value(
                        "No User found in the system with user id -1"));
   }
   
   @Test
   public void testGetAllUsersPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("sysadmin");
      
      mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON).header(
         "Authorization", "Bearer " + jwt)).andExpect(
            status().isOk()).andExpect(jsonPath("$", hasSize(5)));
   }
   
   @Test
   public void testGetAllUsersFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("wrong-user");
      
      mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON).header(
         "Authorization", "Bearer " + jwt)).andExpect(
            status().isUnauthorized()).andExpect(
               jsonPath("$.message").value(
                  "Access denied! User token not valid."));
   }
   
   @Test
   public void testGetUserTicketsPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("sysadmin");
      
      mockMvc.perform(get("/users/" + userId + "/tickets").accept(
         MediaType.APPLICATION_JSON).header("Authorization",
            "Bearer " + jwt)).andExpect(status().isOk()).andExpect(
               jsonPath("$", hasSize(2))).andExpect(
                  jsonPath("$[0].details").value("SampleTicket-1"));
   }
   
   @Test
   public void testGetUserTicketsFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("gmat");
      
      mockMvc.perform(get("/users/" + -1 + "/tickets").accept(
         MediaType.APPLICATION_JSON).header("Authorization",
            "Bearer " + jwt)).andExpect(status().isNotFound()).andExpect(
               jsonPath("$.message").value(
                  "No User found in the system with userId -1"));
   }
   
   @Test
   public void testGetTechnicianTicketsPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("sysadmin");
      
      mockMvc.perform(get("/technicians/" + technician2Id + "/tickets").accept(
         MediaType.APPLICATION_JSON).header("Authorization",
            "Bearer " + jwt)).andExpect(status().isOk()).andExpect(
               jsonPath("$", hasSize(1))).andExpect(
                  jsonPath("$[0].id").value(ticket1Id));
   }
   
   @Test
   public void testGetTechnicianTicketsFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("sysadmin");
      
      mockMvc.perform(get("/technicians/" + -1 + "/tickets").accept(
         MediaType.APPLICATION_JSON).header("Authorization",
            "Bearer " + jwt)).andExpect(status().isNotFound()).andExpect(
               jsonPath("$.message").value(
                  "No Technician found in the system with userId -1"));
   }
}
