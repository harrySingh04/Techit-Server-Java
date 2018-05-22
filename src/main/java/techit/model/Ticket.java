package techit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

import techit.model.helper.TicketPriority;
import techit.model.helper.TicketStatus;

@Entity
@Table(name = "tickets")
public class Ticket implements Serializable {
   private static final long serialVersionUID = 1L;
   
   @Id
   @GeneratedValue
   @Column(name = "id", nullable = false, unique = true)
   private Integer id;
   
   @JsonIgnore
   @ManyToOne
   @JoinColumn(name = "requester_id", referencedColumnName = "id",
               nullable = false)
   private User requester;
   
   @JsonIgnore
   @ManyToMany
   @JoinTable(name = "tickets_technicians",
              joinColumns = @JoinColumn(name = "ticket_id",
                                        referencedColumnName = "id"),
              inverseJoinColumns = @JoinColumn(name = "user_id",
                                               referencedColumnName = "id"))
   @Where(clause = "user_role_id = 2")
   private List<User> technicians = new ArrayList<>();
   
   @JsonIgnore
   @ManyToMany
   @JoinTable(name = "tickets_technicians",
              joinColumns = @JoinColumn(name = "ticket_id",
                                        referencedColumnName = "id"),
              inverseJoinColumns = @JoinColumn(name = "user_id",
                                               referencedColumnName = "id"))
   @Where(clause = "user_role_id = 1")
   private List<User> supervisors = new ArrayList<>();
   
   @Column(name = "current_progress")
   private Integer currentStatus = TicketStatus.OPEN.getValue();
   
   @Column(name = "current_priority")
   private Integer currentPriority = TicketPriority.NA.getValue();
   
   @Column(name = "phone")
   private String phone;
   
   @Column(name = "email")
   private String email;
   
   @Column(name = "department")
   private String department;
   
   @JsonIgnore
   @ManyToOne
   @JoinColumn(name = "unit_id", referencedColumnName = "id")
   private Unit unit;
   
   @Column(name = "subject")
   private String subject;
   
   @Column(name = "details", length = 4000)
   private String details;
   
   @Temporal(TemporalType.TIME)
   @Column(name = "start_date")
   private Date startDate;
   
   @Temporal(TemporalType.TIME)
   @Column(name = "creation_date")
   private Date creationDate;
   
   @Temporal(TemporalType.TIME)
   @Column(name = "end_date")
   private Date endDate;
   
   @Temporal(TemporalType.TIME)
   @Column(name = "last_updated_date")
   private Date lastUpdatedDate;
   
   @Column(name = "location")
   private String ticketLocation;
   
   @JsonIgnore
   @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
   private List<TicketUpdate> updates = new ArrayList<>();
   
   @Column(name = "completion_details")
   private String completionDetails;
   
   public Ticket() {
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
    * Returns the value of requester
    *
    * @return the requester
    */
   public User getRequester() {
      return requester;
   }
   
   /**
    * Sets the value of requester
    *
    * @param requester
    *           the requester to set
    */
   public void setRequester(User requester) {
      this.requester = requester;
   }
   
   /**
    * Returns the value of users
    *
    * @return the users
    */
   public List<User> getTechnicians() {
      return technicians;
   }
   
   /**
    * Sets the value of users
    *
    * @param users
    *           the users to set
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
         this.technicians.add(technician);
      }
   }
   
   /**
    * Returns the value of supervisors
    *
    * @return the supervisors
    */
   public List<User> getSupervisors() {
      return supervisors;
   }
   
   /**
    * Sets the value of supervisors
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
      if (supervisor == null) {
         supervisors.add(supervisor);
      }
   }
   
   /**
    * Returns the value of currentStatus
    *
    * @return the currentStatus
    */
   public TicketStatus getCurrentStatus() {
      return currentStatus != null ? TicketStatus.valueOf(currentStatus) : null;
   }
   
   /**
    * Sets the value of currentStatus
    *
    * @param currentStatus
    *           the currentStatus to set
    */
   public void setCurrentStatus(TicketStatus currentStatus) {
      if (currentStatus != null) {
         this.currentStatus = currentStatus.getValue();
      }
   }
   
   /**
    * Returns the value of currentPriority
    *
    * @return the currentPriority
    */
   public TicketPriority getCurrentPriority() {
      return currentPriority != null ? TicketPriority.valueOf(currentPriority)
         : null;
   }
   
   /**
    * Sets the value of currentPriority
    *
    * @param currentPriority
    *           the currentPriority to set
    */
   public void setCurrentPriority(TicketPriority currentPriority) {
      if (currentPriority != null) {
         this.currentPriority = currentPriority.getValue();
      }
   }
   
   /**
    * Returns the value of phone
    *
    * @return the phone
    */
   public String getPhone() {
      return phone;
   }
   
   /**
    * Sets the value of phone
    *
    * @param phone
    *           the phone to set
    */
   public void setPhone(String phone) {
      this.phone = phone;
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
    * Returns the value of subject
    *
    * @return the subject
    */
   public String getSubject() {
      return subject;
   }
   
   /**
    * Sets the value of subject
    *
    * @param subject
    *           the subject to set
    */
   public void setSubject(String subject) {
      this.subject = subject;
   }
   
   /**
    * Returns the value of details
    *
    * @return the details
    */
   public String getDetails() {
      return details;
   }
   
   /**
    * Sets the value of details
    *
    * @param details
    *           the details to set
    */
   public void setDetails(String detail) {
      this.details = detail;
   }
   
   /**
    * Returns the value of startDate
    *
    * @return the startDate
    */
   public Date getStartDate() {
      return startDate;
   }
   
   /**
    * Sets the value of startDate
    *
    * @param startDate
    *           the startDate to set
    */
   public void setStartDate(Date startDate) {
      this.startDate = startDate;
   }
   
   /**
    * Returns the value of creationDate
    *
    * @return the creationDate
    */
   public Date getCreationDate() {
      return creationDate;
   }
   
   /**
    * Sets the value of creationDate
    *
    * @param creationDate
    *           the creationDate to set
    */
   public void setCreationDate(Date creationDate) {
      this.creationDate = creationDate;
   }
   
   /**
    * Returns the value of endDate
    *
    * @return the endDate
    */
   public Date getEndDate() {
      return endDate;
   }
   
   /**
    * Sets the value of endDate
    *
    * @param endDate
    *           the endDate to set
    */
   public void setEndDate(Date endDate) {
      this.endDate = endDate;
   }
   
   /**
    * Returns the value of lastUpdatedDate
    *
    * @return the lastUpdatedDate
    */
   public Date getLastUpdatedDate() {
      return lastUpdatedDate;
   }
   
   /**
    * Sets the value of lastUpdatedDate
    *
    * @param lastUpdatedDate
    *           the lastUpdatedDate to set
    */
   public void setLastUpdatedDate(Date lastUpdatedDate) {
      this.lastUpdatedDate = lastUpdatedDate;
   }
   
   /**
    * Returns the value of ticketLocation
    *
    * @return the ticketLocation
    */
   public String getTicketLocation() {
      return ticketLocation;
   }
   
   /**
    * Sets the value of ticketLocation
    *
    * @param ticketLocation
    *           the ticketLocation to set
    */
   public void setTicketLocation(String ticketLocation) {
      this.ticketLocation = ticketLocation;
   }
   
   /**
    * Returns the value of updates
    *
    * @return the updates
    */
   public List<TicketUpdate> getUpdates() {
      return updates;
   }
   
   /**
    * add the ticketUpdate to updates
    *
    * @param ticketUpdate
    *           the ticketUpdate to add
    */
   public void addUpdate(TicketUpdate ticketUpdate) {
      if (ticketUpdate != null) {
         updates.add(ticketUpdate);
      }
   }
   
   /**
    * Sets the value of updates
    *
    * @param updates
    *           the updates to set
    */
   public void setUpdates(List<TicketUpdate> updates) {
      this.updates.clear();
      if (updates != null) {
         this.updates.addAll(updates);
      }
   }
   
   /**
    * Returns the value of completionDetails
    *
    * @return the completionDetails
    */
   public String getCompletionDetails() {
      return completionDetails;
   }
   
   /**
    * Sets the value of completionDetails
    *
    * @param completionDetails
    *           the completionDetails to set
    */
   public void setCompletionDetails(String completionDetail) {
      this.completionDetails = completionDetail;
   }
}