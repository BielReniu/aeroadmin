package cat.uvic.teknos.dam.aeroadmin.client;

import java.util.Scanner;

public class ConsoleApp {

    public static void main(String[] args) {
        // 1. Creem les dependències
        // El port ha de coincidir amb el que has definit al teu server/App.java (8082)
        AirlineApiClient apiClient = new AirlineApiClient("http://localhost:8082");
        Scanner scanner = new Scanner(System.in);

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
        scanner.close();
    }
}