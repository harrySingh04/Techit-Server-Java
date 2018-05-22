package techit.rest.controller;

import java.util.List;

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
import techit.model.Unit;
import techit.model.User;
import techit.model.dao.UnitDao;
import techit.model.helper.UserRole;
import techit.rest.error.RestException;

@RestController
@Transactional
public class UnitController {
   
   @Autowired
   private UnitDao unitDao;
   
   @RequestMapping(value = "/units", method = RequestMethod.GET)
   public List<Unit> getAllUnits(
      @ModelAttribute("loggedInUser") User loggedInUser) throws Exception {
      try {
         if (loggedInUser == null) {
            throw new RestException(401, "User not logged in.");
         }
         
         return unitDao.getUnits();
      }
      catch (Exception e) {
         e.printStackTrace();
         throw e;
      }
   }
   
   @RequestMapping(value = "/units", method = RequestMethod.POST)
   public Unit createUnit(@ModelAttribute("loggedInUser") User loggedInUser,
      @RequestBody Unit unit) {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      if (unit.getUnitName() == null) {
         throw new RestException(400, "Missing unit name.");
      }
      
      if (loggedInUser.getUserRole() != UserRole.SYS_ADMIN) {
         throw new RestException(403,
            "The logged in user is not authroized to create/update units.");
      }
      
      return unitDao.saveUnit(unit);
   }
   
   @RequestMapping(value = "/units/{unitId}/technicians",
                   method = RequestMethod.GET)
   public List<User> getUnitTechnicians(
      @ModelAttribute("loggedInUser") User loggedInUser,
      @PathVariable int unitId) throws Exception {
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      Unit existingUnit = unitDao.getUnit(unitId);
      
      if (existingUnit == null) {
         throw new RestException(404,
            "No Unit found in the system with id " + unitId);
      }
      
      Hibernate.initialize(existingUnit.getTechnicians());
      
      return existingUnit.getTechnicians();
   }
   
   @RequestMapping(value = "/units/{unitId}/tickets",
                   method = RequestMethod.GET)
   public List<Ticket> getUnitTickets(
      @ModelAttribute("loggedInUser") User loggedInUser,
      @PathVariable int unitId) throws Exception {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      Unit existingUnit = unitDao.getUnit(unitId);
      
      if (existingUnit == null) {
         throw new RestException(404,
            "No Unit found in the system with id " + unitId);
      }
      
      Hibernate.initialize(existingUnit.getTickets());
      
      return existingUnit.getTickets();
   }
   
   // ===================================================================
   // ================== Other APIs =====================================
   // ===================================================================
   
   @RequestMapping(value = "/units/{id}", method = RequestMethod.GET)
   public Unit getUnit(@ModelAttribute("loggedInUser") User loggedInUser,
      @PathVariable int id) {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      Unit unit = unitDao.getUnit(id);
      
      if (unit == null) {
         throw new RestException(404,
            "No Unit found in the system with id " + id);
      }
      
      return unit;
   }
   
   @RequestMapping(value = "/units", method = RequestMethod.PUT)
   public Unit updateUnit(@ModelAttribute("loggedInUser") User loggedInUser,
      @RequestBody Unit unit) {
      
      if (loggedInUser == null) {
         throw new RestException(401, "User not logged in.");
      }
      
      if (unit.getId() == null) {
         throw new RestException(400,
            "Missing unit id in the update Unit API call");
      }
      
      Unit existingUnit = unitDao.getUnit(unit.getId());
      
      if (existingUnit == null) {
         throw new RestException(404,
            "No Unit found in the system with id " + unit.getId());
      }
      
      if (loggedInUser.getUserRole() != UserRole.SYS_ADMIN) {
         throw new RestException(403,
            "The logged in user is not authroized to create/update units.");
      }
      
      return unitDao.saveUnit(unit);
   }
   
   @RequestMapping(value = "/units/{unitId}/supervisors",
                   method = RequestMethod.GET)
   public List<User> getUnitSupervisors(
      @ModelAttribute("loggedInUser") User loggedInUser,
      @PathVariable int unitId) throws Exception {
      try {
         if (loggedInUser == null) {
            throw new RestException(401, "User not logged in.");
         }
         
         Unit existingUnit = unitDao.getUnit(unitId);
         
         if (existingUnit == null) {
            throw new RestException(404,
               "No Unit found in the system with id " + unitId);
         }
         
         Hibernate.initialize(existingUnit.getSupervisors());
         
         return existingUnit.getSupervisors();
      }
      catch (Exception e) {
         e.printStackTrace();
         throw e;
      }
   }
}
