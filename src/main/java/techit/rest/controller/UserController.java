package techit.rest.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import techit.model.Ticket;
import techit.model.User;
import techit.model.dao.UserDao;
import techit.model.helper.UserRole;
import techit.rest.error.RestException;

@RestController
@Transactional
public class UserController {
   private static final Log sLogger = LogFactory.getLog(UserController.class);
   
   @Autowired
   private UserDao userDao;
   
   @RequestMapping(value = "/users", method = RequestMethod.GET)
   public List<User> getAllUsers(
      @ModelAttribute("loggedInUser") User loggedInUser) throws Exception {
      
      try {
         if (loggedInUser == null) {
            throw new RestException(401, "User not logged in.");
         }
         
         if (loggedInUser.getUserRole() != UserRole.SYS_ADMIN) {
            sLogger.warn("Logged in user " + loggedInUser.getUsername()
               + " is not admin");
            
            throw new RestException(403,
               "The logged in user is not authroized to retrieve all users information.");
         }
         
         return userDao.getUsers();
      }
      catch (Exception e) {
         e.printStackTrace();
         throw e;
      }
   }
   
   @RequestMapping(value = "/users", method = RequestMethod.POST)
   public User createUser(@ModelAttribute("loggedInUser") User loggedInUser,
      @RequestBody User user) {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      sLogger.info("createUser called by " + loggedInUser.getUsername()
         + " to create a new user with username " + user.getUsername());
      
      if ((user.getUsername() == null) || (user.getPassword() == null)) {
         throw new RestException(400, "Missing username and/or password.");
      }
      
      if (loggedInUser.getUserRole() != UserRole.SYS_ADMIN) {
         throw new RestException(403,
            "The logged in user is not authroized to create/update users.");
      }
      
      return userDao.saveUser(user);
   }
   
   @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
   public User getUserById(@ModelAttribute("loggedInUser") User loggedInUser,
      @PathVariable int userId) {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      User user = userDao.getUser(userId);
      
      if (user == null) {
         throw new RestException(404,
            "No User found in the system with id " + userId);
      }
      
      if (user.getId() != loggedInUser.getId()) {
         if (loggedInUser.getUserRole() != UserRole.SYS_ADMIN) {
            sLogger.warn("Logged in user " + loggedInUser.getUsername()
               + " is not admin");
            
            throw new RestException(403,
               "The logged in user is not authroized to retrieve this user information.");
         }
      }
      
      return user;
   }
   
   @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT)
   public User updateUser(@ModelAttribute("loggedInUser") User loggedInUser,
      @PathVariable int userId, @RequestBody User user) {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      User updateUser = userDao.getUser(userId);
      
      if (updateUser == null) {
         throw new RestException(404,
            "No User found in the system with user id " + userId);
      }
      
      if ((loggedInUser.getId() != userId)
         && (loggedInUser.getUserRole() != UserRole.SYS_ADMIN)) {
         throw new RestException(403,
            "The logged in user is not authroized to create/update users.");
      }
      
      return userDao.saveUser(user);
   }
   
   @RequestMapping(value = "/users/{userId}/tickets",
                   method = RequestMethod.GET)
   public List<Ticket> getUserTickets(
      @ModelAttribute("loggedInUser") User loggedInUser,
      @PathVariable int userId) {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      User user = userDao.getUser(userId);
      
      if (user == null) {
         throw new RestException(404,
            "No User found in the system with userId " + userId);
      }
      
      if ((loggedInUser.getId() != userId)
         && (loggedInUser.getUserRole() != UserRole.SYS_ADMIN)
         && (loggedInUser.getUserRole() != UserRole.SUPERVISOR)) {
         throw new RestException(403,
            "The logged in user is not authroized to get this user's tickets.");
      }
      
      Hibernate.initialize(user.getTickets());
      
      return user.getTickets();
   }
   
   @RequestMapping(value = "/technicians/{userId}/tickets",
                   method = RequestMethod.GET)
   public List<Ticket> getTechnicianTickets(
      @ModelAttribute("loggedInUser") User loggedInUser,
      @PathVariable int userId) {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      User user = userDao.getTechnician(userId);
      
      if (user == null) {
         throw new RestException(404,
            "No Technician found in the system with userId " + userId);
      }
      
      if ((loggedInUser.getId() != userId)
         && (loggedInUser.getUserRole() != UserRole.SYS_ADMIN)
         && (loggedInUser.getUserRole() != UserRole.SUPERVISOR)) {
         throw new RestException(403,
            "The logged in user is not authroized to get this technician's tickets.");
      }
      
      Hibernate.initialize(user.getAssignedTickets());
      
      return user.getAssignedTickets();
   }
}
