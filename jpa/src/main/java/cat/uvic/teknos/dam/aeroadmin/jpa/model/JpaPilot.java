package cat.uvic.teknos.dam.aeroadmin.jpa.model;

import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.model.Pilot;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotLicense;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pilot")
public class JpaPilot implements Pilot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pilot_id") // Bona pràctica: especificar el nom de la columna
    private int pilotId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String nationality;

    @OneToOne(targetEntity = JpaPilotLicense.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "pilot_id") // Normalment, la llicència comparteix el PK del pilot
    private PilotLicense license;

    @ManyToOne(targetEntity = JpaAirline.class)
    @JoinColumn(name = "airline_id")
    private Airline airline;

    // Getters i Setters de la interfície Pilot (i només aquests)

    @Override
    public int getPilotId() {
        return pilotId;
    }

    @Override
    public void setPilotId(int pilotId) {
        this.pilotId = pilotId;
    }

    @Override
    public Airline getAirline() {
        return airline;
    }

    @Override
    public void setAirline(Airline airline) {
        // Cal fer el cast a la implementació concreta de JPA
        this.airline = (JpaAirline) airline;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String getNationality() {
        return nationality;
    }

    @Override
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public PilotLicense getLicense() {
        return license;
    }

    @Override
    public void setLicense(PilotLicense license) {
        // Cal fer el cast a la implementació concreta de JPA
        this.license = (JpaPilotLicense) license;
    }
}