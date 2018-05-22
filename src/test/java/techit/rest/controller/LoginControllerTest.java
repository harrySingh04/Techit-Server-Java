package techit.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import techit.model.User;
import techit.model.dao.UserDao;
import techit.model.helper.UserRole;
import techit.rest.security.SecurityUtil;

@Test(groups = "TicketControllerTest")
@ContextConfiguration(locations = { "classpath:applicationContext.xml",
   "classpath:techit-servlet.xml" })
@WebAppConfiguration
public class LoginControllerTest
   extends AbstractTransactionalTestNGSpringContextTests {
   
   private MockMvc mockMvc;
   
   @Autowired
   private WebApplicationContext wac;
   
   @Autowired
   UserDao userDao;
   
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
   }
   
   void printCompleteResponse(ResultActions resultActions) throws Exception {
      if (resultActions != null) {
         resultActions.andDo(print());
      }
   }
   
   @Test
   public void testLoginPass() throws Exception {
      mockMvc.perform(
         post("/login").accept(MediaType.APPLICATION_JSON).contentType(
            MediaType.APPLICATION_JSON).param("username", "sysadmin").param(
               "password", "abcd")).andExpect(status().isOk()).andExpect(
                  jsonPath("$").value(SecurityUtil.convertToJWT("sysadmin")));
   }
   
   @Test
   public void testLoginFail() throws Exception {
      mockMvc.perform(
         post("/login").accept(MediaType.APPLICATION_JSON).contentType(
            MediaType.APPLICATION_JSON).param("username", "sysadmin").param(
               "password", "wrongpassword")).andExpect(
                  status().isUnauthorized()).andExpect(
                     jsonPath("$.message").value(
                        "Invalid Username and/or password"));
   }
}
