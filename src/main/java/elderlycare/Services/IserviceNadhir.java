package elderlycare.Services;

import elderlycare.DAO.Entities.*;

import java.util.List;

public interface IserviceNadhir {

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
    public Elderly getElderlyByRelativeId(Long relativeId);
    public Relative updateRelativeEtats(long idRelative);
    public List<Relative> getRelativesByEtats(long etats);


}
