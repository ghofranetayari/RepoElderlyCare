package elderlycare.RestControllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Token;
import com.stripe.param.PaymentIntentCreateParams;
import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Entities.Orderr;
import elderlycare.DAO.Entities.Product;
import elderlycare.DAO.Entities.Relative;
import elderlycare.DAO.Repositories.*;
import elderlycare.Services.EventService;
import elderlycare.Services.ShopService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@AllArgsConstructor

@CrossOrigin(origins = {"http://localhost:4200"})
class ShopRestController {
    public static String uploadDirectory = "C:/xamppp/htdocs/hazemimage";
    OrderRepository orderRepository;
    CartRepository cartRepository;
    ShopService shopserive;
    Notificationshoprepo notificationshoprepo ;
    ElderlyRepository elderlyRepository;
    ProductRepository productRepository;
    RelativeRepository relativeRepository ;
    // Load your Stripe secret key from application.properties
    private static final Logger logger = LoggerFactory.getLogger(ShopRestController.class);
    private final EventService eventService;

    @GetMapping("/elderly/{id}/accountBalance")
    public double getElderlyAccountBalance(@PathVariable Long id) {
        return eventService.getAccountBalanceById(id);
    }

    @PostMapping("/pay-relative/{relativeId}")
    public ResponseEntity<?> payRelative(@PathVariable long relativeId, @RequestParam double amount, @RequestParam String tokenId) {
        try {
            // Retrieve the relative based on the provided ID
            Relative relative = relativeRepository.findById(relativeId)
                    .orElseThrow(() -> new ChangeSetPersister.NotFoundException());

            // Retrieve the associated elderly
            Elderly elderly = relative.getElderly();

            // Initialize Stripe API key
            Stripe.apiKey = "sk_test_51OEf1yIE7WmosFsXFp9azlkFtgqDMHC0wO96VoXtExTd44jxxd1P765jdwhwj8F7ObkSem4CHn2pDD6Bopm7taXj001W4LVvcp";

            // Create payment charge using the payment token
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", (int) (amount * 100)); // Convert amount to cents
            chargeParams.put("currency", "usd");
            chargeParams.put("description", "Charge for relative payment");
            chargeParams.put("source", tokenId);

            Charge charge = Charge.create(chargeParams);

            // If payment is successful, update elderly's account and return success response
            if (charge.getStatus().equals("succeeded")) {
                double updatedCompte = elderly.getCompte() + amount;
                elderly.setCompte(updatedCompte);
                elderlyRepository.save(elderly);
                return ResponseEntity.ok("Payment successful. Amount added to elderly's account. New account balance: " + updatedCompte);
            } else {
                // If payment fails, return internal server error
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during payment.");
            }
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Relative not found with ID: " + relativeId);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during payment: " + e.getMessage());
        }
    }

    @PostMapping("/buyOrderWithElderlyAccount/{orderId}/{elderlyId}")
    public ResponseEntity<?> buyOrderWithElderlyAccount(@PathVariable  long orderId, @PathVariable long elderlyId) {
        try {
            // Retrieve the order based on the provided order ID
            Orderr order = shopserive.findById(orderId);

            // If the order doesn't exist, return a not found response
            if (order == null) {
                return ResponseEntity.notFound().build();
            }

            // Retrieve the elderly based on the provided elderly ID
            Elderly elderly = elderlyRepository.findById(elderlyId)
                    .orElseThrow(() -> new ChangeSetPersister.NotFoundException());

            // Check if the elderly has enough money in their account to buy the order
            double totalPrice = order.getTotalPrice();
            double reducedPrice = totalPrice * 0.95; // Apply 5% reduction
            if (elderly.getCompte() >= reducedPrice) {
                // Deduct the reduced order price from the elderly's account
                double updatedCompte = elderly.getCompte() - reducedPrice;
                elderly.setCompte(updatedCompte);
                elderlyRepository.save(elderly);

                // Mark the order as bought
                shopserive.buyOrder(order);

                // Return a success response
                return ResponseEntity.ok("Order bought successfully with a 5% discount");
            } else {
                // Return a response indicating insufficient funds
                return ResponseEntity.badRequest().body("You don't have enough money to buy this order");
            }
        } catch (ChangeSetPersister.NotFoundException e) {
            // Return a not found response if the elderly is not found
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Return an internal server error response for any other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to buy order: " + e.getMessage());
        }
    }





    @GetMapping("/bought")
    public List<ShopService.ElderlyOrderInfo> getElderlyOrderInfoWithBoughtOrders() {
        return shopserive.getElderlyOrderInfoWithBoughtOrders();
    }

    @PostMapping("/send-notification/{elderlyId}/{productId}")
    public void sendNotification(@PathVariable long elderlyId, @PathVariable long productId) throws ChangeSetPersister.NotFoundException {
        // Retrieve Elderly and Product objects based on the IDs provided
        Elderly elderly = elderlyRepository.findById(elderlyId).orElseThrow(() -> new ChangeSetPersister.NotFoundException());
        Product product = productRepository.findById(productId).orElseThrow(() -> new ChangeSetPersister.NotFoundException());

        // Save notification
        shopserive.saveNotification(elderly, product);
    }

    @PostMapping("/intent")

    public ResponseEntity<?> createPaymentIntent(@RequestBody Long orderId) {
        String stripeSecretKey = "sk_test_51OEf1yIE7WmosFsXFp9azlkFtgqDMHC0wO96VoXtExTd44jxxd1P765jdwhwj8F7ObkSem4CHn2pDD6Bopm7taXj001W4LVvcp";

        try {
            // Initialize Stripe API key
            Stripe.apiKey = stripeSecretKey;

            // Calculate the total amount of the payment
            Double amount = calculateTotalAmount(orderId);

            // Ensure that the total amount is not null
            if (amount == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found for ID: " + orderId);
            }

            long amountInCents = (long) (amount * 100);

            // Create payment intent with Stripe
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setCurrency("usd")
                    .setAmount(amountInCents)
                    .build();

            // Create the payment intent and log the response
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            System.out.println("Payment Intent Response: " + paymentIntent.toJson());

            // Retrieve the order and mark it as bought
            Orderr order = shopserive.findById(orderId);
            shopserive.buyOrder(order);

            // Return the client secret associated with the payment intent
            return ResponseEntity.ok(Map.of("clientSecret", paymentIntent.getClientSecret()));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating payment intent: " + e.getMessage());
        } catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found for ID: " + orderId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }


    private Double calculateTotalAmount(Long orderId) {
        try {
            return orderRepository.findTotalPriceByOrderId(orderId);
        } catch (Exception e) {
            // Log the exception
            logger.error("Error occurred while calculating total amount for order ID " + orderId, e);
            return null;
        }
    }











    @PutMapping("/updateCartItemQuantity")
    public ResponseEntity<String> updateCartItemQuantity(@RequestParam Long orderId, @RequestParam int quantity) {
        try {
            shopserive.updateCartItemQuantity(orderId, quantity);
            return ResponseEntity.ok("Quantity updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/buy/{orderId}")
    public ResponseEntity<String> buyOrder(@PathVariable long orderId) {
        try {
            Orderr order = shopserive.findById(orderId);
            if (order != null) {
                shopserive.buyOrder(order);
                return ResponseEntity.ok("Order bought successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to buy order: " + e.getMessage());
        }
    }


    @PostMapping("/addProductToOrder")
    public ResponseEntity<Orderr> addProductToOrder(@RequestParam Long productId, @RequestParam Long elderlyId) {
        Orderr order = shopserive.addProductToOrder(productId, elderlyId);
        return ResponseEntity.ok(order);
    }
    @GetMapping("/order/{orderId}/productName")
    public ResponseEntity<String> getProductNameByOrderId(@PathVariable Long orderId) {
        try {
            String productName = shopserive.getProductNameByOrderId(orderId);
            return ResponseEntity.ok(productName);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PutMapping("/{orderId}/quantity")
    public ResponseEntity<?> updateOrderQuantity(@PathVariable Long orderId, @RequestParam int quantity) {
        try {
            shopserive.updateCartItemQuantity(orderId, quantity);
            return ResponseEntity.ok("Quantity updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update quantity: " + e.getMessage());
        }
    }

    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        try {
            shopserive.cancelOrder(orderId);
            return ResponseEntity.ok("Order cancelled successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to cancel order: " + e.getMessage());
        }
    }
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String searchTerm) {
        List<Product> products = shopserive.searchProducts(searchTerm);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @GetMapping("/productspage")
    public ResponseEntity<Page<Product>> getAllProductspage(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 9); // Set page size to 9
        Page<Product> productPage = shopserive.getAllProductspage(pageable);
        return ResponseEntity.ok().body(productPage);
    }


    @GetMapping("/{elderlyId}/cart")
    public ResponseEntity<?> getElderlyCart(@PathVariable Long elderlyId) {
        try {
            Set<Orderr> cart = shopserive.getElderlyCart(elderlyId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = shopserive.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = shopserive.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/addproduct")
    public ResponseEntity<Product> createProduct(@ModelAttribute Product product, @RequestParam("image") MultipartFile file) {
        try {
            // Call the method from the ShopService to add the product with image upload
            Product createdProduct = shopserive.addProduct(product, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the error appropriately (e.g., log it)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/toggle-status")
    public ResponseEntity<Product> toggleProductStatus(@PathVariable Long id) {
        Product updatedProduct = shopserive.toggleProductStatus(id);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/products/{orderId}/capacity")
    public double getProductCapacityByOrderId(@PathVariable Long orderId) {
        return shopserive.getProductCapacityByOrderId(orderId);
    }


    // ProductController.java

    @PutMapping("/{id}/update")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @ModelAttribute Product updatedProductData, @RequestParam(value = "image", required = false) MultipartFile file) {
        try {
            Product updatedProduct = shopserive.updateProduct(id, updatedProductData, file);
            if (updatedProduct == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedProduct);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the error appropriately (e.g., log it)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        shopserive.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            shopserive.uploadImage(id, file);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/imageUrl")
    public ResponseEntity<String> getImageUrl(@PathVariable Long id) {
        // Fetch the product from the service based on the provided id
        Product product = shopserive.getProductById(id);

        // Extract the image file name from the product
        String fileName = product.getImageUrl(); // Assuming the image file name is stored in the Product entity

        // Construct the full URL of the image using the file name and your base URL
        String imageUrl = "http://localhost:8096/Hazem/image/" + fileName;

        // Return the full image URL in the response
        return ResponseEntity.ok(imageUrl);
    }

    @GetMapping("/elderly/{elderlyId}/boughtOrders")
    public ResponseEntity<List<Orderr>> getBoughtOrdersForElderly(@PathVariable Long elderlyId) {
        List<Orderr> boughtOrders = shopserive.getBoughtOrdersForElderly(elderlyId);
        if (boughtOrders.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(boughtOrders);
    }

}