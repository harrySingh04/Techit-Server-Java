package techit.rest.controller;

import java.util.Date;
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
import techit.model.TicketUpdate;
import techit.model.User;
import techit.model.dao.TicketDao;
import techit.model.dao.UserDao;
import techit.model.helper.TicketPriority;
import techit.model.helper.TicketStatus;
import techit.model.helper.UserRole;
import techit.rest.error.RestException;

@RestController
@Transactional
public class TicketController {
   private static final Log sLog = LogFactory.getLog(TicketController.class);
   
   @Autowired
   private TicketDao ticketDao;
   @Autowired
   private UserDao userDao;
   
   @RequestMapping(value = "/tickets", method = RequestMethod.GET)
   public List<Ticket> getAllTickets(
      @ModelAttribute("loggedInUser") User loggedInUser) throws Exception {
      try {
         if (loggedInUser == null) {
            throw new RestException(401, "User not logged in.");
         }
         
         return ticketDao.getTickets();
      }
      catch (Exception e) {
         throw e;
      }
   }
   
   @RequestMapping(value = "/tickets", method = RequestMethod.POST)
   public Ticket createTicket(@ModelAttribute("loggedInUser") User loggedInUser,
      @RequestBody Ticket ticket) {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      if (ticket.getDetails() == null || ticket.getDetails().isEmpty()) {
         throw new RestException(400, "Ticket details can not be empty.");
      }
      
      ticket.setRequester(loggedInUser);
      ticket.setCreationDate(new Date());
      ticket.setLastUpdatedDate(new Date());
      
      Ticket savedTicket = ticketDao.saveTicket(ticket);
      
      sLog.debug("New Ticket with id " + savedTicket.getId()
         + " created by user " + loggedInUser.getUsername());
      
      return savedTicket;
   }
   
   @RequestMapping(value = "/tickets/{id}", method = RequestMethod.GET)
   public Ticket getTicketById(
      @ModelAttribute("loggedInUser") User loggedInUser, @PathVariable int id) {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      Ticket ticket = ticketDao.getTicket(id);
      
      if (ticket == null) {
         throw new RestException(404,
            "No Ticket found in the system with id " + id);
      }
      
      return ticket;
   }
   
   @RequestMapping(value = "/tickets/{ticketId}", method = RequestMethod.PUT)
   public Ticket updateTicket(@ModelAttribute("loggedInUser") User loggedInUser,
      @RequestBody Ticket ticket, @PathVariable int ticketId) {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      Ticket existingTicket = ticketDao.getTicket(ticketId);
      
      if (existingTicket == null) {
         throw new RestException(404,
            "No Ticket found in the system with id " + ticketId);
      }
      
      ticket.setId(ticketId);
      
      return ticketDao.saveTicket(ticket);
   }
   
   @RequestMapping(value = "/tickets/{ticketId}/technicians",
                   method = RequestMethod.GET)
   public List<User> getTicketTechnicians(
      @ModelAttribute("loggedInUser") User loggedInUser,
      @PathVariable int ticketId) throws Exception {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      Ticket existingTicket = ticketDao.getTicket(ticketId);
      
      if (existingTicket == null) {
         throw new RestException(404,
            "No Ticket found in the system with id " + ticketId);
      }
      
      Hibernate.initialize(existingTicket.getTechnicians());
      
      return existingTicket.getTechnicians();
   }
   
   @RequestMapping(value = "/tickets/{ticketId}/technicians/{userId}",
                   method = RequestMethod.PUT)
   public void assignTechnicianToTicket(
      @ModelAttribute("loggedInUser") User loggedInUser,
      @PathVariable int ticketId, @PathVariable int userId) throws Exception {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      Ticket existingTicket = ticketDao.getTicket(ticketId);
      
      if (existingTicket == null) {
         throw new RestException(404,
            "No Ticket found in the system with id " + ticketId);
      }
      
      User technician = userDao.getTechnician(userId);
      
      if (technician == null) {
         throw new RestException(404,
            "No Technician found in the system with userId " + userId);
      }
      
      if ((loggedInUser.getId() != userId)
         && (loggedInUser.getUserRole() != UserRole.SUPERVISOR)) {
         throw new RestException(403,
            "The logged in user is can not assign tickets to this technician.");
      }
      
      boolean alreadyAssigned = false;
      List<User> existingTechs = existingTicket.getTechnicians();
      if (existingTechs != null) {
         for (User existingTech : existingTechs) {
            if (existingTech.getId() == userId) {
               alreadyAssigned = true;
               break;
            }
         }
      }
      
      if (!alreadyAssigned) {
         existingTicket.addTechnician(technician);
         ticketDao.saveTicket(existingTicket);
      }
      else {
         throw new RestException(400, "Technician with userId " + userId
            + " is already assigned to ticket with id " + ticketId);
      }
   }
   
   @RequestMapping(value = "/tickets/{ticketId}/status/{status}",
                   method = RequestMethod.PUT)
   public void updateTicketStatus(
      @ModelAttribute("loggedInUser") User loggedInUser,
      @PathVariable int ticketId, @PathVariable String status)
      throws Exception {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      if (status == null) {
         throw new RestException(400, "Ticket status can not be null.");
      }
      
      Ticket existingTicket = ticketDao.getTicket(ticketId);
      
      if (existingTicket == null) {
         throw new RestException(404,
            "No Ticket found in the system with id " + ticketId);
      }
      
      boolean isAssignedTechnician = false;
      List<User> technicians = existingTicket.getTechnicians();
      for (User technician : technicians) {
         if (technician.getId() == loggedInUser.getId()) {
            isAssignedTechnician = true;
            break;
         }
      }
      
      if ((loggedInUser.getId() != existingTicket.getRequester().getId())
         && (!isAssignedTechnician)
         && (loggedInUser.getUserRole() != UserRole.SUPERVISOR)) {
         throw new RestException(403,
            "The logged in user is not authroized to update this ticket.");
      }
      
      try {
         existingTicket.setCurrentStatus(TicketStatus.valueOf(status));
      }
      catch (IllegalArgumentException e) {
         throw new RestException(400, "Invalid ticket status " + status);
      }
      
      ticketDao.saveTicket(existingTicket);
   }
   
   @RequestMapping(value = "/tickets/{ticketId}/priority/{priority}",
                   method = RequestMethod.PUT)
   public void updateTicketPriority(
      @ModelAttribute("loggedInUser") User loggedInUser,
      @PathVariable int ticketId, @PathVariable String priority)
      throws Exception {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      if (priority == null) {
         throw new RestException(400, "Ticket priority can not be null.");
      }
      
      Ticket existingTicket = ticketDao.getTicket(ticketId);
      
      if (existingTicket == null) {
         throw new RestException(404,
            "No Ticket found in the system with id " + ticketId);
      }
      
      boolean isAssignedTechnician = false;
      List<User> technicians = existingTicket.getTechnicians();
      for (User technician : technicians) {
         if (technician.getId() == loggedInUser.getId()) {
            isAssignedTechnician = true;
            break;
         }
      }
      
      if ((loggedInUser.getId() != existingTicket.getRequester().getId())
         && (!isAssignedTechnician)
         && (loggedInUser.getUserRole() != UserRole.SUPERVISOR)) {
         throw new RestException(403,
            "The logged in user is not authroized to update this ticket.");
      }
      
      try {
         existingTicket.setCurrentPriority(TicketPriority.valueOf(priority));
      }
      catch (IllegalArgumentException e) {
         throw new RestException(400, "Invalid ticket priority " + priority);
      }
      
      ticketDao.saveTicket(existingTicket);
   }
   
   @RequestMapping(value = "/tickets/{ticketId}/updates",
                   method = RequestMethod.POST)
   public void createTicketUpdate(
      @ModelAttribute("loggedInUser") User loggedInUser,
      @PathVariable int ticketId, @RequestBody TicketUpdate ticketUpdate) {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      Ticket ticket = ticketDao.getTicket(ticketId);
      
      if (ticket == null) {
         throw new RestException(404,
            "No Ticket found in the system with id " + ticketId);
      }
      
      ticketUpdate.setTicket(ticket);
      ticketUpdate.setModifier(loggedInUser);
      ticketUpdate.setModifiedDate(new Date());
      ticket.getUpdates().add(ticketUpdate);
      
      ticketDao.saveTicket(ticket);
   }
   
   // ========== Other APIs ========================
   @RequestMapping(value = "/tickets/{ticketId}/updates",
                   method = RequestMethod.GET)
   public List<TicketUpdate> getTicketUpdates(
      @ModelAttribute("loggedInUser") User loggedInUser,
      @PathVariable int ticketId) {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      Ticket ticket = ticketDao.getTicket(ticketId);
      
      if (ticket == null) {
         throw new RestException(404,
            "No Ticket found in the system with id " + ticketId);
      }
      
      Hibernate.initialize(ticket.getUpdates());
      
      return ticket.getUpdates();
   }
}
