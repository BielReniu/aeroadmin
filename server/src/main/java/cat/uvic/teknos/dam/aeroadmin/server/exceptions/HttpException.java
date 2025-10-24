package cat.uvic.teknos.dam.aeroadmin.server.exceptions;

public class HttpException extends RuntimeException {
  private final int statusCode;
  private final String statusMessage; // <-- NOU CAMP

  // Constructor modificat per acceptar el statusMessage
  public HttpException(int statusCode, String statusMessage, String detailMessage) {
    super(detailMessage); // El missatge detallat va al RuntimeException
    this.statusCode = statusCode;
    this.statusMessage = statusMessage;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getStatusMessage() { // <-- NOU GETTER
    return statusMessage;
  }
}