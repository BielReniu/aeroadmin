package cat.uvic.teknos.dam.aeroadmin.client;

import cat.uvic.teknos.dam.aeroadmin.model.impl.AirlineImpl;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

/**
 * Aquesta classe gestiona la lògica de la interfície d'usuari (consola)
 * per a les Aerolínies, interactuant amb l'ApiClient.
 */
public class AirlineManager {
    private final Scanner sc;
    // MODIFICAT: Canvia el tipus de client
    private final PersistentAirlineApiClient apiClient;

    // MODIFICAT: Rep el nou tipus de client via constructor
    public AirlineManager(Scanner sc, PersistentAirlineApiClient apiClient) {
        this.sc = sc;
        this.apiClient = apiClient;
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Gestor de Companyies Aèries (Client) ---");
            System.out.println("1. Llistar");
            System.out.println("2. Crear");
            System.out.println("3. Actualitzar");
            System.out.println("4. Eliminar");
            System.out.println("5. Veure'n una per ID");
            System.out.println("0. Tornar enrere");
            System.out.print("Tria una opció: ");
            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1": listAll(); break;
                    case "2": create();  break;
                    case "3": update();  break;
                    case "4": delete();  break;
                    case "5": getById(); break;
                    case "0": exit = true; break;
                    default:  System.out.println("Opció invàlida.");
                }
            } catch (IOException e) {
                // Captura els errors de connexió o errors 4xx/5xx del servidor
                System.err.println("❌ Error de comunicació amb el servidor: " + e.getMessage());
                // Si l'error és perquè el socket s'ha tancat (p.ex. per inactivitat),
                // la lògica de reconnexió a PersistentAirlineApiClient s'activarà
                // a la següent petició.
            } catch (NumberFormatException e) {
                System.err.println("❌ Error: L'ID o l'any han de ser números.");
            } catch (Exception e) {
                // Qualsevol altre error
                System.err.println("❌ Ha ocorregut un error inesperat: " + e.getMessage());
                e.printStackTrace(); // Útil per debugar
            }
        }
    }

    private void printAirline(Airline a) {
        System.out.printf("  ID: %-3d | Nom: %-30s | IATA: %s | País: %s%n",
                a.getAirlineId(), a.getAirlineName(), a.getIataCode(), a.getCountry());
    }

    private void listAll() throws IOException {
        System.out.println("\n--- Llista de Companyies Aèries ---");
        Set<Airline> airlines = apiClient.getAllAirlines();
        if (airlines.isEmpty()) {
            System.out.println("No hi ha companyies per mostrar.");
        } else {
            airlines.forEach(this::printAirline);
        }
    }

    private void getById() throws IOException, NumberFormatException {
        System.out.println("\n--- Veure Companyia per ID ---");
        System.out.print("Introdueix l'ID: ");
        int id = Integer.parseInt(sc.nextLine());
        Airline airline = apiClient.getAirlineById(id);
        printAirline(airline);
    }

    /**
     * Mètode privat per llegir dades d'una aerolínia des de la consola.
     * Si 'airlineToUpdate' no és nul, mostra les dades anteriors.
     */
    private Airline readAirlineDataFromUser(Airline airlineToUpdate) throws NumberFormatException {
        Airline airline = new AirlineImpl();

        System.out.print("Nom" + (airlineToUpdate != null ? " (anterior: '" + airlineToUpdate.getAirlineName() + "')" : "") + ": ");
        airline.setAirlineName(sc.nextLine());

        System.out.print("Codi IATA" + (airlineToUpdate != null ? " (anterior: '" + airlineToUpdate.getIataCode() + "')" : "") + ": ");
        airline.setIataCode(sc.nextLine());

        System.out.print("Codi ICAO" + (airlineToUpdate != null ? " (anterior: '" + airlineToUpdate.getIcaoCode() + "')" : "") + ": ");
        airline.setIcaoCode(sc.nextLine());

        System.out.print("País" + (airlineToUpdate != null ? " (anterior: '" + airlineToUpdate.getCountry() + "')" : "") + ": ");
        airline.setCountry(sc.nextLine());

        System.out.print("Any de fundació (opcional)" + (airlineToUpdate != null ? " (anterior: '" + (airlineToUpdate.getFoundationYear() != null ? airlineToUpdate.getFoundationYear() : "") + "')" : "") + ": ");
        String yearInput = sc.nextLine();
        if (!yearInput.isBlank()) {
            airline.setFoundationYear(Integer.parseInt(yearInput)); // Pot llançar NumberFormatException
        }

        System.out.print("Pàgina web (opcional)" + (airlineToUpdate != null ? " (anterior: '" + airlineToUpdate.getWebsite() + "')" : "") + ": ");
        airline.setWebsite(sc.nextLine());

        return airline;
    }

    private void create() throws IOException, NumberFormatException {
        System.out.println("\n--- Nova Companyia Aèria ---");
        Airline newAirline = readAirlineDataFromUser(null);
        Airline createdAirline = apiClient.createAirline(newAirline);
        System.out.println("✅ Companyia creada correctament amb ID: " + createdAirline.getAirlineId());
    }

    private void update() throws IOException, NumberFormatException {
        System.out.println("\n--- Actualitzar Companyia Aèria ---");
        System.out.print("Introdueix l'ID de la companyia a actualitzar: ");
        int id = Integer.parseInt(sc.nextLine());

        // Primer, la demanem per mostrar les dades anteriors
        Airline existingAirline = apiClient.getAirlineById(id);
        System.out.println("Introdueix les noves dades per a l'ID " + id + ":");

        Airline updatedAirlineData = readAirlineDataFromUser(existingAirline);
        updatedAirlineData.setAirlineId(id); // Assegurem que l'ID és el correcte

        Airline result = apiClient.updateAirline(updatedAirlineData);
        System.out.println("✅ Companyia actualitzada correctament.");
        printAirline(result);
    }

    private void delete() throws IOException, NumberFormatException {
        System.out.println("\n--- Eliminar Companyia Aèria ---");
        System.out.print("Introdueix l'ID de la companyia a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());
        apiClient.deleteAirline(id);
        System.out.println("✅ Companyia eliminada correctament.");
    }
}