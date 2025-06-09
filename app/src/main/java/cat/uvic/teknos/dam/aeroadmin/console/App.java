package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.model.ModelFactory;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        try {
            // 1) Carregar di.properties
            Properties props = new Properties();
            try (InputStream in = App.class.getResourceAsStream("/di.properties")) {
                if (in == null) {
                    throw new RuntimeException("No s’ha trobat di.properties");
                }
                props.load(in);
            }

            // 2) Demanar estratègia a l'usuari
            Scanner scanner = new Scanner(System.in);
            System.out.println("Selecciona l'estratègia de persistència:");
            System.out.println("  1) JDBC");
            System.out.println("  2) JPA");
            System.out.print("Tria 1 o 2: ");
            String mode = scanner.nextLine().trim();
            String key = mode.equals("1") ? "repository_factory.jdbc" : "repository_factory.jpa";

            // 3) Carregar RepositoryFactory dinàmicament
            String repoFcqn = props.getProperty(key);
            @SuppressWarnings("unchecked")
            Class<RepositoryFactory> repoClass =
                    (Class<RepositoryFactory>) Class.forName(repoFcqn);
            RepositoryFactory repoFactory = repoClass.getDeclaredConstructor().newInstance();

            // 4) Carregar ModelFactory dinàmicament
            String modelFcqn = props.getProperty("model_factory");
            @SuppressWarnings("unchecked")
            Class<ModelFactory> modelClass =
                    (Class<ModelFactory>) Class.forName(modelFcqn);
            ModelFactory modelFactory = modelClass.getDeclaredConstructor().newInstance();

            // 5) Bucle principal
            boolean running = true;
            while (running) {
                System.out.println("\n===== AeroAdmin Backoffice =====");
                System.out.println("1. Gestor d'Avions");
                System.out.println("2. Gestor de Pilots");
                System.out.println("3. Gestor de Companyies Aèries");
                System.out.println("4. Gestor de Vols");
                System.out.println("0. Sortir");
                System.out.print("Selecciona una opció: ");

                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        new AircraftManager(modelFactory, repoFactory, scanner).run();
                        break;
                    case "2":
                        new PilotManager(modelFactory, repoFactory, scanner).run();
                        break;
                    case "3":
                        new AirlineManager(modelFactory, repoFactory, scanner).run();
                        break;
                    case "4":
                        new FlightManager(modelFactory, repoFactory, scanner).run();
                        break;
                    case "0":
                        System.out.println("Sortint... Gràcies!");
                        running = false;
                        break;
                    default:
                        System.out.println("Opció invàlida.");
                }
            }
            scanner.close();

        } catch (Exception e) {
            System.err.println("Error en temps d'execució:");
            e.printStackTrace();
        }
    }
}
