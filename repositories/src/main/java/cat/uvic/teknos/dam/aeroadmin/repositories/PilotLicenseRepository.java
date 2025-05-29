package cat.uvic.teknos.dam.aeroadmin.repositories;

import cat.uvic.teknos.dam.aeroadmin.model.enums.LicenseType;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotLicense;

import java.util.Set;

public interface PilotLicenseRepository extends Repository<Integer, PilotLicense> {
    Set<PilotLicense> getByLicenseType(LicenseType licenseType);
}