package elderlycare.Services;

import elderlycare.DAO.Entities.Ambulance;

import java.util.List;

public interface IserviceNadhir {

    Ambulance ajouterAmbulance (Ambulance ambulance);



    public void archiveAmbulance(long ambulanceId);

    public List<Ambulance> getNonArchivedAmbulances();
    public Ambulance updateAmbulance(Ambulance ambulance);
    public Ambulance getAmbulanceByAmbulanceID(Long ambulanceID);

    public Ambulance ajouterAmbulanceEtAffecterAmbulanceDriver(Ambulance a, Long AmbulanceDriverID);
    public Ambulance assignAmbulanceToDriver(long ambulanceId, long driverId);
}
