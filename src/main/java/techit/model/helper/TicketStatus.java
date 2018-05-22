package techit.model.helper;

public enum TicketStatus {
   OPEN(0), INPROGRESS(1), ONHOLD(2), COMPLETED(3), CLOSED(4);

   private int statusId;

   private TicketStatus(int statusId) {
      this.statusId = statusId;
   }

   public int getValue() {
      return statusId;
   }

   public static TicketStatus valueOf(int statusId) {
      for (TicketStatus ticketStatus : values()) {
         if (ticketStatus.getValue() == statusId) {
            return ticketStatus;
         }
      }
      throw new IllegalArgumentException(
         "Invalid ticket status id " + statusId);
   }
}