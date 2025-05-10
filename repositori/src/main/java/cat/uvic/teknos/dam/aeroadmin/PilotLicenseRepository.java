package cat.uvic.teknos.dam.aeroadmin;

import cat.uvic.teknos.dam.aeroadmin.enums.LicenseType;
import cat.uvic.teknos.dam.aeroadmin.model.PilotLicense;

import java.util.Set;

public interface PilotLicenseRepository extends Repository<Integer, PilotLicense> {
    Set<PilotLicense> getByLicenseType(LicenseType licenseType);
}