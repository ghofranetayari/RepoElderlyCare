package elderlycare.Services;

import elderlycare.DAO.Entities.Tracking;
import elderlycare.DAO.Repositories.TrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class SimulationService {

    @Autowired
    private TrackingRepository trackingRepository;

    public void simulateMovement(long elderlyId) {
        Optional<Tracking> optionalTracking = trackingRepository.findByElderlyElderlyID(elderlyId);
        if (optionalTracking.isPresent()) {
            Tracking tracking = optionalTracking.get();
            // Simulate movement based on different scenarios
            if (isAtDestination(tracking)) {
                System.out.println("Elderly has reached the destination successfully!");
            } else if (isLost(tracking)) {
                System.out.println("Elderly has lost the way!");
                // Implement logic to handle getting lost (e.g., provide directions, call for help)
            } else {
                moveTowardsDestination(tracking);
                trackingRepository.save(tracking);
                System.out.println("Elderly is on the way to the destination.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tracking data not found for elderly ID: " + elderlyId);
        }
    }

    private boolean isAtDestination(Tracking tracking) {
        // Check if the elderly is at the destination
        double latitudeDiff = Math.abs(tracking.getLatitudeDest() - tracking.getLatitudeInitial());
        double longitudeDiff = Math.abs(tracking.getLongitudeDest() - tracking.getLongitudeInitial());
        return latitudeDiff < 0.0001 && longitudeDiff < 0.0001; // Adjust threshold for proximity to destination
    }

    private boolean isLost(Tracking tracking) {
        // Simulate the elderly getting lost based on certain conditions (e.g., wandering off course)
        // For simplicity, let's say they are lost if they deviate significantly from the direct path
        double distanceToDestination = calculateDistance(tracking.getLatitudeInitial(), tracking.getLongitudeInitial(),
                tracking.getLatitudeDest(), tracking.getLongitudeDest());
        return distanceToDestination > 0.1; // Adjust threshold for being lost
    }

    private void moveTowardsDestination(Tracking tracking) {
        // Calculate the distance to the destination
        double distanceToDestination = calculateDistance(tracking.getLatitudeInitial(), tracking.getLongitudeInitial(),
                tracking.getLatitudeDest(), tracking.getLongitudeDest());

        // Calculate the movement fraction based on the remaining distance
        double fractionOfDistance = 0.01; // Initial fraction of distance to move
        if (distanceToDestination < 0.01) {
            // If the remaining distance is less than the movement fraction, adjust the fraction
            fractionOfDistance = distanceToDestination / 2; // Move half of the remaining distance
        }

        // Calculate the direction and adjust the position towards the destination
        double latitudeDiff = tracking.getLatitudeDest() - tracking.getLatitudeInitial();
        double longitudeDiff = tracking.getLongitudeDest() - tracking.getLongitudeInitial();
        tracking.setLatitudeInitial(tracking.getLatitudeInitial() + (latitudeDiff * fractionOfDistance));
        tracking.setLongitudeInitial(tracking.getLongitudeInitial() + (longitudeDiff * fractionOfDistance));
    }


    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Calculate the distance between two coordinates using the Haversine formula
        double earthRadius = 6371; // in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}

