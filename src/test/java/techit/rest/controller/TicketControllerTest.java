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

import techit.model.Ticket;
import techit.model.TicketUpdate;
import techit.model.Unit;
import techit.model.User;
import techit.model.dao.TicketDao;
import techit.model.dao.UnitDao;
import techit.model.dao.UserDao;
import techit.model.helper.TicketStatus;
import techit.model.helper.UserRole;
import techit.rest.security.SecurityUtil;

@Test(groups = "UserControllerTest")
@ContextConfiguration(locations = { "classpath:applicationContext.xml",
   "classpath:techit-servlet.xml" })
@WebAppConfiguration
public class TicketControllerTest
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
   int ticket2Id;
   int unitId;
   
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
      
      unitId = unit.getId();
      
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
      
      ticket2Id = ticket2.getId();
      
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
   public void testGetTicketByIdPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("gmat");
      mockMvc.perform(get("/tickets/{id}", ticket1Id).accept(
         MediaType.APPLICATION_JSON).header("Authorization",
            "Bearer " + jwt)).andExpect(status().isOk()).andExpect(
               jsonPath("$.details").value("SampleTicket-1"));
   }
   
   @Test
   public void testGetTicketByIdFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("gmat");
      mockMvc.perform(
         get("/tickets/{id}", -1).accept(MediaType.APPLICATION_JSON).header(
            "Authorization", "Bearer " + jwt)).andExpect(
               status().isNotFound()).andExpect(
                  jsonPath("$.message").value(
                     "No Ticket found in the system with id -1"));
   }
   
   @Test
   public void testCreateTicketPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("gmat");
      
      Ticket ticket = new Ticket();
      
      ticket.setDetails("TestTicket-1");
      ticket.setPhone("567");
      ticket.setTechnicians(
         Arrays.asList(userDao.getTechnician(technician1Id)));
      ticket.setSupervisors(
         Arrays.asList(new User[] { userDao.getSupervisor(supervisorId) }));
      ticket.setUnit(unitDao.getUnit(unitId));
      
      ObjectMapper mapper = new ObjectMapper();
      String ticketJsonString = mapper.writeValueAsString(ticket);
      
      mockMvc.perform(
         post("/tickets").accept(MediaType.APPLICATION_JSON).header(
            "Authorization", "Bearer " + jwt).contentType(
               MediaType.APPLICATION_JSON).content(ticketJsonString)).andExpect(
                  status().isOk()).andExpect(
                     jsonPath("$.details").value("TestTicket-1")).andExpect(
                        jsonPath("$.phone").value("567"));
   }
   
   @Test
   public void testCreateTicketFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("gmat");
      
      Ticket ticket = new Ticket();
      
      ticket.setPhone("567");
      ticket.setTechnicians(
         Arrays.asList(userDao.getTechnician(technician1Id)));
      ticket.setSupervisors(
         Arrays.asList(new User[] { userDao.getSupervisor(supervisorId) }));
      ticket.setUnit(unitDao.getUnit(unitId));
      
      ObjectMapper mapper = new ObjectMapper();
      String ticketJsonString = mapper.writeValueAsString(ticket);
      
      mockMvc.perform(
         post("/tickets").accept(MediaType.APPLICATION_JSON).header(
            "Authorization", "Bearer " + jwt).contentType(
               MediaType.APPLICATION_JSON).content(ticketJsonString)).andExpect(
                  status().isBadRequest()).andExpect(
                     jsonPath("$.message").value(
                        "Ticket details can not be empty."));
   }
   
   @Test
   public void testEditTicketPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("gmat");
      
      Ticket ticket = ticketDao.getTicket(ticket1Id);
      
      ticket.setDetails("New Detail");
      
      ObjectMapper mapper = new ObjectMapper();
      String ticketJsonString = mapper.writeValueAsString(ticket);
      
      mockMvc.perform(
         put("/tickets/" + ticket1Id).accept(MediaType.APPLICATION_JSON).header(
            "Authorization", "Bearer " + jwt).contentType(
               MediaType.APPLICATION_JSON).content(ticketJsonString)).andExpect(
                  status().isOk()).andExpect(
                     jsonPath("$.details").value("New Detail")).andExpect(
                        jsonPath("$.phone").value("123"));
   }
   
   @Test
   public void testEditTicketFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("gmat");
      
      Ticket ticket = ticketDao.getTicket(ticket2Id);
      
      ticket.setDetails("New Detail");
      
      ObjectMapper mapper = new ObjectMapper();
      String ticketJsonString = mapper.writeValueAsString(ticket);
      
      mockMvc.perform(
         put("/tickets/" + -1).accept(MediaType.APPLICATION_JSON).header(
            "Authorization", "Bearer " + jwt).contentType(
               MediaType.APPLICATION_JSON).content(ticketJsonString)).andExpect(
                  status().isNotFound()).andExpect(
                     jsonPath("$.message").value(
                        "No Ticket found in the system with id -1"));
   }
   
   @Test
   public void testGetAllTicketsPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("sysadmin");
      
      mockMvc.perform(get("/tickets").accept(MediaType.APPLICATION_JSON).header(
         "Authorization", "Bearer " + jwt)).andExpect(
            status().isOk()).andExpect(jsonPath("$", hasSize(2)));
   }
   
   @Test
   public void testGetAllTicketsFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("wrong-user");
      
      mockMvc.perform(get("/tickets").accept(MediaType.APPLICATION_JSON).header(
         "Authorization", "Bearer " + jwt)).andExpect(
            status().isUnauthorized()).andExpect(
               jsonPath("$.message").value(
                  "Access denied! User token not valid."));
   }
   
   @Test
   public void testGetTicketTechniciansPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("sysadmin");
      
      mockMvc.perform(get("/tickets/" + ticket1Id + "/technicians").accept(
         MediaType.APPLICATION_JSON).header("Authorization",
            "Bearer " + jwt)).andExpect(status().isOk()).andExpect(
               jsonPath("$", hasSize(2))).andExpect(
                  jsonPath("$[0].username").value("tmat1")).andExpect(
                     jsonPath("$[1].username").value("tmat2"));
   }
   
   @Test
   public void testGetTicketTechniciansFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("sysadmin");
      
      mockMvc.perform(get("/tickets/" + -1 + "/technicians").accept(
         MediaType.APPLICATION_JSON).header("Authorization",
            "Bearer " + jwt)).andExpect(status().isNotFound()).andExpect(
               jsonPath("$.message").value(
                  "No Ticket found in the system with id -1"));
   }
   
   @Test
   public void testAssignTechnicianToTicketBySupervisorPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("stmat");
      
      mockMvc.perform(
         put("/tickets/" + ticket2Id + "/technicians/" + technician2Id).accept(
            MediaType.APPLICATION_JSON).header("Authorization",
               "Bearer " + jwt)).andExpect(status().isOk());
      
      mockMvc.perform(get("/tickets/" + ticket2Id + "/technicians").accept(
         MediaType.APPLICATION_JSON).header("Authorization",
            "Bearer " + jwt)).andExpect(status().isOk()).andExpect(
               jsonPath("$", hasSize(2))).andExpect(
                  jsonPath("$[0].username").value("tmat1")).andExpect(
                     jsonPath("$[1].username").value("tmat2"));
   }
   
   @Test
   public void testAssignTechnicianToTicketByTechnicianPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("tmat2");
      
      mockMvc.perform(
         put("/tickets/" + ticket2Id + "/technicians/" + technician2Id).accept(
            MediaType.APPLICATION_JSON).header("Authorization",
               "Bearer " + jwt)).andExpect(status().isOk());
      
      mockMvc.perform(get("/tickets/" + ticket2Id + "/technicians").accept(
         MediaType.APPLICATION_JSON).header("Authorization",
            "Bearer " + jwt)).andExpect(status().isOk()).andExpect(
               jsonPath("$", hasSize(2))).andExpect(
                  jsonPath("$[0].username").value("tmat1")).andExpect(
                     jsonPath("$[1].username").value("tmat2"));
   }
   
   @Test
   public void testAssignTechnicianToTicketFail1() throws Exception {
      String jwt = SecurityUtil.convertToJWT("stmat");
      
      mockMvc.perform(
         put("/tickets/" + ticket1Id + "/technicians/" + technician1Id).accept(
            MediaType.APPLICATION_JSON).header("Authorization",
               "Bearer " + jwt)).andExpect(status().isBadRequest()).andExpect(
                  jsonPath("$.message").value("Technician with userId "
                     + technician1Id + " is already assigned to ticket with id "
                     + ticket1Id));
   }
   
   @Test
   public void testAssignTechnicianToTicketFail2() throws Exception {
      String jwt = SecurityUtil.convertToJWT("tmat1");
      
      mockMvc.perform(
         put("/tickets/" + ticket1Id + "/technicians/" + technician2Id).accept(
            MediaType.APPLICATION_JSON).header("Authorization",
               "Bearer " + jwt)).andExpect(status().isForbidden()).andExpect(
                  jsonPath("$.message").value(
                     "The logged in user is can not assign tickets to this technician."));
   }
   
   @Test
   public void testUpdateTicketStatusPass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("tmat1");
      
      mockMvc.perform(put(
         "/tickets/" + ticket1Id + "/status/" + TicketStatus.INPROGRESS).accept(
            MediaType.APPLICATION_JSON).header("Authorization",
               "Bearer " + jwt)).andExpect(status().isOk());
      
      mockMvc.perform(
         get("/tickets/" + ticket1Id).accept(MediaType.APPLICATION_JSON).header(
            "Authorization", "Bearer " + jwt)).andExpect(
               status().isOk()).andExpect(
                  jsonPath("$.currentStatus").value(
                     TicketStatus.INPROGRESS.name()));
   }
   
   @Test
   public void testUpdateTicketStatusFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("tmat2");
      
      mockMvc.perform(put(
         "/tickets/" + ticket2Id + "/status/" + TicketStatus.INPROGRESS).accept(
            MediaType.APPLICATION_JSON).header("Authorization",
               "Bearer " + jwt)).andExpect(status().isForbidden()).andExpect(
                  jsonPath("$.message").value(
                     "The logged in user is not authroized to update this ticket."));
   }
   
   @Test
   public void testCreateTicketUpdatePass() throws Exception {
      String jwt = SecurityUtil.convertToJWT("tmat1");
      
      TicketUpdate ticketUpdate = new TicketUpdate();
      
      ticketUpdate.setUpdateDetail("TestTicketUpdate");
      
      ObjectMapper mapper = new ObjectMapper();
      String ticketJsonString = mapper.writeValueAsString(ticketUpdate);
      
      mockMvc.perform(post("/tickets/" + ticket1Id + "/updates").accept(
         MediaType.APPLICATION_JSON).header("Authorization",
            "Bearer " + jwt).contentType(MediaType.APPLICATION_JSON).content(
               ticketJsonString)).andExpect(status().isOk());
      
      mockMvc.perform(get("/tickets/" + ticket1Id + "/updates").accept(
         MediaType.APPLICATION_JSON).header("Authorization",
            "Bearer " + jwt)).andExpect(status().isOk()).andExpect(
               jsonPath("$", hasSize(1))).andExpect(
                  jsonPath("$[0].updateDetail").value("TestTicketUpdate"));
   }
   
   @Test
   public void testCreateTicketUpdateFail() throws Exception {
      String jwt = SecurityUtil.convertToJWT("tmat1");
      
      TicketUpdate ticketUpdate = new TicketUpdate();
      
      ticketUpdate.setUpdateDetail("TestTicketUpdate");
      
      ObjectMapper mapper = new ObjectMapper();
      String ticketJsonString = mapper.writeValueAsString(ticketUpdate);
      
      mockMvc.perform(post("/tickets/" + -1 + "/updates").accept(
         MediaType.APPLICATION_JSON).header("Authorization",
            "Bearer " + jwt).contentType(MediaType.APPLICATION_JSON).content(
               ticketJsonString)).andExpect(status().isNotFound()).andExpect(
                  jsonPath("$.message").value(
                     "No Ticket found in the system with id -1"));
   }
}
