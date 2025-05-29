package cat.uvic.teknos.dam.aeroadmin.repositories;  // <--- Canvia el paquet aquí

import java.util.Set;

/**
 * Interfície genèrica per gestionar operacions CRUD sobre qualsevol entitat.
 * @param <K> Tipus de la clau primària (normalment Integer)
 * @param <V> Tipus d'entitat (ex: Airline, Pilot...)
 */
public interface Repository<K, V> {

    /**
     * Guarda o actualitza una entitat.
     * @param value Entitat a desar
     */
    void save(V value);

    /**
     * Elimina una entitat.
     * @param value Entitat a eliminar
     */
    void delete(V value);

    /**
     * Recupera una entitat segons el seu ID.
     * @param id L'identificador de l'entitat
     * @return L'entitat trobada o null si no es troba
     */
    V get(K id);

    /**
     * Recupera totes les entitats.
     * @return Conjunt amb totes les entitats
     */
    Set<V> getAll();
}