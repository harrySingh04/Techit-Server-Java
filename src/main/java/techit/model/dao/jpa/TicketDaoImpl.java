package techit.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import techit.model.Ticket;
import techit.model.dao.TicketDao;

@Repository
@Transactional
public class TicketDaoImpl implements TicketDao {

   @PersistenceContext
   private EntityManager entityManager;

   /**
    * getTicket
    *
    * @param id
    * @return
    */
   @Override
   public Ticket getTicket(int id) {
      try {
         Ticket ticket = entityManager.find(Ticket.class, id);

         return ticket;
      }
      catch (NoResultException e) {
         return null;
      }
   }

   /**
    * getTickets
    *
    * @return
    */
   @Override
   public List<Ticket> getTickets() {
      try {
         List<Ticket> tickets = entityManager.createQuery(
            "from Ticket order by id", Ticket.class).getResultList();

         return tickets;
      }
      catch (NoResultException e) {
         return null;
      }
   }

   /**
    * createTicket
    *
    * @param ticket
    */
   @Override
   public Ticket saveTicket(Ticket ticket) {
      return entityManager.merge(ticket);
   }
}
