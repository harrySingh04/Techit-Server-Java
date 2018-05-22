package techit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "units")
public class Unit implements Serializable {
   private static final long serialVersionUID = 1L;
   
   @Id
   @GeneratedValue
   @Column(name = "id", nullable = false, unique = true)
   private Integer id;
   
   @Column(name = "unit_name", nullable = false, unique = true)
   private String unitName;
   
   @JsonIgnore
   @OneToMany(mappedBy = "unit")
   @Where(clause = "user_role_id = 2")
   private List<User> technicians = new ArrayList<>();
   
   @JsonIgnore
   @OneToMany(mappedBy = "unit")
   @Where(clause = "user_role_id = 1")
   private List<User> supervisors = new ArrayList<>();
   
   @JsonIgnore
   @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL)
   private List<Ticket> tickets = new ArrayList<>();
   
   @Column(name = "phone_number")
   private String phoneNumber;
   
   @Column(name = "location")
   private String location;
   
   @Column(name = "email")
   private String email;
   
   @Column(name = "description", length = 4000)
   private String description;
   
   public Unit() {
      
   }
   
   /**
    * Returns the value of id
    *
    * @return the id
    */
   public Integer getId() {
      return id;
   }
   
   /**
    * Sets the value of id
    *
    * @param id
    *           the id to set
    */
   public void setId(int id) {
      this.id = id;
   }
   
   /**
    * Returns the value of unitName
    *
    * @return the unitName
    */
   public String getUnitName() {
      return unitName;
   }
   
   /**
    * Sets the value of unitName
    *
    * @param unitName
    *           the unitName to set
    */
   public void setUnitName(String unitName) {
      this.unitName = unitName;
   }
   
   /**
    * Returns the value of phoneNumber
    *
    * @return the phoneNumber
    */
   public String getPhoneNumber() {
      return phoneNumber;
   }
   
   /**
    * Sets the value of phoneNumber
    *
    * @param phoneNumber
    *           the phoneNumber to set
    */
   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }
   
   /**
    * Returns the value of location
    *
    * @return the location
    */
   public String getLocation() {
      return location;
   }
   
   /**
    * Sets the value of location
    *
    * @param location
    *           the location to set
    */
   public void setLocation(String location) {
      this.location = location;
   }
   
   /**
    * Returns the value of email
    *
    * @return the email
    */
   public String getEmail() {
      return email;
   }
   
   /**
    * Sets the value of email
    *
    * @param email
    *           the email to set
    */
   public void setEmail(String email) {
      this.email = email;
   }
   
   /**
    * Returns the value of description
    *
    * @return the description
    */
   public String getDescription() {
      return description;
   }
   
   /**
    * Sets the value of description
    *
    * @param description
    *           the description to set
    */
   public void setDescription(String description) {
      this.description = description;
   }
   
   /**
    * getTechnicians - Returns the value of technicians
    *
    * @return the technicians
    */
   public List<User> getTechnicians() {
      return technicians;
   }
   
   /**
    * setTechnicians - Sets the value of technicians
    *
    * @param technicians
    *           the technicians to set
    */
   public void setTechnicians(List<User> technicians) {
      this.technicians.clear();
      if (technicians != null) {
         this.technicians.addAll(technicians);
      }
   }
   
   /**
    * addTechnician
    *
    * @param technician
    */
   public void addTechnician(User technician) {
      if (technician != null) {
         technicians.add(technician);
      }
   }
   
   /**
    * getSupervisors - Returns the value of supervisors
    *
    * @return the supervisors
    */
   public List<User> getSupervisors() {
      return supervisors;
   }
   
   /**
    * setSupervisors - Sets the value of supervisors
    *
    * @param supervisors
    *           the supervisors to set
    */
   public void setSupervisors(List<User> supervisors) {
      this.supervisors.clear();
      if (supervisors != null) {
         this.supervisors.addAll(supervisors);
      }
   }
   
   /**
    * addSupervisor
    *
    * @param supervisor
    */
   public void addSupervisor(User supervisor) {
      if (supervisor != null) {
         supervisors.add(supervisor);
      }
   }
   
   /**
    * getTickets - Returns the value of tickets
    *
    * @return the tickets
    */
   public List<Ticket> getTickets() {
      return tickets;
   }
   
   /**
    * addTicket
    *
    * @param ticket
    */
   public void addTicket(Ticket ticket) {
      if (ticket != null) {
         tickets.add(ticket);
      }
   }
   
   /**
    * setTickets - Sets the value of tickets
    *
    * @param tickets
    *           the tickets to set
    */
   public void setTickets(List<Ticket> tickets) {
      this.tickets.clear();
      if (tickets != null) {
         this.tickets.addAll(tickets);
      }
   }
}