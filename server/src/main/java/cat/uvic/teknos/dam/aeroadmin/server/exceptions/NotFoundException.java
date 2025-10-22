package cat.uvic.teknos.dam.aeroadmin.server.exceptions;

public class NotFoundException extends HttpException {
    public NotFoundException() {
        super(404, "Resource not found");
    }

    public NotFoundException(String message) {
        super(404, message);
    }
}