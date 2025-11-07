package cat.uvic.teknos.dam.aeroadmin.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
// AFEGIT: Import per al comptador thread-safe
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    private final int port;
    private final RequestRouter router;
    private final ExecutorService threadPool;

    // AFEGIT: Comptador de clients connectats (Req 2)
    private final AtomicInteger connectedClients = new AtomicInteger(0);

    public Server(int port, RequestRouter router) {
        this.port = port;
        this.router = router;
        // Creem una "piscina" de fils per gestionar les connexions de manera eficient
        this.threadPool = Executors.newCachedThreadPool();
    }

    public void start() {
        // AFEGIT: Engeguem el fil dimoni comptador (Req 2)
        startConnectionCounterThread();

        try (var serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciat i escoltant al port " + port);

            // Bucle infinit per acceptar clients contínuament
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connectat des de " + clientSocket.getInetAddress());

                // MODIFICAT: Passem el comptador al ClientHandler
                // Per a cada client, creem un ClientHandler i l'executem en un fil separat
                threadPool.submit(new ClientHandler(clientSocket, router, connectedClients));
            }
        } catch (IOException e) {
            System.err.println("Error en iniciar el servidor: " + e.getMessage());
            // En una aplicació real, aquí hauríem de gestionar l'error de manera més robusta
        }
    }

    // AFEGIT: Nou mètode per al fil dimoni (Req 2)
    private void startConnectionCounterThread() {
        Thread daemon = new Thread(() -> {
            // Imprimeix el número de clients connectats cada minut
            while (true) {
                try {
                    // L'enunciat demana "every minute" (60_000 mil·lisegons)
                    Thread.sleep(60_000);
                    System.out.println("[DAEMON] Clients actualment connectats: " + connectedClients.get());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        // L'enunciat demana explícitament un "daemon thread"
        daemon.setDaemon(true);
        daemon.start();
    }
}