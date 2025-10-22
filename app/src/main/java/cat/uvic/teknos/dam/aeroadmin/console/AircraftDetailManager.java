package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.model.AircraftDetail;
import cat.uvic.teknos.dam.aeroadmin.repositories.AircraftDetailRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;

import java.util.Scanner;

public class AircraftDetailManager {
    private final AircraftDetailRepository repo;
    private final Scanner sc;

    public AircraftDetailManager(RepositoryFactory rf, Scanner sc) {
        this.sc = sc;
        this.repo = rf.getAircraftDetailRepository();
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Gestor de Detalls d'Avió ---");
            System.out.println("1. Llistar detalls");
            System.out.println("2. Crear nou detall");
            System.out.println("0. Tornar enrere");
            System.out.print("Tria una opció: ");
            String c = sc.nextLine().trim();

            switch (c) {
                case "1":
                    listAll();
                    break;
                case "2":
                    createDetail();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Opció invàlida.");
            }
        }
    }

    private void listAll() {
        System.out.println("\n--- Llista de Detalls d'Avió ---");
        var details = repo.getAll();
        if (details.isEmpty()) {
            System.out.println("No hi ha detalls per mostrar.");
        } else {
            details.forEach(this::printDetail);
        }
    }

    private void createDetail() {
        System.out.println("\n--- Creació d'un Nou Detall ---");
        try {
            AircraftDetail newDetail = repo.create();

            System.out.print("ID de l'avió al qual pertany: ");
            newDetail.setAircraftId(Integer.parseInt(sc.nextLine()));

            System.out.print("Capacitat de passatgers: ");
            newDetail.setPassengerCapacity(Integer.parseInt(sc.nextLine()));

            System.out.print("Rang màxim (km): ");
            newDetail.setMaxRangeKm(Integer.parseInt(sc.nextLine()));

            System.out.print("Velocitat màxima (km/h): ");
            newDetail.setMaxSpeedKmh(Integer.parseInt(sc.nextLine()));

            System.out.print("Capacitat de combustible (litres): ");
            newDetail.setFuelCapacityLiters(Integer.parseInt(sc.nextLine()));

            repo.save(newDetail);
            System.out.println("Detall creat correctament.");

        } catch (NumberFormatException e) {
            System.err.println("Error: Has d'introduir un número vàlid. Operació cancel·lada.");
        } catch (Exception e) {
            System.err.println("Ha ocorregut un error en desar el detall (potser l'ID de l'avió no existeix?): " + e.getMessage());
        }
    }

    private void printDetail(AircraftDetail detail) {
        System.out.println(
                "ID Avió: " + detail.getAircraftId() +
                        " | Capacitat: " + detail.getPassengerCapacity() +
                        " | Rang: " + detail.getMaxRangeKm() + " km" +
                        " | Vel. Màx: " + detail.getMaxSpeedKmh() + " km/h" +
                        " | Combustible: " + detail.getFuelCapacityLiters() + " L"
        );
    }
}