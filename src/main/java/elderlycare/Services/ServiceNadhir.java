package elderlycare.Services;

import elderlycare.DAO.Entities.*;
import elderlycare.DAO.Repositories.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceNadhir implements IserviceNadhir{
    AmbulanceRepository ambulanceRepo;
    AmbulanceDriverRepository ambulanceDriverRepo;
    AmbulanceOwnerRepository ambulanceOwnerRepo;
    RelativeRepository relativeRepository;
    ElderlyRepository elderlyRepository;

    @Override
    public Ambulance ajouterAmbulance(Ambulance ambulance) {
        return ambulanceRepo.save(ambulance);

    }

    @Override
    public AmbulanceDriver ajouterAmbulanceDriver(AmbulanceDriver ambulanceDriver) {
        AmbulanceDriver driver = ambulanceDriverRepo.save(ambulanceDriver);
        ambulanceDriverRepo.save(driver);
        return driver;
    }

    @Override
    public AmbulanceOwner ajouterAmbulanceOwner(AmbulanceOwner ambulanceOwner) {
        AmbulanceOwner aa = ambulanceOwnerRepo.save(ambulanceOwner);
        ambulanceOwnerRepo.save(aa);
        return aa;
    }

    @Override
    public void archiveAmbulance(long ambulanceId) {
        Ambulance ambulance = ambulanceRepo.findById(ambulanceId)
                .orElseThrow(() -> new RuntimeException("Ambulance not found with id: " + ambulanceId));
        ambulance.setArchive(false); // Mettre l'attribut archive à false
        ambulanceRepo.save(ambulance);
    }

    public List<Ambulance> getNonArchivedAmbulances() {
        return ambulanceRepo.findByArchiveTrue();
    }

    public Ambulance getAmbulanceByAmbulanceID(Long ambulanceID) {
        return ambulanceRepo.findById(ambulanceID).get();
    }

    public Ambulance updateAmbulance(Ambulance ambulance) {
        return ambulanceRepo.save(ambulance);
    }

    public void assignAmbulanceToDriver(long ambulanceId, long driverId) {
        Ambulance ambulance = ambulanceRepo.findById(ambulanceId).orElseThrow(() -> new RuntimeException("Ambulance not found"));
        AmbulanceDriver driver = ambulanceDriverRepo.findById(driverId).orElseThrow(() -> new RuntimeException("Driver not found"));

        ambulance.setAmbulancedriver(driver);
        ambulanceRepo.save(ambulance);
    }

    public Ambulance getAmbulanceByLocation(String location) {
        return ambulanceRepo.findByLocation(location);
    }

    public void assignDriverToAmbulance(long ambulanceId, AmbulanceDriver driver) {
        // Vérifier si l'ambulance existe
        Ambulance ambulance = ambulanceRepo.findById(ambulanceId)
                .orElseThrow(() -> new IllegalArgumentException("Ambulance not found with ID: " + ambulanceId));

        // Vérifier si le conducteur existe ou le sauvegarder s'il est nouveau
        AmbulanceDriver existingDriver = ambulanceDriverRepo.findById(driver.getAmbulanceDriverID()).orElse(null);
        if (existingDriver == null) {
            driver = ambulanceDriverRepo.save(driver);
        }

        // Affecter le conducteur à l'ambulance
        ambulance.setAmbulancedriver(driver);
        ambulanceRepo.save(ambulance);
    }
    public Ambulance updateAmbulanceStatus(long id) {
        Ambulance ambulance = ambulanceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ambulance not found with id: " + id));
        ambulance.setStatus(ambulance.getStatus().equals("Available") ? "NotAvailable" : "Available");
        return ambulanceRepo.save(ambulance);
    }
    public List<Ambulance> getAvailableAmbulances() {
        return ambulanceRepo.findByStatus("available");
    }
    public Elderly getElderlyByRelativeId(Long relativeId) {
        Optional<Relative> relativeOptional = relativeRepository.findById(relativeId);
        if (relativeOptional.isPresent()) {
            Relative relative = relativeOptional.get();
            return relative.getElderly();
        } else {
            throw new RuntimeException("Relative not found with ID: " + relativeId);
        }
    }
    public Relative updateRelativeEtats(long idRelative) {
        Optional<Relative> optionalRelative = relativeRepository.findById(idRelative);
        if (optionalRelative.isPresent()) {
            Relative relative = optionalRelative.get();
            relative.setEtats(1L); // Mettre l'état à 1
            return relativeRepository.save(relative);
        } else {
            throw new RuntimeException("Relative not found with id: " + idRelative);
        }
    }
    public List<Relative> getRelativesByEtats(long etats) {
        return relativeRepository.findByEtats(etats);
    }


}
