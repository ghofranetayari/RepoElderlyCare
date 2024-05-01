package elderlycare.RestControllers;

import elderlycare.DAO.Entities.Ambulance;
import elderlycare.DAO.Entities.AmbulanceDriver;
import elderlycare.DAO.Entities.AmbulanceOwner;
import elderlycare.DAO.Entities.Elderly;
import elderlycare.Services.IServiceGlobal;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@RestController
@AllArgsConstructor
@RequestMapping({"/ambulance"})
@CrossOrigin(origins = "*")
public class Nadhir {
    IServiceGlobal iServiceGlobal;
    public static String uploadDirectory = "C:/xampp/htdocs/nadhirPI";


    @PostMapping("/add")
    public Ambulance addAmbulance(@ModelAttribute Ambulance ambulance,
                                  @RequestParam("image") MultipartFile file,
                                  @RequestParam double latitude,
                                  @RequestParam double longitude) {
        try {
            // Vérifie si le répertoire existe, sinon le crée
            Path directoryPath = Paths.get(uploadDirectory);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            // Génère un nom de fichier unique
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalFilename;

            // Chemin complet du fichier
            Path filePath = Paths.get(uploadDirectory, fileName);

            // Enregistre le fichier dans le répertoire spécifié
            Files.write(filePath, file.getBytes());

            // Définit le nom du fichier dans l'ambulance
            ambulance.setImageAmbul(fileName);
            ambulance.setArchive(true);
            ambulance.setLatitude(latitude);
            ambulance.setLongitude(longitude);
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'erreur de manière appropriée (par exemple, journalisez-la)
        }

        // Retourne l'ambulance avec le nom du fichier mis à jour
        return iServiceGlobal.ajouterAmbulance(ambulance);
    }


    @PostMapping("/add-ambulancedriver")
    public AmbulanceDriver addAmbulanceDriver(@RequestBody AmbulanceDriver ambulanceDriver) {
        return iServiceGlobal.ajouterAmbulanceDriver(ambulanceDriver);
    }

    @PostMapping("/add-ambulanceOwner")
    public AmbulanceOwner ajouterAmbulanceOwner(@RequestBody AmbulanceOwner ambulanceOwner) {
        return iServiceGlobal.ajouterAmbulanceOwner(ambulanceOwner);
    }

    @PostMapping("/adduiu")
    public Ambulance addAmbulanceDriver(@RequestBody Ambulance ambulance) {
        return iServiceGlobal.ajouterAmbulance(ambulance);
    }

    @PutMapping("/archive/{id}")
    public ResponseEntity<String> archiveAmbulance(@PathVariable("id") long ambulanceId) {
        try {
            iServiceGlobal.archiveAmbulance(ambulanceId);
            return new ResponseEntity<>("Ambulance archived successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Failed to archive ambulance: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/affiche")
    public List<Ambulance> getNonArchivedAmbulances() {
        return iServiceGlobal.getNonArchivedAmbulances();

    }

    @PutMapping("update/{id}")
    public Ambulance updateAmbulance(
            @PathVariable("id") long id,
            @ModelAttribute Ambulance updatedAmbulance,
            @RequestParam(value = "image", required = false) MultipartFile file
    ) {
        Ambulance existingAmbulance = iServiceGlobal.getAmbulanceByAmbulanceID(id);
        if (existingAmbulance != null) {
            try {
                // Vérifie si un nouveau fichier image a été téléchargé
                if (file != null && !file.isEmpty()) {
                    // Vérifie si le répertoire existe, sinon le crée
                    Path directoryPath = Paths.get(uploadDirectory);
                    if (!Files.exists(directoryPath)) {
                        Files.createDirectories(directoryPath);
                    }

                    // Génère un nom de fichier unique
                    String originalFilename = file.getOriginalFilename();
                    String fileName = UUID.randomUUID().toString() + "_" + originalFilename;

                    // Chemin complet du fichier
                    Path filePath = Paths.get(uploadDirectory, fileName);

                    // Enregistre le fichier dans le répertoire spécifié
                    Files.write(filePath, file.getBytes());

                    // Met à jour le nom du fichier dans l'objet Ambulance
                    existingAmbulance.setImageAmbul(fileName);
                    existingAmbulance.setArchive(true);
                }

                // Mettre à jour les autres champs de l'ambulance avec les valeurs fournies
                existingAmbulance.setLocation(updatedAmbulance.getLocation());
                existingAmbulance.setStatus(updatedAmbulance.getStatus());
                // Mettre à jour d'autres attributs si nécessaire

                // Enregistrer les modifications dans la base de données
                return iServiceGlobal.updateAmbulance(existingAmbulance);
            } catch (IOException e) {
                e.printStackTrace(); // Gérer l'erreur de manière appropriée (par exemple, journalisez-la)
                // Retourner une réponse d'erreur si nécessaire
            }
        } else {
            // Gérer le cas où l'ambulance avec l'ID spécifié n'existe pas
            // Retourner une réponse d'erreur si nécessaire
        }
        return null;
    }

    @PostMapping("/ambulance/{ambulanceId}/driver/{driverId}")
    public void assignAmbulanceToDriver(@PathVariable long ambulanceId, @PathVariable long driverId) {
        iServiceGlobal.assignAmbulanceToDriver(ambulanceId, driverId);
    }

    @GetMapping("aff/{ambulanceID}")
    public Ambulance getAmbulanceByAmbulanceID(@PathVariable("ambulanceID") long ambulanceID) {
        return iServiceGlobal.getAmbulanceByAmbulanceID(ambulanceID);
    }

    @GetMapping("/{location}")
    public ResponseEntity<Ambulance> getAmbulanceByLocation(@PathVariable String location) {
        Ambulance ambulance = iServiceGlobal.getAmbulanceByLocation(location);
        if (ambulance != null) {
            return new ResponseEntity<>(ambulance, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{ambulanceId}/drivers")
    public ResponseEntity<String> assignDriverToAmbulance(@PathVariable long ambulanceId, @RequestBody AmbulanceDriver driver) {
        try {
            iServiceGlobal.assignDriverToAmbulance(ambulanceId, driver);
            return ResponseEntity.ok("Driver assigned to ambulance successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to assign driver to ambulance: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/toggleStatus")
    public ResponseEntity<Ambulance> toggleAmbulanceStatus(@PathVariable long id) {
        Ambulance updatedAmbulance = iServiceGlobal.updateAmbulanceStatus(id);
        return new ResponseEntity<>(updatedAmbulance, HttpStatus.OK);
    }

    @GetMapping("/available")
    public List<Ambulance> getAvailableAmbulances() {
        return iServiceGlobal.getAvailableAmbulances();
    }


    @GetMapping("/driver")
    public List<AmbulanceDriver> getAllAmbulanceDrivers() {
        return iServiceGlobal.getAllAmbulanceDrivers();
    }

    @GetMapping("/elderly/{relativeId}")
    public ResponseEntity<?> getElderlyByRelativeId(@PathVariable Long relativeId) {
        try {
            Elderly elderly = iServiceGlobal.getElderlyByRelativeId(relativeId);
            return new ResponseEntity<>(elderly, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("driver/aff/{id}")
    public ResponseEntity<AmbulanceDriver> getAmbulanceDriverById(@PathVariable("id") long id) {
        // Récupérer le conducteur d'ambulance par son identifiant
        return iServiceGlobal.getAmbulanceDriverById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PutMapping("/{driverId}/update-on-duty")
    public ResponseEntity<String> updateOnDutyStatus(@PathVariable long driverId) {
        iServiceGlobal.updateOnDutyStatus(driverId);
        return ResponseEntity.status(HttpStatus.OK).body("Driver onDuty status updated successfully");
    }

    @GetMapping("/onduty")
    public ResponseEntity<List<AmbulanceDriver>> getOnDutyDrivers() {
        List<AmbulanceDriver> onDutyDrivers = iServiceGlobal.getOnDutyDrivers();
        return new ResponseEntity<>(onDutyDrivers, HttpStatus.OK);
    }

    @PostMapping("/{id}/total-for-driver")
    public ResponseEntity<AmbulanceDriver> addTotalForDriverToDriver(
            @PathVariable("id") long driverId,
            @RequestBody Float total
    ) {
        AmbulanceDriver updatedDriver = iServiceGlobal.addTotalForDriver(driverId, total);
        return new ResponseEntity<>(updatedDriver, HttpStatus.OK);
    }



    // Méthode pour détecter la région des yeux dans l'image


    @PatchMapping("/{elderlyId}/tracking")
    public ResponseEntity<?> addTrackingValueToElderly(@PathVariable Long elderlyId, @RequestBody String trackingValue) {
        Elderly elderly = iServiceGlobal.addTrackingToElderly(elderlyId, trackingValue);
        if (elderly != null) {
            return ResponseEntity.ok(elderly);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Elderly not found with ID: " + elderlyId);
        }
    }
    @GetMapping("/{elderlyId}/trackingelderly")
    public ResponseEntity<?> getTrackingValueOfElderly(@PathVariable Long elderlyId) {
        String trackingValue = iServiceGlobal.getTrackingOfElderly(elderlyId);
        if (trackingValue != null) {
            return ResponseEntity.ok(trackingValue);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tracking value not found for Elderly with ID: " + elderlyId);
        }
    }
    @GetMapping("/{relativeId}/elderlyId")
    public ResponseEntity<Long> getElderlyIdByRelativeId(@PathVariable Long relativeId) {
        Long elderlyId = iServiceGlobal.getElderlyIdByRelativeId(relativeId);
        if (elderlyId != null) {
            return ResponseEntity.ok(elderlyId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}