package elderlycare.RestControllers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Entities.Event;
import elderlycare.Services.EventService;
import elderlycare.Services.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController

@RequestMapping("/events")


@CrossOrigin(origins = {"http://localhost:4200"})
public class EventRestController {

    private final EventService eventService;

    @Autowired
    public EventRestController(EventService eventService) {
        this.eventService = eventService;
    }

    // Method to add an event with an associated image
    @PostMapping("/addWithImage")
    public ResponseEntity<Event> addEventWithImage(@ModelAttribute Event event, @RequestParam("image") MultipartFile image) {
        try {
            Event createdEvent = eventService.addEvent(event, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/allevents")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok().body(events);
    }

    @PutMapping("/update/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @ModelAttribute Event updatedEvent, @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Event updatedEventResult = eventService.updateEvent(eventId, updatedEvent, image);
            if(updatedEventResult != null) {
                return ResponseEntity.ok().body(updatedEventResult);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> eventOptional = eventService.getEventById(id);
        return eventOptional
                .map(event -> ResponseEntity.ok().body(event))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/participate/{elderlyId}/{eventId}")
    public ResponseEntity<String> participateInEventWithPayment(@PathVariable Long elderlyId, @PathVariable Long eventId) {
        boolean participationSuccess = eventService.participateInEventWithPayment(elderlyId, eventId);
        if (participationSuccess) {
            return ResponseEntity.ok().body("Participation successful");
        } else {
            return ResponseEntity.badRequest().body("Participation failed or event is full");
        }
    }

    @GetMapping("/for-elderly/{elderlyId}")
    public ResponseEntity<List<Event>> getEventsForElderly(@PathVariable Long elderlyId) {
        List<Event> events = eventService.getEventsForElderly(elderlyId);
        if (!events.isEmpty()) {
            return ResponseEntity.ok().body(events);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/toggle-archive/{eventId}")
    public ResponseEntity<Event> toggleEventArchive(@PathVariable Long eventId) {
        Event toggledEvent = eventService.toggleEventArchive(eventId);
        if (toggledEvent != null) {
            return ResponseEntity.ok().body(toggledEvent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/within-distance")
    public ResponseEntity<List<Event>> getEventsWithinDistance(
            @RequestParam double userLatitude,
            @RequestParam double userLongitude,
            @RequestParam double maxDistance,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minCapacity) {
        try {
            List<Event> events = eventService.getEventsWithinDistance(userLatitude, userLongitude, maxDistance, maxPrice, minCapacity);
            return ResponseEntity.ok().body(events);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @GetMapping("/place-to-latlong")
    public ResponseEntity<Map<String, Double>> getLatLongFromPlaceName(@RequestParam String placeName) {
        try {
            Map<String, Double> latLong = eventService.getLatLongFromPlaceName(placeName);
            return ResponseEntity.ok().body(latLong);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/recommendations/{elderlyId}")
    public ResponseEntity<List<Event>> recommendEventsForElderly(@PathVariable Long elderlyId) {
        try {
            List<Event> recommendedEvents = eventService.recommendEventsForElderly(elderlyId);
            return ResponseEntity.ok().body(recommendedEvents);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/events-with-elderly")
    public ResponseEntity<List<Map<String, Object>>> getEventsWithElderly() {
        try {
            List<Map<String, Object>> eventsWithElderly = eventService.getEventsWithElderly();
            return ResponseEntity.ok().body(eventsWithElderly);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

   /* @GetMapping("/download-excel")
    public ResponseEntity<Resource> downloadExcelFile() {
        try {
            // Call the exportEventsWithElderlyToExcel method to get the resource
            Resource resource = eventService.exportEventsWithElderlyToExcel("events_with_elderly.xlsx");

            // Set headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", resource.getFilename());

            // Return ResponseEntity with the resource and headers
            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/

    @GetMapping("/download-excel")
    public ResponseEntity<Resource> downloadExcelFile() {
        try {
            // Call the exportEventsWithElderlyToExcel method to get the resource
            Resource resource = eventService.exportEventsWithElderlyToExcel("events_with_elderly.xlsx");

            // Set headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", resource.getFilename());

            // Disable caching to ensure file is downloaded every time
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());

            // Return ResponseEntity with the resource and headers
            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/max-ticket-price")
    public ResponseEntity<Double> getMaxTicketPrice() {
        double maxTicketPrice = eventService.getMaxTicketPrice();
        return new ResponseEntity<>(maxTicketPrice, HttpStatus.OK);
    }

    @GetMapping("/max-event-capacity")
    public ResponseEntity<Integer> getMaxEventCapacity() {
        int maxEventCapacity = eventService.getMaxEventCapacity();
        return new ResponseEntity<>(maxEventCapacity, HttpStatus.OK);
    }

    @GetMapping("/elderly/{id}/accountBalance")
    public double getElderlyAccountBalance(@PathVariable Long id) {
        return eventService.getAccountBalanceById(id);
    }

}




