package cat.uvic.teknos.dam.aeroadmin;

import cat.uvic.teknos.dam.aeroadmin.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.Pilot;

import java.util.Set;

public interface PilotRepository extends Repository<Integer, Pilot> {
    Set<Pilot> getByAirline(Airline airline);
}