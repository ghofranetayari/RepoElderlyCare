package elderlycare.RestControllers;

import elderlycare.DAO.Entities.Ambulance;
import elderlycare.Services.IserviceNadhir;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping({"/ambulance"})
@CrossOrigin(origins="*")
public class ControllerNadhir {

    IserviceNadhir iserviceNadhir;



    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/webapp/images";

    @PostMapping("/add")
    public Ambulance addAmbulance(@ModelAttribute Ambulance ambulance, @RequestParam("image") MultipartFile file) {
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
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'erreur de manière appropriée (par exemple, journalisez-la)
        }

        // Retourne l'ambulance avec le nom du fichier mis à jour
        return iserviceNadhir.ajouterAmbulance(ambulance);
    }







    @PutMapping("/archive/{id}")
    public ResponseEntity<String> archiveAmbulance(@PathVariable("id") long ambulanceId) {
        try {
            iserviceNadhir.archiveAmbulance(ambulanceId);
            return new ResponseEntity<>("Ambulance archived successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Failed to archive ambulance: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/affiche")
    public List<Ambulance> getNonArchivedAmbulances(){
        return iserviceNadhir.getNonArchivedAmbulances();

    }
    @PutMapping("/update/{id}")
    public Ambulance updateAmbulance(
            @PathVariable("id") long id,
            @ModelAttribute Ambulance updatedAmbulance,
            @RequestParam("image") MultipartFile file
    ) {
        Ambulance existingAmbulance = iserviceNadhir.getAmbulanceByAmbulanceID(id);
        if (existingAmbulance != null) {
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
                updatedAmbulance.setImageAmbul(fileName);
                updatedAmbulance.setArchive(true);
            } catch (IOException e) {
                e.printStackTrace(); // Gérer l'erreur de manière appropriée (par exemple, journalisez-la)
            }

            // Mettre à jour les autres champs de l'ambulance si nécessaire
            existingAmbulance.setLocation(updatedAmbulance.getLocation());
            existingAmbulance.setStatus(updatedAmbulance.getStatus());

            // Enregistrer les modifications dans la base de données
            return iserviceNadhir.updateAmbulance(existingAmbulance);
        } else {
            // Gérer le cas où l'ambulance avec l'ID spécifié n'existe pas
            return null;
        }
    }
    @PostMapping("/assignAmbulanceToDriver")
    public ResponseEntity<Ambulance> assignAmbulanceToDriver(@RequestParam("ambulanceId") long ambulanceId,
                                                             @RequestParam("driverId") long driverId) {
        Ambulance assignedAmbulance = iserviceNadhir.assignAmbulanceToDriver(ambulanceId, driverId);
        if (assignedAmbulance != null) {
            return new ResponseEntity<>(assignedAmbulance, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // ou une autre réponse HTTP appropriée
        }
    }
}
