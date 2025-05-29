package cat.uvic.teknos.dam.aeroadmin.repositories;

import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.model.Pilot;

import java.util.Set;

public interface PilotRepository extends Repository<Integer, Pilot> {
    Set<Pilot> getByAirline(Airline airline);
}