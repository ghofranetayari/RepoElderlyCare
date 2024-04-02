package elderlycare.Services;


import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Entities.Orderr;
import elderlycare.DAO.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IShopService {

    public void applyDiscount(Product product) ;

    public void removeDiscount(Product product) ;



    public void applyOrRemoveProductPromotion() ;

    Product toggleProductStatus(Long productId) ;
    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product createProduct(Product product);

    public Product updateProduct(Long id, Product updatedProductData, MultipartFile file) throws IOException ;
    void deleteProduct(Long id);

    void uploadImage(Long id, MultipartFile file) throws IOException;

    String getImageUrl(Long id);
    public String getProductNameByOrderId(Long orderId) ;

    public void cancelOrder(Long orderId) ;
    public List<Product> searchProducts(String searchTerm) ;
    public void updateCartItemQuantity(Long orderId, int quantity) ;
    //public void buyOrder(Orderr order) ;
    public void buyOrder(Orderr order)  ;
    public Orderr findById(long orderId);
    public double getProductCapacityByOrderId(Long orderId) ;
    public Page<Product> getAllProductspage(Pageable pageable) ;
    public void saveNotification(Elderly elderly, Product product) ;
    public List<Map<String, Object>> getElderlyOrderInformation() ;

    public Map<String, Object> mapToOrderInformation(Elderly elderly) ;

    public List<Map<String, Object>> mapToProductOrderInformation(List<Orderr> orders) ;

    public Product addProduct(Product product, MultipartFile file) throws IOException ;


}