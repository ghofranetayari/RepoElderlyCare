package Services;


import elderlycare.DAO.Entities.*;
import elderlycare.DAO.Repositories.*;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ShopService implements IShopService {

    ProductRepository productRepository;
    OrderRepository orderrRepository;
    ElderlyRepository elderlyRepository;
    CartRepository cartRepository;

    Notificationshoprepo notificationrepo ;
    private static final String ACCOUNT_SID = "your_account_sid";
    private static final String AUTH_TOKEN = "your_auth_token";



    public Set<Orderr> getElderlyCart(Long elderlyId) {
        Elderly elderly = elderlyRepository.findById(elderlyId)
                .orElseThrow(() -> new RuntimeException("Elderly not found"));

        // Retrieve the cart associated with the elderly
        Cart cart = elderly.getCarts();

        if (cart == null || cart.getOrders() == null) {
            return Collections.emptySet();
        }

        Set<Orderr> cartWithProductDetails = new HashSet<>();
        for (Orderr order : cart.getOrders()) {
            Product product = order.getProduct();
            if (product != null) {
                order.setProductName(product.getProductName());
                order.getProduct().getPrice();
            }
            cartWithProductDetails.add(order);
        }

        return cartWithProductDetails;
    }

    public void updateCartItemQuantity(Long orderId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Optional<Orderr> orderOptional = orderrRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Orderr order = orderOptional.get();
            Product product = order.getProduct();
            if (product != null) {
                double productCapacity = product.getProdCapacity();
                int intProductCapacity = (int) Math.round(productCapacity);
                if (quantity <= intProductCapacity) {
                    order.setQuantite(quantity);
                    order.setTotalPrice(order.getPrice() * quantity);
                    orderrRepository.save(order);
                } else {
                    throw new IllegalArgumentException("Requested quantity exceeds product capacity.");
                }
            } else {
                throw new RuntimeException("Product not found for order");
            }
        } else {
            throw new RuntimeException("Order not found");
        }
    }


    public Orderr addProductToOrder(Long productId, Long elderlyId) {
        Product product = getProductById(productId);
        Elderly elderly = elderlyRepository.findById(elderlyId)
                .orElseThrow(() -> new RuntimeException("Elderly not found"));

        Cart cart = elderly.getCarts();
        if (cart == null) {
            // If elderly doesn't have a cart, create a new one and associate it
            cart = new Cart();
            cart = cartRepository.save(cart);
            elderly.setCarts(cart); // Associate the cart with the elderly
        }

        // Find any existing orders for the elderly with the given product
        List<Orderr> existingOrders = cart.getOrders().stream()
                .filter(order -> order.getProduct().getProductId()==(productId))
                .collect(Collectors.toList());

        Orderr existingOrder = null;
        for (Orderr order : existingOrders) {
            if ("Pending".equals(order.getOrderStatus())) {
                existingOrder = order;
                break;
            }
        }

        if (existingOrder != null) {
            // If a pending order exists for the product, update it
            int newQuantity = existingOrder.getQuantite() + 1;
            int totalQuantityInCart = cart.getOrders().stream()
                    .filter(order -> order.getProduct().getProductId()==(productId))
                    .filter(order -> "Pending".equals(order.getOrderStatus())) // Filter pending orders
                    .mapToInt(Orderr::getQuantite)
                    .sum();

            if (newQuantity <= product.getProdCapacity() && totalQuantityInCart + 1 <= product.getProdCapacity()) {
                existingOrder.setQuantite(newQuantity); // Increment quantity
                existingOrder.setTotalPrice(existingOrder.getPrice() * newQuantity); // Recalculate total price
                existingOrder.setOrderDate(new Date()); // Update order date

                // Save the updated order
                existingOrder = orderrRepository.save(existingOrder);
            } else {
                throw new RuntimeException("Product capacity exceeded for product ID: " + productId);
            }
        } else {
            // Otherwise, create a new order for the product
            if (product.getProdCapacity() >= 1) { // Check if product has capacity
                Orderr order = new Orderr();
                order.setOrderDate(new Date()); // Set order date to current date
                order.setPrice(product.getPrice()); // Set price per unit
                order.setQuantite(1); // Set default quantity to 1
                order.setTotalPrice(product.getPrice()); // Initialize total price with price
                order.setProduct(product); // Associate the product with the order
                order.setOrderStatus("Pending"); // Set order status

                // Associate the order with the cart
                order.setCartss(cart);

                // Add the order to the cart
                cart.getOrders().add(order);

                // Save the order and update the elderly entity
                existingOrder = orderrRepository.save(order);
                elderlyRepository.save(elderly);
            } else {
                throw new RuntimeException("Product capacity is invalid for product ID: " + productId);
            }
        }

        return existingOrder;
    }



    public static String uploadDirectory = "C:/xamppp/htdocs/hazemimage";

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    public List<Product> searchProducts(String searchTerm) {
        if (StringUtils.hasText(searchTerm) && searchTerm.length() >= 1) {
            // If a search term is provided and has at least one character
            return productRepository.findByProductNameStartingWithIgnoreCase(searchTerm);
        } else {
            // If no search term is provided or the search term is less than one character
            // Return all products
            return productRepository.findAll();
        }
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public String getProductNameByOrderId(Long orderId) {
        Orderr order = orderrRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getProduct() != null) {
            return order.getProduct().getProductName();
        } else {
            throw new RuntimeException("Product not found for order ID: " + orderId);
        }
    }

    public Product createProduct(Product product) {




        return
                productRepository.save(product);
    }

    // ProductService.java

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    // Pattern object for compiled regex
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    // Method to check if an email address has a valid format
    public static boolean isValidEmail(String email) {
        return true;
    }

    public Product updateProduct(Long id, Product updatedProductData, MultipartFile file) throws IOException {
        Product existingProduct = getProductById(id);

        if (existingProduct.getProdCapacity() == 0 && updatedProductData.getProdCapacity() > 0) {
            // Fetch elderly email from Notificationshop entity
            List<String> elderlyEmails = fetchElderlyEmails(id);

            // Send email to each elderly user
            String subject = "Product Capacity Update Notification";
            String body = "Dear Elderly, the capacity of the product you are interested in has been updated.";

            for (String elderlyEmail : elderlyEmails) {
                // Validate the email address format
                if (!isValidEmail(elderlyEmail)) {
                    log.error("Invalid email address format: " + elderlyEmail);
                    continue; // Skip sending email to this invalid address
                }

                // Send email
                sendSimpleEmail(Collections.singletonList(elderlyEmail), subject, body);
                deleteNotificationLineForProduct(id);

            }

        }

        // Check if the capacity changed from zero to non-zero



        if (existingProduct == null) {
            return null; // Or you can throw an exception if you prefer
        }

        // Check if a new image file is provided
        if (file != null) {
            // Generate a unique file name
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalFilename;

            // Complete file path
            Path filePath = Paths.get(uploadDirectory, fileName);

            // Save the file to the specified directory
            Files.write(filePath, file.getBytes());

            // Set the new image URL in the product
            existingProduct.setImageUrl(fileName);
        }

        // Update other attributes of the product
        existingProduct.setProductName(updatedProductData.getProductName());
        existingProduct.setProdDesc(updatedProductData.getProdDesc());
        existingProduct.setPrice(updatedProductData.getPrice());
        existingProduct.setProdCapacity(updatedProductData.getProdCapacity());

        // Update the product
        return productRepository.save(existingProduct);
    }

    private void deleteNotificationLineForProduct(Long productId) {
        notificationrepo.deleteByProductId(productId);
    }



    public Product toggleProductStatus(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Toggle the status
        if (product.getArchProd().equals("Available")) {
            product.setArchProd("Not Available");
        } else {
            product.setArchProd("Available");
        }

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public void uploadImage(Long id, MultipartFile file) throws IOException {
        Product product = getProductById(id);
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file");
        }
        String uploadDirectory = "C:/xamppp/htdocs/hazemimage/";
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = uploadDirectory + fileName;
        Files.createDirectories(Paths.get(uploadDirectory));
        Files.copy(file.getInputStream(), Paths.get(filePath));
        product.setImageUrl(filePath);
        productRepository.save(product); // Save the updated product with image URL
    }

    public String getImageUrl(Long id) {
        Product product = getProductById(id);
        return product.getImageUrl();
    }


    public Orderr findById(long orderId) {
        Optional<Orderr> optionalOrder = orderrRepository.findById(orderId);
        return optionalOrder.orElse(null);
    }

    public void buyOrder(Orderr order) {
        // Update order status to "bought"
        order.setOrderStatus("bought");

        // Adjust product capacity
        double remainingCapacity = order.getProduct().getProdCapacity() - order.getQuantite();
        order.getProduct().setProdCapacity(remainingCapacity);

        // Save changes
        orderrRepository.save(order);

    }


    public void cancelOrder(Long orderId) {
        Orderr order = orderrRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Remove the association with the product
        order.setProduct(null);

        // Delete the order
        orderrRepository.deleteById(orderId);
    }
    public double getProductCapacityByOrderId(Long orderId) {
        Orderr order = orderrRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Product product = order.getProduct();
        if (product != null) {
            return product.getProdCapacity();
        } else {
            throw new RuntimeException("Product not found for order ID: " + orderId);
        }
    }


    public Page<Product> getAllProductspage(Pageable pageable) {
        return productRepository.findAllAvailableProducts(pageable);
    }



    public void saveNotification(Elderly elderly, Product product) {
        Notificationshop notification = Notificationshop.builder()
                .elderly(elderly)
                .product(product)
                .createdAt(LocalDateTime.now())
                .build();
        notificationrepo.save(notification);
    }
    private final JavaMailSender mailSender;

    public void sendSimpleEmail(List<String> toEmails, String subject, String body) {
        try {
            // Debugging: Print out the email addresses before sending
            System.out.println("Sending email to: " + toEmails);

            // Validate and process each email address in the list
            for (String toEmail : toEmails) {
                // Remove square brackets and trim any leading or trailing whitespace from the email address
                String trimmedEmail = toEmail.replaceAll("\\[|\\]", "").trim();

                // Validate the email address format
                if (!isValidEmail(trimmedEmail)) {
                    log.error("Invalid email address format: " + trimmedEmail);
                    continue; // Skip sending email to this invalid address
                }

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                // Set the sender
                helper.setFrom("your_sender_email_address");

                // Set the recipient
                helper.addTo(trimmedEmail); // Use addTo method for multiple recipients

                // Set the email subject
                helper.setSubject(subject);

                // Set the email body
                helper.setText(body);

                // Send the email
                mailSender.send(message);
                System.out.println("Mail Sent to: " + trimmedEmail);
            }
        } catch (Exception e) {
            // Log any exceptions that occur during email sending
            log.error("Error sending email: " + e.getMessage(), e);
        }
    }



    private List<String> fetchElderlyEmails(Long productId) {
        // Fetch all notifications associated with the product ID
        List<Notificationshop> notifications = notificationrepo.findByProduct_ProductId(productId);

        // Extract elderly IDs from notifications
        List<Long> elderlyIds = notifications.stream()
                .map(notification -> notification.getElderly().getElderlyID())
                .collect(Collectors.toList());

        // Retrieve email addresses of elderly using their IDs
        List<String> elderlyEmails = elderlyIds.stream()
                .map(elderlyId -> fetchElderlyEmailById(elderlyId))
                .collect(Collectors.toList());

        return elderlyEmails;
    }

    private String fetchElderlyEmailById(Long elderlyId) {
        // Fetch elderly by ID
        Elderly elderly = elderlyRepository.findById(elderlyId)
                .orElseThrow(() -> new RuntimeException("Elderly not found with ID: " + elderlyId));

        // Return email address
        return elderly.getEmail();
    }

    private static final String API_KEY = "AIzaSyBnyqs5es-NmgNqNXab-o6yOAa2nV55gJ4";
    public List<Map<String, Object>> getElderlyOrderInformation() {
        return elderlyRepository.findAll().stream()
                .filter(elderly -> elderly.getCarts() != null && !elderly.getCarts().getOrders().isEmpty())
                .filter(elderly -> elderly.getCarts().getOrders().stream()
                        .anyMatch(orderr -> "bought".equalsIgnoreCase(orderr.getOrderStatus())))
                .map(this::mapToOrderInformation)
                .collect(Collectors.toList());
    }

    public Map<String, Object> mapToOrderInformation(Elderly elderly) {
        Map<String, Object> orderInformation = new HashMap<>();
        orderInformation.put("elderlyName", elderly.getFirstName() + " " + elderly.getLastName());
        orderInformation.put("phoneNumber", elderly.getPhoneNumber());
        orderInformation.put("email", elderly.getEmail());

        Cart cart = elderly.getCart();
        if (cart != null && cart.getOrders() != null) {
            List<Orderr> boughtOrders = cart.getOrders().stream()
                    .filter(orderr  -> "bought".equalsIgnoreCase(orderr.getOrderStatus())) // Filter orders with status "bought"
                    .collect(Collectors.toList());

            if (!boughtOrders.isEmpty()) {
                orderInformation.put("boughtOrders", mapToProductOrderInformation(boughtOrders));
            } else {
                orderInformation.put("boughtOrders", Collections.emptyList()); // No "boughtOrders" found
            }
        } else {
            orderInformation.put("boughtOrders", Collections.emptyList()); // No cart or orders found
        }

        return orderInformation;
    }




    public List<Map<String, Object>> mapToProductOrderInformation(List<Orderr> orders) {
        return orders.stream()
                .filter(order -> "bought".equalsIgnoreCase(order.getOrderStatus()))
                .map(this::mapToProductOrderInformation)
                .collect(Collectors.toList());
    }

    private Map<String, Object> mapToProductOrderInformation(Orderr order) {
        Map<String, Object> productOrderInformation = new HashMap<>();
        productOrderInformation.put("productName", order.getProduct().getProductName());
        productOrderInformation.put("quantity", order.getQuantite());
        productOrderInformation.put("totalPrice", order.getTotalPrice());
        return productOrderInformation;
    }
    public List<ElderlyOrderInfo> getElderlyOrderInfoWithBoughtOrders() {
        List<ElderlyOrderInfo> elderlyOrderInfoList = new ArrayList<>();

        // Fetch all elderly entities with associated carts
        List<Elderly> elderlyList = elderlyRepository.findAll();

        for (Elderly elderly : elderlyList) {
            // Check if the elderly has a cart
            Cart cart = elderly.getCarts();
            if (cart != null && cart.getOrders() != null) {
                // Iterate through the orders in the cart
                for (Orderr order : cart.getOrders()) {
                    // Check if the order status is "bought"
                    if ("bought".equalsIgnoreCase(order.getOrderStatus())) {
                        // Create ElderlyOrderInfo object to store the required information
                        ElderlyOrderInfo elderlyOrderInfo = new ElderlyOrderInfo();
                        elderlyOrderInfo.setElderlyName(elderly.getFirstName() + " " + elderly.getLastName());
                        elderlyOrderInfo.setElderlyPhone(elderly.getPhoneNumber());
                        elderlyOrderInfo.setElderlyEmail(elderly.getEmail());
                        elderlyOrderInfo.setProductName(order.getProduct().getProductName());
                        elderlyOrderInfo.setQuantity(order.getQuantite());
                        elderlyOrderInfo.setTotalPrice(order.getTotalPrice());

                        // Add ElderlyOrderInfo object to the list
                        elderlyOrderInfoList.add(elderlyOrderInfo);
                    }
                }
            }
        }

        return elderlyOrderInfoList;
    }


    @Data
    public class ElderlyOrderInfo {
        private String elderlyName;
        private String elderlyPhone;
        private String elderlyEmail;
        private String productName;
        private int quantity;
        private double totalPrice;
    }




    // Scheduled task to apply or remove discount based on the current month
    public void applyOrRemoveProductPromotion() {/*
        // Check if the current month is March or April
        Month currentMonth = LocalDate.now().getMonth();
        boolean isDiscountMonth = currentMonth == Month.MARCH || currentMonth == Month.APRIL;

        // Fetch all products
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            if (isDiscountMonth && !product.isDiscounted()) {
                // Apply discount
                applyDiscount(product);
            } else if (!isDiscountMonth && product.isDiscounted()) {
                // Remove discount
                removeDiscount(product);
            }
        }*/
    }

    // Apply discount to the product
    public void applyDiscount(Product product) {
        /*
        float discountPrice = product.getPrice() * 0.8f; // 20% discount
        product.setPrice(discountPrice);
        product.setDiscounted(true);
        productRepository.save(product);*/

    }

    // Remove discount from the product
    public void removeDiscount(Product product) {/*
        float originalPrice = product.getPrice() / 0.8f; // Restore original price
        product.setPrice(originalPrice);
        product.setDiscounted(false);
        productRepository.save(product);
   */ }
    public Product addProduct(Product product, MultipartFile file) throws IOException {
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

        // Set the file name in the product
        product.setImageUrl(fileName);

        product.setDiscounted(false);

        // Save the product to the database
        return productRepository.save(product);
    }





}