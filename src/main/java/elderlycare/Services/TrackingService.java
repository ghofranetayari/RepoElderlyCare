package elderlycare.Services;

import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Entities.Relative;
import elderlycare.DAO.Entities.Tracking;
import elderlycare.DAO.Repositories.ElderlyRepository;
import elderlycare.DAO.Repositories.RelativeRepository;
import elderlycare.DAO.Repositories.TrackingRepository;
import elderlycare.dto.TrackingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class TrackingService {

    @Autowired
    private TrackingRepository trackingRepository;

    @Autowired
    private ElderlyRepository elderlyRepository;



    @Autowired
    private RelativeRepository relativeRepository;


    public Optional<Tracking> getTrackingByElderlyId(long elderlyId) {
        return trackingRepository.findByElderlyElderlyID(elderlyId);
    }

    public Optional<Tracking> getTrackingById(long id) {
        return trackingRepository.findById(id);
    }

    public Tracking addTracking(Tracking tracking, long elderlyId) {
        Optional<Elderly> elderlyOptional = elderlyRepository.findById(elderlyId);
        if (elderlyOptional.isPresent()) {
            Elderly elderly = elderlyOptional.get();
            tracking.setElderly(elderly); // Set the associated elderly
            return trackingRepository.save(tracking);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Elderly with ID " + elderlyId + " not found");
        }
    }







    public TrackingDTO getElderlyCoordinatesByRelativeId(Long relativeId) {
        Relative relative = relativeRepository.findByIdRelative(relativeId);
        if (relative != null) {
            Elderly elderly = relative.getElderly();
            if (elderly != null) {
                Tracking tracking = trackingRepository.findByElderlyElderlyID(elderly.getElderlyID()).orElse(null);
                if (tracking != null) {
                    TrackingDTO trackingDTO = new TrackingDTO();
                    trackingDTO.setLatitudeInitial(tracking.getLatitudeInitial());
                    trackingDTO.setLongitudeInitial(tracking.getLongitudeInitial());
                    return trackingDTO;
                }
            }
        }
        return null;
    }




    public String getElderlyNameByRelativeId(long relativeId) {
        Relative relative = relativeRepository.findById(relativeId).orElse(null);
        if (relative != null) {
            Elderly elderly = relative.getElderly();
            return elderly != null ? elderly.getFirstName() + " " + elderly.getLastName() : null;
        }
        return null;
    }







        public Tracking updateTracking(Long trackingId, Tracking updatedTracking) {
            Optional<Tracking> existingTracking = trackingRepository.findById(trackingId);
            if (!existingTracking.isPresent()) {
                return null;
            }

            Tracking existing = existingTracking.get();
            if (updatedTracking.getLatitudeInitial() != null) {
                existing.setLatitudeInitial(updatedTracking.getLatitudeInitial());
            }
            existing.setLongitudeInitial(updatedTracking.getLongitudeInitial() != null ? updatedTracking.getLongitudeInitial() : existing.getLongitudeInitial());
            existing.setLatitudeDest(updatedTracking.getLatitudeDest() != null ? updatedTracking.getLatitudeDest() : existing.getLatitudeDest());
            existing.setLongitudeDest(updatedTracking.getLongitudeDest() != null ? updatedTracking.getLongitudeDest() : existing.getLongitudeDest());
            existing.setInitial(updatedTracking.getInitial() != null ? updatedTracking.getInitial() : existing.getInitial());
            existing.setDestination(updatedTracking.getDestination() != null ? updatedTracking.getDestination() : existing.getDestination());
            existing.setId(trackingId);
            return trackingRepository.save(existing);
        }

    public Long getTrackingIdByElderlyId(Long elderlyId) {

        Optional<Tracking> tracking = trackingRepository.findByElderlyElderlyID(elderlyId);
        if (tracking.isPresent()) {
            return tracking.get().getId();
        } else {
            return null;
        }
    }



    public Optional<Tracking> getTrackingByRelativeId(Long relativeId) {
        Relative relative = relativeRepository.findById(relativeId).orElse(null);
        if (relative != null) {
            Elderly elderly = relative.getElderly();
            if (elderly != null) {
                return trackingRepository.findByElderlyElderlyID(elderly.getElderlyID());
            }
        }
        return Optional.empty();
    }



    public String getRelativePhoneNumberByElderlyId(Long elderlyId) {
        Elderly elderly = elderlyRepository.findById(elderlyId).orElse(null);
        if (elderly != null && elderly.getUser() != null) {
            return elderly.getUser().getPhoneNumber();
        }
        return null;
    }
    public String getRelativePhoneNumberById(Long id) {
        Relative relative = relativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relative not found with id: " + id));
        return relative.getPhoneNumber();
    }
    public void deleteTracking(long id) {
        trackingRepository.deleteById(id);
    }
}
