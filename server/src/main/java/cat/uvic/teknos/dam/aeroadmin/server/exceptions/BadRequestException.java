package cat.uvic.teknos.dam.aeroadmin.server.exceptions;

public class BadRequestException extends HttpException {
    public BadRequestException() {
        super(400, "Bad Request", "Bad Request");
    }

    public BadRequestException(String detailMessage) {
        super(400, "Bad Request", detailMessage);
    }
}