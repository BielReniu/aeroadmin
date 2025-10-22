package cat.uvic.teknos.dam.aeroadmin.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int port;
    private final RequestRouter router;
    private final ExecutorService threadPool;

    public Server(int port, RequestRouter router) {
        this.port = port;
        this.router = router;
        // Creem una "piscina" de fils per gestionar les connexions de manera eficient
        this.threadPool = Executors.newCachedThreadPool();
    }

    public void start() {
        try (var serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciat i escoltant al port " + port);

            // Bucle infinit per acceptar clients contínuament
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connectat des de " + clientSocket.getInetAddress());

                // Per a cada client, creem un ClientHandler i l'executem en un fil separat
                threadPool.submit(new ClientHandler(clientSocket, router));
            }
        } catch (IOException e) {
            System.err.println("Error en iniciar el servidor: " + e.getMessage());
            // En una aplicació real, aquí hauríem de gestionar l'error de manera més robusta
        }
    }
}