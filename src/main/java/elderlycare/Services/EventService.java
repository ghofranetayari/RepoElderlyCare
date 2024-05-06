package elderlycare.Services;

import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Entities.Event;
import elderlycare.DAO.Repositories.ElderlyRepository;
import elderlycare.DAO.Repositories.EventRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class EventService implements IEventService {

    public double getMaxTicketPrice() {
        return eventRepository.findMaxTicketPrice();
    }

    public int getMaxEventCapacity() {
        return eventRepository.findMaxEventCapacity();
    }
    ElderlyRepository elderlyRepository;

    public static String uploadDirectory = "C:/xamppp/htdocs/hazemimage";


    private final EventRepository eventRepository;


    // Method to add an event with an associated image
    public Event addEvent(Event event, MultipartFile file) throws IOException {
        // Check if the upload directory exists, if not, create it
        Path directoryPath = Paths.get(uploadDirectory);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        // Generate a unique file name
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;

        // Complete file path
        Path filePath = Paths.get(uploadDirectory, fileName);

        // Save the file to the specified directory
        Files.write(filePath, file.getBytes());

        // Set the file name in the event
        event.setImageUrl(fileName);

        // Get latitude and longitude from place name
        String placeName = event.getPlace();
        Map<String, Double> latLong = getLatLongFromPlaceName(placeName);
        event.setLatitude(latLong.get("latitude"));
        event.setLongitude(latLong.get("longitude"));

        // Save the event to the database
        return eventRepository.save(event);
    }


    // Method to retrieve all events from the database
    public List<Event> getAllEvents() {
        // Use the eventRepository to fetch all events from the database
        return eventRepository.findAll();
    }
    public Event updateEvent(Long eventId, Event updatedEvent, MultipartFile file) throws IOException {
        // Retrieve the existing event from the database
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Update all fields with the new values
        existingEvent.setName(updatedEvent.getName());
        existingEvent.setDate(updatedEvent.getDate());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setCapacity(updatedEvent.getCapacity());

        // Update the place name if provided
        String placeName = updatedEvent.getPlace();
        if (placeName != null && !placeName.isEmpty()) {
            existingEvent.setPlace(placeName);

            // Get latitude and longitude from updated place name
            Map<String, Double> latLong = getLatLongFromPlaceName(placeName);
            existingEvent.setLatitude(latLong.get("latitude"));
            existingEvent.setLongitude(latLong.get("longitude"));
        }

        // If a new image file is provided, update the image
        if (file != null) {
            // Generate a unique file name
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalFilename;

            // Complete file path
            Path filePath = Paths.get(uploadDirectory, fileName);

            // Save the file to the specified directory
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Set the new image URL in the event
            existingEvent.setImageUrl(fileName);
        }

        // Save the updated event to the database
        return eventRepository.save(existingEvent);
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }


    public boolean participateInEventWithPayment(Long elderlyId, Long eventId) {
        // Fetch event from the database
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        Optional<Elderly> elderlyOptional = elderlyRepository.findById(elderlyId);

        if (eventOptional.isPresent() && elderlyOptional.isPresent()) {
            Event event = eventOptional.get();
            Elderly elderly = elderlyOptional.get();
            double ticketPrice = event.getTicketprice(); // Retrieve ticket price from the event

            // Check if elderly has enough funds in compte
            if (elderly.getCompte() >= ticketPrice && event.getCapacity() > 0) {
                // Update elderly's compte
                elderly.setCompte(elderly.getCompte() - ticketPrice);
                elderlyRepository.save(elderly);

                // Decrement event capacity
                event.setCapacity(event.getCapacity() - 1);
                eventRepository.save(event);

                // Add the event to the elderly's list of participated events
                elderly.getEvents().add(event);
                elderlyRepository.save(elderly);

                return true;
            }
        }

        return false;
    }




   /* public boolean participateInEventWithPayment(Long elderlyId, Long eventId) {
        // Fetch event from the database
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            double ticketPrice = event.getTicketprice(); // Get ticket price from the event

            // Initialize Stripe with your API key
            Stripe.apiKey = "sk_test_51OEf1yIE7WmosFsXFp9azlkFtgqDMHC0wO96VoXtExTd44jxxd1P765jdwhwj8F7ObkSem4CHn2pDD6Bopm7taXj001W4LVvcp";

            try {
                // Create a payment charge directly without specifying a customer ID
                Map<String, Object> params = new HashMap<>();
                params.put("amount", (int) (ticketPrice * 100)); // Convert ticket price to cents
                params.put("currency", "usd");
                params.put("source", "tok_visa"); // Replace "tok_visa" with an actual card token if available
                params.put("description", "Charge for event participation");

                Charge charge = Charge.create(params);

                // If payment is successful, update event capacity, save association, and return true
                if (charge.getStatus().equals("succeeded")) {
                    if (decrementEventCapacity(eventId)) {
                        // Fetch elderly from the database
                        Optional<Elderly> elderlyOptional = elderlyRepository.findById(elderlyId);
                        if (elderlyOptional.isPresent()) {
                            Elderly elderly = elderlyOptional.get();
                            // Add the event to the elderly's list of participated events
                            elderly.getEvents().add(event);
                            // Save the updated elderly entity
                            elderlyRepository.save(elderly);
                            return true;
                        }
                    }
                }
            } catch (StripeException e) {
                e.printStackTrace();
            }
        }

        // If event is not found, payment fails, or elderly is not found, return false
        return false;
    }*/

    public boolean decrementEventCapacity(Long eventId) {
        // Fetch event from database
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Decrement event capacity by one if there's still capacity available
        if (event.getCapacity() > 0) {
            int updatedCapacity = event.getCapacity() - 1;
            event.setCapacity(updatedCapacity);

            // Save the updated event to the database
            eventRepository.save(event);
            return true;
        }

        // If event capacity is already full, return false
        return false;
    }

    public List<Event> getEventsForElderly(Long elderlyId) {
        Optional<Elderly> elderlyOptional = elderlyRepository.findById(elderlyId);
        if (elderlyOptional.isPresent()) {
            Elderly elderly = elderlyOptional.get();
            return elderly.getEvents();
        } else {
            // Handle case where elderly is not found
            return Collections.emptyList();
        }
    }

    public Event toggleEventArchive(Long eventId) {
        // Fetch the event from the database
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Toggle the value of the archiveevent field
        if (event.getArchiveevent().equals("not available")) {
            event.setArchiveevent("available");
        } else {
            event.setArchiveevent("not available");
        }

        // Save the updated event to the database
        return eventRepository.save(event);
    }

    private static final String NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org/search";

    // Method to get latitude and longitude from place name using Nominatim API
    public Map<String, Double> getLatLongFromPlaceName(String placeName) {
        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Build the URL with the place name as a query parameter
        String url = String.format("%s?q=%s&format=json", NOMINATIM_BASE_URL, placeName);

        // Make a GET request to the Nominatim API
        ResponseEntity<Map[]> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET.GET,
                null,
                Map[].class
        );

        // Check if the response is successful
        if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            // Extract latitude and longitude from the first result
            Map<String, Double> latLong = new HashMap<>();
            Map<String, Object> firstResult = responseEntity.getBody()[0];
            latLong.put("latitude", Double.parseDouble(firstResult.get("lat").toString()));
            latLong.put("longitude", Double.parseDouble(firstResult.get("lon").toString()));
            return latLong;
        } else {
            // Handle unsuccessful response
            throw new RuntimeException("Failed to retrieve latitude and longitude from place name.");
        }


    }

    public List<Event> getEventsWithinDistance(double userLatitude, double userLongitude, Double maxDistance, Double maxPrice, Integer minCapacity) {
        // Get all events from the database
        List<Event> allEvents = eventRepository.findAll();

        // Filter events based on distance, price, and capacity
        List<Event> eventsWithinDistance = allEvents.stream()
                .filter(event -> {
                    boolean withinDistance = maxDistance == null || maxDistance == 0 || calculateDistance(userLatitude, userLongitude, event.getLatitude(), event.getLongitude()) <= maxDistance;
                    boolean withinPrice = maxPrice == null || event.getTicketprice() <= maxPrice;
                    boolean meetsCapacity = minCapacity == null || event.getCapacity() <= minCapacity;

                    return withinDistance && withinPrice && meetsCapacity;
                })
                .collect(Collectors.toList());

        return eventsWithinDistance;
    }


    // Helper method to calculate distance between two points using Haversine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Distance in kilometers

        return distance;
    }

    private static final double PLACE_RATING = 0.5; // Adjust as needed
    private static final double MAX_TICKET_PRICE = 100.0;


    private static Map<Integer, Map<Integer, Double>> userRatings = new HashMap<>();
    private static final double DEFAULT_MAX_TICKET_PRICE = 1000.0; // Default maximum ticket price
    private static double maxTicketPrice = DEFAULT_MAX_TICKET_PRICE;

    @Autowired
    public EventService(EventRepository eventRepository, ElderlyRepository elderlyRepository) {
        this.eventRepository = eventRepository;
        this.elderlyRepository = elderlyRepository;
    }

    private static final double MAX_DISTANCE = 300.0; // Maximum distance in kilometers

    public List<Event> recommendEventsForElderly(Long elderlyId) {
        Optional<Elderly> elderlyOptional = elderlyRepository.findById(elderlyId);
        if (elderlyOptional.isPresent()) {
            Elderly elderly = elderlyOptional.get();
            double elderlyLatitude = elderly.getLatitude();
            double elderlyLongitude = elderly.getLongitude();
            double elderlyAccountBalance = elderly.getCompte();
            LocalDate currentDate = LocalDate.now();

            // Pre-filter events based on ticket price, event date, and other criteria
            List<Event> allEvents = eventRepository.findAllByTicketpriceLessThanAndDateAfter(
                    elderlyAccountBalance * 0.1, currentDate); // Adjust the method name and parameters as needed

            // Use a priority queue to maintain top events based on distance
            PriorityQueue<Event> priorityQueue = new PriorityQueue<>((e1, e2) ->
                    Double.compare(calculateDistance(elderlyLatitude, elderlyLongitude, e1.getLatitude(), e1.getLongitude()),
                            calculateDistance(elderlyLatitude, elderlyLongitude, e2.getLatitude(), e2.getLongitude())));

            // Add events to the priority queue
            for (Event event : allEvents) {
                double distance = calculateDistance(elderlyLatitude, elderlyLongitude, event.getLatitude(), event.getLongitude());
                if (distance <= MAX_DISTANCE) { // Apply dynamic distance threshold
                    priorityQueue.offer(event);
                }
            }

            // Retrieve top events from the priority queue
            List<Event> recommendedEvents = new ArrayList<>();
            int numRecommendations = Math.min(priorityQueue.size(), 5); // Limit to top 5 events
            for (int i = 0; i < numRecommendations; i++) {
                recommendedEvents.add(priorityQueue.poll());
            }

            return recommendedEvents;
        } else {
            return Collections.emptyList();
        }
    }

    // Helper method to calculate distance between two points using Haversine formula
    private double calculateDistancesssss(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Distance in kilometers
        return distance;
    }

    public List<Map<String, Object>> getEventsWithElderly() {
        List<Map<String, Object>> eventsWithElderly = new ArrayList<>();

        // Retrieve all events from the repository
        List<Event> events = eventRepository.findAll();

        // Iterate through each event to fetch associated elderly
        for (Event event : events) {
            // Fetch associated elderly for the current event
            List<Elderly> elderlyList = getElderlyForEvent(event);

            // Create a map to hold event and associated elderly
            Map<String, Object> eventWithElderly = new HashMap<>();
            eventWithElderly.put("event", event);
            eventWithElderly.put("elderlyList", elderlyList);
            eventsWithElderly.add(eventWithElderly);
        }

        return eventsWithElderly;
    }

    private List<Elderly> getElderlyForEvent(Event event) {
        List<Elderly> elderlyList = new ArrayList<>();

        // Retrieve associated elderly for the given event
        if (event != null && event.getElderlys() != null) {
            for (Elderly elderly : event.getElderlys()) {
                Elderly associatedElderly = elderlyRepository.findById(elderly.getElderlyID()).orElse(null);
                if (associatedElderly != null) {
                    elderlyList.add(associatedElderly);
                }
            }
        }

        return elderlyList;
    }

    public Resource exportEventsWithElderlyToExcel(String fileName) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("EventsWithElderly");

        // Create header row
        Row headerRow = sheet.createRow(0);
        Cell elderlyNameHeader = headerRow.createCell(0);
        elderlyNameHeader.setCellValue("Elderly Name");

        Cell eventNameHeader = headerRow.createCell(1);
        eventNameHeader.setCellValue("Event Name");

        Cell eventDateHeader = headerRow.createCell(2);
        eventDateHeader.setCellValue("Event Date");

        Cell eventTicketPriceHeader = headerRow.createCell(3);
        eventTicketPriceHeader.setCellValue("Event Ticket Price");

        // Add data rows
        int rowIndex = 1;
        List<Map<String, Object>> eventsWithElderly = getEventsWithElderly();
        for (Map<String, Object> entry : eventsWithElderly) {
            Event event = (Event) entry.get("event");
            List<Elderly> elderlyList = (List<Elderly>) entry.get("elderlyList");

            for (Elderly elderly : elderlyList) {
                Row dataRow = sheet.createRow(rowIndex++);

                Cell elderlyNameCell = dataRow.createCell(0);
                elderlyNameCell.setCellValue(elderly.getFirstName() + " " + elderly.getLastName());

                Cell eventNameCell = dataRow.createCell(1);
                eventNameCell.setCellValue(event.getName());

                Cell eventDateCell = dataRow.createCell(2);
                eventDateCell.setCellValue(event.getDate().toString());

                Cell eventTicketPriceCell = dataRow.createCell(3);
                eventTicketPriceCell.setCellValue(event.getTicketprice());
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray()) {
            @Override
            public String getFilename() {
                return "events_with_elderly.xlsx";
            }
        };

        return resource;
    }
    public double getAccountBalanceById(Long elderlyId) {
        return elderlyRepository.findCompteById(elderlyId);
    }


}




// ... (create header and data rows)

// Save the rkbook to a file
