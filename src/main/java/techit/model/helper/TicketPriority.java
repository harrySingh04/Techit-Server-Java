package techit.model.helper;

public enum TicketPriority {
   NA(0), LOW(1), MEDIUM(2), HIGH(3);

   private int priorityId;

   private TicketPriority(int priorityId) {
      this.priorityId = priorityId;
   }

   public int getValue() {
      return this.priorityId;
   }

   public static TicketPriority valueOf(int priorityId) {
      for (TicketPriority ticketPriority : values()) {
         if (ticketPriority.getValue() == priorityId) {
            return ticketPriority;
         }
      }
      throw new IllegalArgumentException(
         "Invalid ticket priority id " + priorityId);
   }
}