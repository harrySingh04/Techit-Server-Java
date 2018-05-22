package techit.exception;

public class WorkOrderException extends Exception {
   private static final long serialVersionUID = 1L;

   public WorkOrderException() {
      super();
   }

   public WorkOrderException(String message, Throwable cause) {
      super(message, cause);
   }

   public WorkOrderException(String message) {
      super(message);
   }

   public WorkOrderException(Throwable cause) {
      super(cause);
   }
}
