package cat.uvic.teknos.dam.aeroadmin.server.exceptions;

public class NotFoundException extends HttpException {
    public NotFoundException() {
        super(404, "Not Found", "Resource not found");
    }

    public NotFoundException(String detailMessage) {
        super(404, "Not Found", detailMessage);
    }
}