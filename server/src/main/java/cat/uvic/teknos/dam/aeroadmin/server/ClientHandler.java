package cat.uvic.teknos.dam.aeroadmin.server;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final RequestRouter router;
    private final RawHttp rawHttp = new RawHttp();

    public ClientHandler(Socket clientSocket, RequestRouter router) {
        this.clientSocket = clientSocket;
        this.router = router;
    }

    @Override
    public void run() {
        // Utilitzem try-with-resources per assegurar que el socket es tanca sempre
        try (clientSocket) {
            // 1. Llegim la petició HTTP del client
            RawHttpRequest request = rawHttp.parseRequest(clientSocket.getInputStream());

            // 2. Passem la petició al router perquè la processi i ens retorni una resposta
            RawHttpResponse<?> response = router.route(request);

            // 3. Enviem la resposta HTTP al client
            response.writeTo(clientSocket.getOutputStream());

        } catch (IOException e) {
            System.err.println("Error de comunicació amb el client: " + e.getMessage());
        }
    }
}