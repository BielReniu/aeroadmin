package cat.uvic.teknos.dam.aeroadmin.server;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.net.Socket;
// AFEGIT: Import per al comptador
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final RequestRouter router;
    private final RawHttp rawHttp = new RawHttp();

    // AFEGIT: Camp per al comptador
    private final AtomicInteger connectedClients;

    // MODIFICAT: El constructor ara rep el comptador
    public ClientHandler(Socket clientSocket, RequestRouter router, AtomicInteger connectedClients) {
        this.clientSocket = clientSocket;
        this.router = router;
        this.connectedClients = connectedClients;
    }

    @Override
    public void run() {
        // AFEGIT: Incrementa el comptador quan el fil comença
        connectedClients.incrementAndGet();

        // MODIFICAT: Canviem 'try-with-resources' per 'try-finally'
        // per mantenir el socket obert.
        try {
            // AFEGIT: Bucle per mantenir la connexió oberta
            while (true) {
                // 1. Llegim la petició HTTP del client (es bloqueja aquí fins a rebre dades)
                RawHttpRequest request = rawHttp.parseRequest(clientSocket.getInputStream());

                // AFEGIT: Gestió de la desconnexió (Req 3)
                if (request.getUri().getPath().equals("/disconnect")) {
                    System.out.println("Client " + clientSocket.getInetAddress() + " ha sol·licitat desconnexió.");

                    // Enviar Acknowledgement (ACK)
                    RawHttpResponse<?> ack = rawHttp.parseResponse("HTTP/1.1 200 OK\r\nServer: AeroAdmin-ACK\r\nConnection: close\r\n\r\n");
                    ack.writeTo(clientSocket.getOutputStream());

                    break; // Sortir del bucle 'while' per tancar la connexió
                }

                // 2. Passem la petició al router perquè la processi i ens retorni una resposta
                RawHttpResponse<?> response = router.route(request);

                // 3. Enviem la resposta HTTP al client
                response.writeTo(clientSocket.getOutputStream());

                // NO tanquem el socket, tornem a l'inici del bucle
            }

        } catch (IOException e) {
            // Això saltarà si el client es desconnecta de cop (p.ex. tanca la consola o hi ha un error)
            System.err.println("Client desconnectat (possiblement per inactivitat o error): " + clientSocket.getInetAddress());
        } finally {
            // AFEGIT: En qualsevol cas (sortida normal o error), decrementem el comptador i tanquem el socket.
            connectedClients.decrementAndGet();
            try {
                if (!clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Error en tancar el socket: " + e.getMessage());
            }
        }
    }
}