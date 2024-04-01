package elderlycare.Services;

import elderlycare.DAO.Entities.Ambulance;
import elderlycare.DAO.Entities.AmbulanceDriver;
import elderlycare.DAO.Repositories.AmbulanceDriverRepository;
import elderlycare.DAO.Repositories.AmbulanceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceNadhir implements IserviceNadhir{

    AmbulanceRepository ambulanceRepository;
    AmbulanceDriverRepository ambulanceDriverRepository;

    @Override
    public Ambulance ajouterAmbulance(Ambulance ambulance) {
        return ambulanceRepository.save(ambulance);

    }


    @Override
    public void archiveAmbulance(long ambulanceId) {
        Ambulance ambulance = ambulanceRepository.findById(ambulanceId)
                .orElseThrow(() -> new RuntimeException("Ambulance not found with id: " + ambulanceId));
        ambulance.setArchive(false); // Mettre l'attribut archive à false
        ambulanceRepository.save(ambulance);
    }

    public List<Ambulance> getNonArchivedAmbulances() {
        return ambulanceRepository.findByArchiveTrue();
    }
    public Ambulance getAmbulanceByAmbulanceID(Long ambulanceID) {
        return ambulanceRepository.findById(ambulanceID).orElse(null);
    }

    public Ambulance updateAmbulance(Ambulance ambulance) {
        return ambulanceRepository.save(ambulance);
    }

    public Ambulance ajouterAmbulanceEtAffecterAmbulanceDriver(Ambulance a, Long AmbulanceDriverID) {
        Ambulance ambulance = ambulanceRepository.save(a);
        AmbulanceDriver ambulanceDriver = ambulanceDriverRepository.findById(AmbulanceDriverID).get();
        ambulance.setAmbulancedriver(ambulanceDriver);
        ambulanceRepository.save(a);
        return ambulance;
    }

    public Ambulance assignAmbulanceToDriver(long ambulanceId, long driverId) {
        Ambulance ambulance = ambulanceRepository.findById(ambulanceId).orElse(null);
        AmbulanceDriver driver = ambulanceDriverRepository.findById(driverId).orElse(null);

        if (ambulance != null && driver != null) {
            ambulance.setAmbulancedriver(driver);
            return ambulanceRepository.save(ambulance);
        } else {
            // Gérer le cas où l'ambulance ou le conducteur n'existe pas
            return null; // ou lancez une exception appropriée
        }
    }

}
