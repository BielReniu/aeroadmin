package cat.uvic.teknos.dam.aeroadmin.server.exceptions;

public class HttpException extends RuntimeException {
  private final int statusCode;
  private final String statusMessage;

  public HttpException(int statusCode, String statusMessage, String detailMessage) {
    super(detailMessage);
    this.statusCode = statusCode;
    this.statusMessage = statusMessage;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getStatusMessage() {
    return statusMessage;
  }
}