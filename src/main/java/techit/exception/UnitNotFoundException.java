package techit.exception;

public class UnitNotFoundException extends WorkOrderException {

   private static final long serialVersionUID = 1L;

   public UnitNotFoundException(int ticketId) {
      super("Unit with id " + ticketId + " not found in the system");
   }
}
