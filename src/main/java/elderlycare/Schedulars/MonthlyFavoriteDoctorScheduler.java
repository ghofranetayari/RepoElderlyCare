package elderlycare.Schedulars;
import elderlycare.DAO.Entities.Doctor;
import elderlycare.DAO.Entities.Product;
import elderlycare.DAO.Repositories.DoctorRepository;
import elderlycare.DAO.Repositories.ProductRepository;
import elderlycare.Services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;

@Component

public class MonthlyFavoriteDoctorScheduler {
    @Autowired

    ProductRepository productRepository;

    @Autowired

    private DoctorService iDoctorService ;
    @Autowired

    private  DoctorRepository doctorRepository; // Assuming you have a DoctorRepository

    // Schedule the method to run at the first day of every month at 12:00 AM
    //@Scheduled(cron = "0 0 0 1 * ?")
    @Scheduled(cron = "0 * * * * ?") // Run every minute for testing

    public void updatePatientFavoriteOfTheMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1);

        List<Doctor> doctors = iDoctorService.getAllDoctors(); // Assuming you have a method to get all doctors
        Map<Doctor, Integer> monthlyRatings = iDoctorService.calculateMonthlyRatings(doctors, firstDayOfMonth);
        Doctor favoriteDoctor = iDoctorService.findDoctorWithHighestRating(monthlyRatings);

        // Update the status of the favorite doctor
        if (favoriteDoctor != null) {
            favoriteDoctor.setFavoriteOfTheMonth(true);
            doctorRepository.save(favoriteDoctor); // Assuming you have a method to save/update doctors
            // Reset the favoriteOfTheMonth flag for other doctors
            doctors.stream()
                    .filter(doctor -> !doctor.equals(favoriteDoctor))
                    .forEach(doctor -> {
                        doctor.setFavoriteOfTheMonth(false);
                        doctorRepository.save(doctor);
                    });
        }
    }
    @Scheduled(cron = "0 * * * * ?")
    public void applyOrRemoveProductPromotion() {
        System.out.println("applyOrRemoveProductPromotion started at: " + LocalDateTime.now());

        // Check if the current month is March or April
        Month currentMonth = LocalDate.now().getMonth();
        boolean isDiscountMonth = currentMonth == Month.AUGUST || currentMonth == Month.APRIL;

        System.out.println("Current month: " + currentMonth.getValue());
        System.out.println("Is discount month: " + isDiscountMonth);

        // Fetch all products
        List<Product> products = productRepository.findAll();
        System.out.println("Number of products: " + products.size());

        for (Product product : products) {
            if (isDiscountMonth) {
                // Apply 20% discount if not already discounted
                if (!product.isDiscounted()) {
                    float discount = product.getPrice() * 0.8f;
                    product.setPrice(discount);
                    product.setDiscounted(true); // Mark product as discounted
                }
            } else {
                // Check if the product was discounted during the previous discount period
                if (product.isDiscounted()) {
                    float newPrice = product.getPrice() / 0.8f;
                    // Restore price to original multiplied by 1.2
                    product.setPrice(newPrice);
                    product.setDiscounted(false); // Mark product as not discounted
                }
            }

            // Update product in the database
            productRepository.save(product);
            System.out.println("Updated product: " + product.getProductName() + " - Price: " + product.getPrice() + " - Discounted: " + product.isDiscounted());
        }

        System.out.println("applyOrRemoveProductPromotion completed at: " + LocalDateTime.now());
    }


}
