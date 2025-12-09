package cat.uvic.teknos.dam.aeroadmin.client;

import java.io.IOException;
import java.util.Scanner;

public class ConsoleApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("✈️ BENVINGUT AL SISTEMA AEROADMIN ✈️");

        String serverUrl = "http://localhost:8082";

        System.out.print("Identitat del Client (Enter per defecte 'client1'): ");
        String idInput = scanner.nextLine().trim();
        String clientId = idInput.isEmpty() ? "client1" : idInput;

        System.out.println("Connecting as: " + clientId + " to " + serverUrl + " ...");

        PersistentAirlineApiClient apiClient = new PersistentAirlineApiClient(serverUrl, clientId);

        try {
            apiClient.connect();
        } catch (IOException e) {
            System.err.println("❌ No s'ha pogut connectar al servidor. L'aplicació es tancarà.");
            System.err.println("Detall: " + e.getMessage());
            return;
        }

        AirlineManager airlineManager = new AirlineManager(scanner, apiClient);

        boolean running = true;
        while (running) {
            System.out.println("\n===== AeroAdmin Client (" + clientId + ") =====");
            System.out.println("1. Gestionar Companyies Aèries");
            System.out.println("0. Sortir");
            System.out.print("Selecciona una opció: ");

            String choice = scanner.nextLine().trim();

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

        try {
            apiClient.disconnect();
        } catch (IOException e) {
            System.err.println("Error en desconnectar: " + e.getMessage());
        }

        scanner.close();
    }
}