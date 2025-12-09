package cat.uvic.teknos.dam.aeroadmin.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    private final int port;
    private final RequestRouter router;
    private final ExecutorService threadPool;

    private final AtomicInteger connectedClients = new AtomicInteger(0);

    public Server(int port, RequestRouter router) {
        this.port = port;
        this.router = router;
        this.threadPool = Executors.newCachedThreadPool();
    }

    public void start() {
        startConnectionCounterThread();

        try (var serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciat i escoltant al port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connectat des de " + clientSocket.getInetAddress());

                threadPool.submit(new ClientHandler(clientSocket, router, connectedClients));
            }
        } catch (IOException e) {
            System.err.println("Error en iniciar el servidor: " + e.getMessage());
        }
    }

    private void startConnectionCounterThread() {
        Thread daemon = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60_000);
                    System.out.println("[DAEMON] Clients actualment connectats: " + connectedClients.get());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        daemon.setDaemon(true);
        daemon.start();
    }
}