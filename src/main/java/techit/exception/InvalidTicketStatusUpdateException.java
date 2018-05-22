package techit.exception;

import java.util.ArrayList;
import java.util.List;

import techit.model.helper.TicketStatus;

public class InvalidTicketStatusUpdateException extends WorkOrderException {
   private static final long serialVersionUID = 1L;
   
   private static List<String>
           getValidTicketStatusList(TicketStatus currentStatus) {
      List<String> validTicketStatusList = new ArrayList<>();
      for (int i = currentStatus.getValue()
         + 1; i <= TicketStatus.CLOSED.getValue(); i++) {
         validTicketStatusList.add(TicketStatus.valueOf(i).name());
      }
      return validTicketStatusList;
   }
   
   public InvalidTicketStatusUpdateException(int ticketId,
      TicketStatus currentStatus, TicketStatus newStatus) {
      super("Ticket " + ticketId + " can not be updated from status "
         + currentStatus.name() + " to status " + newStatus.name()
         + ". Valid new status is one of "
         + getValidTicketStatusList(currentStatus));
   }
   
   public InvalidTicketStatusUpdateException(String message) {
      super(message);
   }
}
