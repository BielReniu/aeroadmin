package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.model.ModelFactory;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class App {

    // ... (la part de dalt del fitxer és igual)

    public static void main(String[] args) {
        try {
            // ... (la part de càrrega de propietats i selecció de JDBC/JPA és igual)

            Properties props = new Properties();
            try (InputStream in = App.class.getResourceAsStream("/di.properties")) {
                if (in == null) {
                    throw new RuntimeException("No s’ha trobat di.properties");
                }
                props.load(in);
            }

            Scanner scanner = new Scanner(System.in);
            System.out.println("Selecciona l'estratègia de persistència:");
            System.out.println("  1) JDBC");
            System.out.println("  2) JPA");
            System.out.print("Tria 1 o 2: ");
            String mode = scanner.nextLine().trim();
            String key = mode.equals("1") ? "repository_factory.jdbc" : "repository_factory.jpa";

            String repoFcqn = props.getProperty(key);
            @SuppressWarnings("unchecked")
            Class<RepositoryFactory> repoClass =
                    (Class<RepositoryFactory>) Class.forName(repoFcqn);
            RepositoryFactory repoFactory = repoClass.getDeclaredConstructor().newInstance();

            String modelFcqn = props.getProperty("model_factory");
            @SuppressWarnings("unchecked")
            Class<ModelFactory> modelClass =
                    (Class<ModelFactory>) Class.forName(modelFcqn);
            ModelFactory modelFactory = modelClass.getDeclaredConstructor().newInstance();

            boolean running = true;
            while (running) {
                System.out.println("\n===== AeroAdmin Backoffice =====");
                System.out.println("1. Gestor de Companyies Aèries");
                System.out.println("2. Gestor d'Avions");
                System.out.println("3. Gestor de Detalls d'Avió");
                System.out.println("4. Gestor de Pilots");
                System.out.println("5. Gestor de Llicències de Pilot");
                System.out.println("6. Gestor de Vols");
                System.out.println("7. Gestor d'Assignacions de Pilot");
                System.out.println("0. Sortir");
                System.out.print("Selecciona una opció: ");

                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1": new AirlineManager(repoFactory, scanner).run(); break;
                    case "2": new AircraftManager(repoFactory, scanner).run(); break;
                    case "3": new AircraftDetailManager(repoFactory, scanner).run(); break;
                    case "4": new PilotManager(repoFactory, scanner).run(); break;
                    case "5": new PilotLicenseManager(repoFactory, scanner).run(); break;
                    case "6": new FlightManager(repoFactory, scanner).run(); break;
                    case "7": new PilotAssignmentManager(repoFactory, scanner).run(); break;
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
            System.err.println("Ha ocorregut un error crític a l'aplicació:");
            e.printStackTrace();
        }
    }
}
