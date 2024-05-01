package elderlycare.Services;

import elderlycare.DAO.Entities.*;
import elderlycare.DAO.Repositories.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceGlobal implements IServiceGlobal {
    AmbulanceRepository ambulanceRepo;
    AmbulanceDriverRepository ambulanceDriverRepo;
    AmbulanceOwnerRepository ambulanceOwnerRepo;
    AmbulanceDriverRepository ambulanceDriverRepository;
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
    public List<AmbulanceDriver> getAllAmbulanceDrivers() {
        return ambulanceDriverRepository.findAll();
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

    public Optional<AmbulanceDriver> getAmbulanceDriverById(long id) {
        return ambulanceDriverRepository.findById(id);
    }

    @Transactional
    public void updateOnDutyStatus(long driverId) {
        Optional<AmbulanceDriver> optionalDriver = ambulanceDriverRepository.findById(driverId);
        if (optionalDriver.isPresent()) {
            AmbulanceDriver driver = optionalDriver.get();
            driver.setOnDuty(true);
            ambulanceDriverRepository.save(driver);
        } else {
            // Gérer le cas où l'ID du conducteur n'existe pas
        }
    }

    public List<AmbulanceDriver> getOnDutyDrivers() {
        List<AmbulanceDriver> allDrivers = ambulanceDriverRepository.findAll();
        return allDrivers.stream()
                .filter(AmbulanceDriver::getOnDuty) // Filtrer les conducteurs en service
                .collect(Collectors.toList());
    }

    public AmbulanceDriver addTotalForDriver(long driverId, Float total) {
        AmbulanceDriver driver = ambulanceDriverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Conducteur introuvable avec l'ID : " + driverId));

        driver.setTotalForDriver(total);
        return ambulanceDriverRepository.save(driver);
    }
    public Elderly addTrackingToElderly(Long elderlyId, String trackingValue) {
        Elderly elderly = elderlyRepository.findById(elderlyId).orElse(null);
        if (elderly != null) {
            if(Objects.equals(trackingValue, "{\"drowsinessStatus\":\"Not Drowsy\"}")){
                trackingValue="NotDrowsy";
            }
            else{
                trackingValue="Drowsy";
            }
            System.err.println(trackingValue);
            elderly.setTracking(trackingValue);
            return elderlyRepository.save(elderly);
        }
        return null;
    }

    public String getTrackingOfElderly(Long elderlyId) {
        Elderly elderly = elderlyRepository.findById(elderlyId).orElse(null);
        if (elderly != null) {
            return elderly.getTracking();
        }
        return null;
    }
    public Long getElderlyIdByRelativeId(Long relativeId) {
        Relative relative = relativeRepository.findById(relativeId).orElse(null);
        if (relative != null && relative.getElderly() != null) {
            return relative.getElderly().getElderlyID();
        }
        return null; // Retourne null si aucun Elderly associé n'est trouvé
    }
}