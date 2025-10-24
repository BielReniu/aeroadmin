package cat.uvic.teknos.dam.aeroadmin.server.exceptions;

public class NotFoundException extends HttpException {
    public NotFoundException() {
        // Cridem al nou constructor de HttpException
        super(404, "Not Found", "Resource not found");
    }

    public NotFoundException(String detailMessage) {
        // Cridem al nou constructor de HttpException
        super(404, "Not Found", detailMessage);
    }
}