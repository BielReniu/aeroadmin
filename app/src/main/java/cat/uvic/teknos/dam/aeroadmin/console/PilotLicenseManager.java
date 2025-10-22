package cat.uvic.teknos.dam.aeroadmin.console;

import cat.uvic.teknos.dam.aeroadmin.model.enums.LicenseType;
import cat.uvic.teknos.dam.aeroadmin.model.model.Pilot;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotLicense;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotLicenseRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

public class PilotLicenseManager {
    private final PilotLicenseRepository licenseRepository;
    private final PilotRepository pilotRepository;
    private final Scanner sc;

    public PilotLicenseManager(RepositoryFactory rf, Scanner sc) {
        this.sc = sc;
        this.licenseRepository = rf.getPilotLicenseRepository();
        this.pilotRepository = rf.getPilotRepository();
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Gestor de Llicències de Pilot ---");
            System.out.println("1. Llistar llicències");
            System.out.println("2. Crear/Actualitzar llicència");
            System.out.println("3. Eliminar llicència");
            System.out.println("0. Tornar enrere");
            System.out.print("Tria una opció: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": listAll(); break;
                case "2": save();    break; // Unificat Crear i Actualitzar
                case "3": delete();  break;
                case "0": exit = true; break;
                default:  System.out.println("Opció invàlida.");
            }
        }
    }

    private void printLicense(PilotLicense license) {
        Pilot pilot = pilotRepository.get(license.getPilotId());
        String pilotName = (pilot != null) ? pilot.getFirstName() + " " + pilot.getLastName() : "Pilot Desconegut";
        System.out.printf("ID Pilot: %-3d | Nom Pilot: %-25s | Llicència: %-15s | Tipus: %s%n",
                license.getPilotId(), pilotName, license.getLicenseNumber(), license.getLicenseType());
    }

    private void listAll() {
        System.out.println("\n--- Llista de Llicències ---");
        Set<PilotLicense> licenses = licenseRepository.getAll();
        if (licenses.isEmpty()) {
            System.out.println("No hi ha llicències per mostrar.");
        } else {
            licenses.forEach(this::printLicense);
        }
    }

    private void save() {
        System.out.println("\n--- Crear o Actualitzar Llicència ---");
        try {
            // 1. Seleccionar un pilot
            System.out.println("\n--- Pilots Disponibles ---");
            var pilots = new ArrayList<>(pilotRepository.getAll());
            if (pilots.isEmpty()) {
                System.out.println("❌ No hi ha pilots registrats. No es pot assignar una llicència.");
                return;
            }
            pilots.forEach(p -> System.out.printf("  ID: %d | Nom: %s %s%n", p.getPilotId(), p.getFirstName(), p.getLastName()));
            System.out.print("Introdueix l'ID del pilot per a la llicència: ");
            int pilotId = Integer.parseInt(sc.nextLine());

            // Comprovem si el pilot existeix
            if (pilotRepository.get(pilotId) == null) {
                System.out.println("❌ Pilot no trobat. Operació cancel·lada.");
                return;
            }

            PilotLicense license = licenseRepository.get(pilotId);
            if (license == null) {
                System.out.println("Creant una nova llicència per a aquest pilot.");
                license = licenseRepository.create();
                license.setPilotId(pilotId);
            } else {
                System.out.println("Actualitzant la llicència existent per a aquest pilot.");
            }

            System.out.print("Número de llicència: ");
            license.setLicenseNumber(sc.nextLine());

            // 3. Seleccionar un tipus de llicència
            System.out.println("\n--- Tipus de Llicència Disponibles ---");
            Arrays.stream(LicenseType.values()).forEach(type -> System.out.println(" - " + type.name()));
            System.out.print("Introdueix el tipus (p. ex., ATPL): ");
            license.setLicenseType(LicenseType.valueOf(sc.nextLine().toUpperCase()));

            System.out.print("Data d'emissió (yyyy-MM-dd): ");
            license.setIssueDate(LocalDate.parse(sc.nextLine()));

            System.out.print("Data de caducitat (yyyy-MM-dd): ");
            license.setExpirationDate(LocalDate.parse(sc.nextLine()));

            licenseRepository.save(license);
            System.out.println("✅ Llicència desada correctament.");

        } catch (NumberFormatException e) {
            System.err.println("Error: L'ID ha de ser un número vàlid.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error: El tipus de llicència introduït no és vàlid.");
        } catch (DateTimeParseException e) {
            System.err.println("Error: El format de la data no és correcte. Ha de ser 'yyyy-MM-dd'.");
        } catch (Exception e) {
            System.err.println("Ha ocorregut un error inesperat: " + e.getMessage());
        }
    }

    private void delete() {
        System.out.println("\n--- Eliminar Llicència ---");
        System.out.print("Introdueix l'ID del pilot de la llicència a eliminar: ");
        try {
            int id = Integer.parseInt(sc.nextLine());
            PilotLicense license = licenseRepository.get(id);
            if (license != null) {
                licenseRepository.delete(license);
                System.out.println("✅ Llicència eliminada correctament.");
            } else {
                System.out.println("❌ No s'ha trobat cap llicència per a aquest pilot.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: L'ID ha de ser un número.");
        } catch (Exception e) {
            System.err.println("Ha ocorregut un error inesperat: " + e.getMessage());
        }
    }
}