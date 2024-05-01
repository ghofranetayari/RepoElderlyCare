package elderlycare.RestControllers;

import elderlycare.DAO.Entities.Relative;
import elderlycare.DAO.Entities.Tracking;
import elderlycare.DAO.Repositories.RelativeRepository;
import elderlycare.Services.SimulationService;
import elderlycare.Services.TrackingService;
import elderlycare.dto.TrackingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tracking")
public class TrackingController {

    @Autowired
    private SimulationService simulationService;
    private final TrackingService trackingService;




    @Autowired
    private RelativeRepository relativeRepository;

    @Autowired
    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }





    @GetMapping ("/get/{tackingid}")
    public ResponseEntity<?> getTracking(@PathVariable long tackingid) {

            Optional <Tracking> tracking = trackingService.getTrackingById(tackingid);
        if (tracking != null) {
            return ResponseEntity.ok(tracking);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/save/{elderlyId}")
    public ResponseEntity<?> saveTracking(@PathVariable long elderlyId, @RequestBody Tracking tracking) {
        try {
            Tracking savedTracking = trackingService.addTracking(tracking, elderlyId);
            return ResponseEntity.ok(savedTracking);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving tracking data");
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<?> updateTracking(@PathVariable Long trackingId, @RequestBody Tracking updatedTracking) {
        try {
            Tracking existingTracking = trackingService.updateTracking(trackingId, updatedTracking);
            if (existingTracking == null) {
                return ResponseEntity.notFound().build(); // Indicate tracking not found
            }
            return ResponseEntity.ok(existingTracking);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating tracking data");
        }
    }



    @GetMapping("/relative/{relativeId}")
    public ResponseEntity<TrackingDTO> getTrackingByRelativeId(@PathVariable Long relativeId) {
        TrackingDTO trackingDTO = trackingService.getElderlyCoordinatesByRelativeId(relativeId);
        if (trackingDTO != null) {
            return ResponseEntity.ok(trackingDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/gettrackrelative/{relativeId}")
    public ResponseEntity<Tracking> getTrackingRelativeByRelativeId(@PathVariable Long relativeId) {
        Optional<Tracking> tracking = trackingService.getTrackingByRelativeId(relativeId);
        return tracking.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/{elderlyId}")
    public ResponseEntity<Long> getTrackingId(@PathVariable Long elderlyId) {
        try {
            Long trackingId = trackingService.getTrackingIdByElderlyId(elderlyId);
            if (trackingId != null) {
                return ResponseEntity.ok(trackingId);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L); // Return -1 as error indicator
        }
    }


    @GetMapping("/elderly/{relativeId}")
    public ResponseEntity<String> getElderlyNameByRelativeId(@PathVariable long relativeId) {
        String elderlyName = trackingService.getElderlyNameByRelativeId(relativeId);
        if (elderlyName != null) {
            return ResponseEntity.ok().body(elderlyName);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/relativePhone/{id}")
    public ResponseEntity<String> getRelativePhoneNumber(@PathVariable Long id) {
        String phoneNumber = trackingService.getRelativePhoneNumberById(id);
        return ResponseEntity.ok(phoneNumber);
    }



    @GetMapping("/phone/{elderlyId}")
    public ResponseEntity<String> getRelativePhoneNumberByElderlyId(@PathVariable Long elderlyId) {
        List<Relative> relatives = relativeRepository.findByElderlyElderlyID(elderlyId);
        for (Relative relative : relatives) {
            if (relative.getPhoneNumber().equals("50759756")) {
                return ResponseEntity.ok(relative.getPhoneNumber());
            }
        }
        return ResponseEntity.notFound().build();
    }


   /* @GetMapping("/simulate-movement")
    public Tracking simulateMovement(@RequestParam Long elderlyId, @RequestParam String scenario) {
        Optional<Tracking> trackingOptional = trackingRepository.findByElderlyElderlyID(elderlyId);
        Tracking tracking = trackingOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tracking information not found for elderly with ID: " + elderlyId));

        switch (scenario) {
            case "successfulJourney":
                simulationService.simulateSuccessfulJourney(tracking);
                break;
            case "lost":
                simulationService.simulateLost(tracking);
                break;
            default:
                throw new IllegalArgumentException("Invalid scenario: " + scenario);
        }

        return tracking;
    }*/




    /*@GetMapping("/elderly/{elderlyId}")
    public ResponseEntity<Tracking> getTrackingByElderlyId(@PathVariable long elderlyId) {
        Optional<Tracking> tracking = trackingService.getTrackingByElderlyId(elderlyId);
        return tracking.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/elderly/{elderlyId}")
    public ResponseEntity<Tracking> addTracking(@PathVariable long elderlyId, @RequestBody Tracking tracking) {
        Tracking createdTracking = trackingService.addTracking(tracking, elderlyId);
        return new ResponseEntity<>(createdTracking, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tracking> updateTracking(@PathVariable("id") long id, @RequestBody Tracking tracking) {
        if (!trackingService.getTrackingById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        tracking.setId(id);
        Tracking updatedTracking = trackingService.updateTracking(tracking);
        return new ResponseEntity<>(updatedTracking, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTracking(@PathVariable("id") long id) {
        if (!trackingService.getTrackingById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        trackingService.deleteTracking(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }*/
}
