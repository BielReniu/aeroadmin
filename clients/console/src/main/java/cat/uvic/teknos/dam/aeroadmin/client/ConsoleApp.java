package cat.uvic.teknos.dam.aeroadmin.client;

// AFEGIT: Import per a la gestió d'errors de connexió
import java.io.IOException;
import java.util.Scanner;

public class ConsoleApp {

    public static void main(String[] args) {
        // 1. Creem les dependències
        // El port ha de coincidir amb el que has definit al teu server/App.java (8082)

        // MODIFICAT: Utilitzem el nou client persistent
        PersistentAirlineApiClient apiClient = new PersistentAirlineApiClient("http://localhost:8082");
        Scanner scanner = new Scanner(System.in);

        // AFEGIT: Gestionem la connexió inicial fora del bucle
        try {
            apiClient.connect(); // Obrim la connexió UNA SOLA VEGADA
        } catch (IOException e) {
            System.err.println("❌ No s'ha pogut connectar al servidor. L'aplicació es tancarà.");
            System.err.println("Detall: " + e.getMessage());
            return; // Sortir si no es pot connectar
        }

        // 2. Creem el gestor (manager) i li injectem les dependències
        AirlineManager airlineManager = new AirlineManager(scanner, apiClient);

        boolean running = true;
        while (running) {
            System.out.println("\n===== AeroAdmin Client (Consola) =====");
            System.out.println("1. Gestionar Companyies Aèries");
            // Aquí podries afegir "2. Gestionar Pilots", etc., si implementes més controladors
            System.out.println("0. Sortir");
            System.out.print("Selecciona una opció: ");

            String choice = scanner.nextLine().trim();

            // Qualsevol interacció de l'usuari (fins i tot una opció invàlida)
            // farà que la següent crida a l'API reiniciï el timer d'inactivitat.
            // Si l'usuari no toca res, el timer del client saltarà als 2 minuts.

            switch (choice) {
                case "1":
                    airlineManager.run();
                    break;
                case "0":
                    System.out.println("Sortint...");
                    running = false;
                    break;
                default:
                    System.out.println("Opció invàlida.");
            }
        }

        // AFEGIT: En sortir del bucle, tanquem la connexió correctament (Req 3)
        try {
            apiClient.disconnect();
        } catch (IOException e) {
            System.err.println("Error en desconnectar: " + e.getMessage());
        }

        scanner.close();
    }
}