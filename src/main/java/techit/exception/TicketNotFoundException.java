package techit.exception;

public class TicketNotFoundException extends WorkOrderException {

   private static final long serialVersionUID = 1L;

   public TicketNotFoundException(int ticketId) {
      super("Ticket with id " + ticketId + " not found in the system");
   }
}
