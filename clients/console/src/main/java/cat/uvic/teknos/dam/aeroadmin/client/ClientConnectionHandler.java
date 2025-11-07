package cat.uvic.teknos.dam.aeroadmin.client;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Gestiona la connexió de xarxa persistent amb el servidor.
 * Aquesta classe és thread-safe i monitoritza la inactivitat del client
 * per desconnectar-se automàticament.
 */
public class ClientConnectionHandler {

    private final String host;
    private final int port;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    private final RawHttp http = new RawHttp();
    /**
     * Un Lock per assegurar que només un fil (el principal o el
     * d'inactivitat) accedeix al socket a la vegada.
     */
    private final Lock connectionLock = new ReentrantLock();

    /**
     * Emmagatzema l'últim moment d'activitat registrat (en mil·lisegons).
     * És 'volatile' per assegurar la visibilitat entre fils.
     */
    private volatile long lastActivityTime;
    private volatile boolean isConnected = false;
    private Thread inactivityMonitorThread;

    private static final long INACTIVITY_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(2);

    /**
     * Constructor.
     *
     * @param host L'amfitrió del servidor.
     * @param port El port del servidor.
     */
    public ClientConnectionHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Estableix la connexió amb el servidor i obté els streams.
     *
     * @throws IOException Si la connexió falla.
     */
    public void connect() throws IOException {
        try {
            this.socket = new Socket(host, port);
            this.outputStream = socket.getOutputStream();
            this.inputStream = socket.getInputStream();
            this.isConnected = true;
            resetInactivityTimer();
            System.out.println("Connectat al servidor a " + host + ":" + port);
        } catch (IOException e) {
            System.err.println("No s'ha pogut connectar al servidor: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Inicia el fil monitor d'inactivitat.
     */
    public void startInactivityMonitor() {
        inactivityMonitorThread = new Thread(this::monitorInactivity);
        inactivityMonitorThread.setDaemon(true);
        inactivityMonitorThread.setName("ClientInactivityMonitor");
        inactivityMonitorThread.start();
    }

    /**
     * Lògica del fil monitor. Comprova cada 10 segons si s'ha
     * superat el temps d'inactivitat (2 minuts).
     */
    private void monitorInactivity() {
        while (isConnected) {
            try {
                long inactivityDuration = System.currentTimeMillis() - lastActivityTime;
                if (inactivityDuration > INACTIVITY_TIMEOUT_MS) {
                    System.out.println("\n[INACTIVITAT] 2 minuts d'inactivitat. Desconnectant...");
                    disconnect(true);
                    break;
                }
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (IOException e) {
                System.err.println("[INACTIVITAT] Error en desconnectar: " + e.getMessage());
                break;
            }
        }
    }

    /**
     * Reinicia el comptador d'inactivitat.
     * Es crida quan l'usuari realitza una acció (menú) o
     * quan s'envia una petició.
     */
    public void resetInactivityTimer() {
        this.lastActivityTime = System.currentTimeMillis();
    }

    /**
     * Envia una petició HTTP al servidor i retorna la resposta.
     * Aquest mètode és segur per a fils i reinicia el temporitzador
     * d'inactivitat.
     *
     * @param request La petició HTTP a enviar.
     * @return La resposta del servidor.
     * @throws IOException Si no està connectat o hi ha un error de comunicació.
     */
    public RawHttpResponse<?> sendRequest(RawHttpRequest request) throws IOException {
        if (!isConnected) {
            throw new IOException("No connectat al servidor.");
        }
        resetInactivityTimer();

        connectionLock.lock();
        try {
            request.writeTo(outputStream);
            return http.parseResponse(inputStream);
        } finally {
            connectionLock.unlock();
        }
    }

    /**
     * Gestiona el procés de desconnexió (per inactivitat o manual).
     *
     * @param byInactivity Indica si la desconnexió és per inactivitat.
     * @throws IOException Si hi ha un error en comunicar-se o tancar.
     */
    public void disconnect(boolean byInactivity) throws IOException {
        if (!isConnected) return;

        connectionLock.lock();
        try {
            if (byInactivity) {
                /**
                 * Envia el missatge 'disconnect' i espera l'ACK.
                 */
                RawHttpRequest request = http.parseRequest("POST /disconnect\r\nHost: " + host + "\r\n\r\n");
                request.writeTo(outputStream);
                http.parseResponse(inputStream);
            }
            /**
             * En una desconnexió manual (Sortir del menú), no cal
             * enviar el missatge /disconnect, només tanquem el socket.
             */
        } finally {
            close();
            connectionLock.unlock();
        }
    }

    /**
     * Tanca els streams i el socket.
     */
    public void close() {
        isConnected = false;
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
            System.out.println("Connexió tancada.");
        } catch (IOException e) {
            System.err.println("Error en tancar la connexió: " + e.getMessage());
        }
        if (inactivityMonitorThread != null) {
            inactivityMonitorThread.interrupt();
        }
    }

    /**
     * Comprova si el client encara està connectat.
     *
     * @return true si està connectat, false altrament.
     */
    public boolean isConnected() {
        return isConnected;
    }
}