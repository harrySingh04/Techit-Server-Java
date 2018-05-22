package techit.model.dao;

import java.util.List;

import techit.model.Ticket;

public interface TicketDao {
   
   /**
    * getTicket
    *
    * @param id
    * @return
    */
   Ticket getTicket(int id);
   
   /**
    * saveTicket
    *
    * @param ticket
    */
   Ticket saveTicket(Ticket ticket);
   
   /**
    * getTickets
    *
    * @return
    */
   List<Ticket> getTickets();
}