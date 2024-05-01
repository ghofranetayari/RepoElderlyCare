package elderlycare.Services;

import elderlycare.DAO.Entities.Ambulance;
import elderlycare.DAO.Entities.AmbulanceDriver;
import elderlycare.DAO.Entities.AmbulanceOwner;
import elderlycare.DAO.Entities.Elderly;

import java.util.List;
import java.util.Optional;

public interface IServiceGlobal {

    Ambulance ajouterAmbulance (Ambulance ambulance);
    AmbulanceDriver ajouterAmbulanceDriver(AmbulanceDriver ambulanceDriver);

    AmbulanceOwner ajouterAmbulanceOwner(AmbulanceOwner ambulanceOwner);
    public void archiveAmbulance(long ambulanceId);

    public List<Ambulance> getNonArchivedAmbulances();
    public Ambulance updateAmbulance(Ambulance ambulance);
    public Ambulance getAmbulanceByAmbulanceID(Long ambulanceID);
    public void assignAmbulanceToDriver(long ambulanceId, long driverId);
    public Ambulance getAmbulanceByLocation(String location);
    void assignDriverToAmbulance(long ambulanceId, AmbulanceDriver driver);
    public Ambulance updateAmbulanceStatus(long id);
    public List<Ambulance> getAvailableAmbulances();
    public List<AmbulanceDriver> getAllAmbulanceDrivers();
    public Elderly getElderlyByRelativeId(Long relativeId);
    public Optional<AmbulanceDriver> getAmbulanceDriverById(long id);
    public void updateOnDutyStatus(long driverId);
    public List<AmbulanceDriver> getOnDutyDrivers();

    public AmbulanceDriver addTotalForDriver(long driverId, Float total);
    public Elderly addTrackingToElderly(Long elderlyId, String trackingValue);
    public String getTrackingOfElderly(Long elderlyId);
    public Long getElderlyIdByRelativeId(Long relativeId);
}

