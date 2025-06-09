package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.model.ModelFactory;
import cat.uvic.teknos.dam.aeroadmin.repositories.AircraftRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;

import java.util.List;
import java.util.Scanner;

public class AircraftManager {

    private final ModelFactory modelFactory;
    private final RepositoryFactory repositoryFactory;
    private final AircraftRepository repository;
    private final Scanner scanner;

    public AircraftManager(ModelFactory modelFactory,
                           RepositoryFactory repositoryFactory,
                           Scanner scanner) {
        this.modelFactory      = modelFactory;
        this.repositoryFactory = repositoryFactory;
        this.repository        = repositoryFactory.getAircraftRepository();
        this.scanner           = scanner;
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Gestor d'Avions ---");
            System.out.println("1. Llistar avions");
            System.out.println("2. Mostrar avió");
            System.out.println("3. Guardar nou avió");
            System.out.println("4. Eliminar avió");
            System.out.println("0. Tornar enrere");
            System.out.print("Tria una opció: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    repository.getAll().forEach(a ->
                            System.out.printf("ID: %d | Model: %s | Fabricant: %s%n",
                                    a.getAircraftId(), a.getModel(), a.getManufacturer()));
                    break;

                case "2":
                    System.out.print("Introdueix ID a mostrar: ");
                    int idShow = Integer.parseInt(scanner.nextLine());
                    Aircraft shown = repository.get(idShow);
                    System.out.println(shown != null ? shown : "Avió no trobat.");
                    break;

                case "3":
                    // 1) Demanem dades bàsiques
                    Aircraft newAircraft = modelFactory.createAircraft();
                    System.out.print("Model: ");
                    newAircraft.setModel(scanner.nextLine());
                    System.out.print("Fabricant: ");
                    newAircraft.setManufacturer(scanner.nextLine());
                    System.out.print("Matrícula: ");
                    newAircraft.setRegistrationNumber(scanner.nextLine());
                    System.out.print("Any de producció: ");
                    newAircraft.setProductionYear(Integer.parseInt(scanner.nextLine()));

                    // 2) Llistar companyies disponibles
                    List<Airline> airlines =
                            (List<Airline>) repositoryFactory.getAirlineRepository().getAll();
                    System.out.println("\nCompanyies Aèries disponibles:");
                    airlines.forEach(al ->
                            System.out.printf("  ID: %d | Nom: %s | IATA: %s%n",
                                    al.getAirlineId(), al.getName(), al.getIataCode()));

                    // 3) Demanar i validar l'ID de la companyia
                    System.out.print("Introdueix ID de la Companyia Aèria: ");
                    String airlineInput = scanner.nextLine().trim();
                    int airlineId;
                    try {
                        airlineId = Integer.parseInt(airlineInput);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ L'ID ha de ser un número. Abortant operació.");
                        break;
                    }

                    Airline airline = repositoryFactory
                            .getAirlineRepository()
                            .get(airlineId);
                    if (airline == null) {
                        System.out.println("❌ Companyia no trobada. Abortant operació.");
                        break;
                    }
                    newAircraft.setAirline(airline);

                    // 4) Guardar l'avió
                    repository.save(newAircraft);
                    System.out.println("✅ Avió guardat correctament.");
                    break;

                case "4":
                    System.out.print("Introdueix ID a eliminar: ");
                    int idDel = Integer.parseInt(scanner.nextLine());
                    Aircraft toDelete = repository.get(idDel);
                    if (toDelete != null) {
                        repository.delete(toDelete);
                        System.out.println("✅ Avió eliminat.");
                    } else {
                        System.out.println("Avió no trobat.");
                    }
                    break;

                case "0":
                    exit = true;
                    break;

                default:
                    System.out.println("Opció invàlida.");
            }
        }
    }
}
