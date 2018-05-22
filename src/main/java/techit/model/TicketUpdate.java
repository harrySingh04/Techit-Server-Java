package techit.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ticket_updates")
public class TicketUpdate implements Serializable {
   private static final long serialVersionUID = 1L;
   
   @Id
   @GeneratedValue
   @Column(name = "id")
   private Integer id;
   
   @Column(name = "ticket_id", insertable = false, updatable = false)
   private Integer ticketId;
   
   @JsonIgnore
   @ManyToOne
   @JoinColumn(name = "ticket_id", referencedColumnName = "id")
   private Ticket ticket;
   
   @Column(name = "modifier_user_id", insertable = false, updatable = false)
   private Integer modifierUserId;
   
   @JsonIgnore
   @OneToOne
   @JoinColumn(name = "modifier_user_id", referencedColumnName = "id")
   private User modifier;
   
   @Column(name = "update_detail", length = 4000)
   private String updateDetail;
   
   @Temporal(TemporalType.TIME)
   @Column(name = "modified_date")
   private Date modifiedDate;
   
   public TicketUpdate() {
      
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
    * Returns the value of ticketId
    *
    * @return the ticketId
    */
   public Integer getTicketId() {
      return ticketId;
   }
   
   /**
    * Sets the value of ticketId
    *
    * @param ticketId
    *           the ticketId to set
    */
   public void setTicketId(Integer ticketId) {
      this.ticketId = ticketId;
   }
   
   /**
    * Returns the value of ticket
    *
    * @return the ticket
    */
   public Ticket getTicket() {
      return ticket;
   }
   
   /**
    * Sets the value of ticket
    *
    * @param ticket
    *           the ticket to set
    */
   public void setTicket(Ticket ticket) {
      this.ticket = ticket;
   }
   
   /**
    * Returns the value of modifier
    *
    * @return the modifier
    */
   public User getModifier() {
      return modifier;
   }
   
   /**
    * Sets the value of modifier
    *
    * @param modifier
    *           the modifier to set
    */
   public void setModifier(User modifier) {
      this.modifier = modifier;
   }
   
   /**
    * Returns the value of updateDetail
    *
    * @return the updateDetail
    */
   public String getUpdateDetail() {
      return updateDetail;
   }
   
   /**
    * Sets the value of updateDetail
    *
    * @param updateDetail
    *           the updateDetail to set
    */
   public void setUpdateDetail(String updateDetail) {
      this.updateDetail = updateDetail;
   }
   
   /**
    * Returns the value of modifiedDate
    *
    * @return the modifiedDate
    */
   public Date getModifiedDate() {
      return modifiedDate;
   }
   
   /**
    * Sets the value of modifiedDate
    *
    * @param modifiedDate
    *           the modifiedDate to set
    */
   public void setModifiedDate(Date modifiedDate) {
      this.modifiedDate = modifiedDate;
   }
   
   /**
    * getModifierUserId - Returns the value of modifieUserId
    *
    * @return the modifieUserId
    */
   public Integer getModifierUserId() {
      return modifierUserId;
   }
   
   /**
    * setModifierUserId - Sets the value of modifieUserId
    *
    * @param modifierUserId
    *           the modifierUserId to set
    */
   public void setModifierUserId(Integer modifierUserId) {
      this.modifierUserId = modifierUserId;
   }
}