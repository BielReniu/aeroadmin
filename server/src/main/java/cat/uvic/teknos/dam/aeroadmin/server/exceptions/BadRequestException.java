package cat.uvic.teknos.dam.aeroadmin.server.exceptions;

public class BadRequestException extends HttpException {
    public BadRequestException() {
        // Cridem al nou constructor de HttpException
        super(400, "Bad Request", "Bad Request");
    }

    public BadRequestException(String detailMessage) {
        // Cridem al nou constructor de HttpException
        super(400, "Bad Request", detailMessage);
    }
}