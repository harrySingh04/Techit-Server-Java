
package techit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import techit.model.helper.UserRole;

@Entity
@Table(name = "users")
public class User implements Serializable {
   private static final long serialVersionUID = 1L;
   
   @Id
   @GeneratedValue
   @Column(name = "id", nullable = false, unique = true)
   private Integer id;
   
   @Column(name = "first_name", nullable = false)
   private String firstName;
   
   @Column(name = "last_name", nullable = false)
   private String lastName;
   
   @Column(name = "username", nullable = false)
   private String username;
   
   @JsonProperty(access = Access.WRITE_ONLY)
   @Column(name = "password", nullable = false)
   private String password;
   
   @Column(name = "phone_number")
   private String phoneNumber;
   
   @Column(name = "department")
   private String department;
   
   @Column(name = "email")
   private String email;
   
   @Column(name = "user_role_id")
   private Integer userRoleId = UserRole.USER.getValue();
   
   @JsonIgnore
   @ManyToOne
   @JoinColumn(name = "unit_id", referencedColumnName = "id")
   private Unit unit;
   
   @JsonIgnore
   @OneToMany(mappedBy = "requester")
   private List<Ticket> tickets = new ArrayList<>();
   
   @JsonIgnore
   @OneToMany(mappedBy = "requester")
   private List<Ticket> assignedTickets = new ArrayList<>();
   
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
    * Returns the value of firstName
    *
    * @return the firstName
    */
   public String getFirstName() {
      return firstName;
   }
   
   /**
    * Sets the value of firstName
    *
    * @param firstName
    *           the firstName to set
    */
   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }
   
   /**
    * Returns the value of lastName
    *
    * @return the lastName
    */
   public String getLastName() {
      return lastName;
   }
   
   /**
    * Sets the value of lastName
    *
    * @param lastName
    *           the lastName to set
    */
   public void setLastName(String lastName) {
      this.lastName = lastName;
   }
   
   /**
    * Returns the value of username
    *
    * @return the username
    */
   public String getUsername() {
      return username;
   }
   
   /**
    * Sets the value of username
    *
    * @param username
    *           the username to set
    */
   public void setUsername(String userName) {
      this.username = userName;
   }
   
   /**
    * Returns the value of password
    *
    * @return the password
    */
   public String getPassword() {
      return password;
   }
   
   /**
    * Sets the value of password
    *
    * @param password
    *           the password to set
    */
   public void setPassword(String password) {
      this.password = password;
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
    * Returns the value of department
    *
    * @return the department
    */
   public String getDepartment() {
      return department;
   }
   
   /**
    * Sets the value of department
    *
    * @param department
    *           the department to set
    */
   public void setDepartment(String department) {
      this.department = department;
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
    * Returns the value of userRole
    *
    * @return the userRole
    */
   public UserRole getUserRole() {
      return userRoleId != null ? UserRole.valueOf(userRoleId) : null;
   }
   
   /**
    * Sets the value of userRole
    *
    * @param userRole
    *           the userRole to set
    */
   public void setUserRole(UserRole userRole) {
      if (userRole != null) {
         this.userRoleId = userRole.getValue();
      }
   }
   
   /**
    * Returns the value of unit
    *
    * @return the unit
    */
   public Unit getUnit() {
      return unit;
   }
   
   /**
    * Sets the value of unit
    *
    * @param unit
    *           the unit to set
    */
   public void setUnit(Unit unit) {
      this.unit = unit;
   }
   
   /**
    * Returns the value of tickets
    *
    * @return the tickets
    */
   public List<Ticket> getTickets() {
      return tickets;
   }
   
   /**
    * Sets the value of tickets
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
    * Returns the value of assignedTickets
    *
    * @return the assignedTickets
    */
   public List<Ticket> getAssignedTickets() {
      return assignedTickets;
   }
   
   /**
    * Sets the value of assignedTickets
    *
    * @param assignedTickets
    *           the assignedTickets to set
    */
   public void setAssignedTickets(List<Ticket> assignedTickets) {
      this.assignedTickets.clear();
      if (assignedTickets != null) {
         this.assignedTickets.addAll(assignedTickets);
      }
   }
   
   /**
    * addAssignedTickets
    *
    * @param assignedTicket
    */
   public void addAssignedTicket(Ticket assignedTicket) {
      if (assignedTicket != null) {
         assignedTickets.add(assignedTicket);
      }
   }
}
