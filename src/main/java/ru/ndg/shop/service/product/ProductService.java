package ru.ndg.shop.service.product;

import org.springframework.data.domain.Page;
import ru.ndg.shop.dto.ProductDto;

import java.util.Map;

public interface ProductService {

    Page<ProductDto> getAllProducts(Map<String, String> params, Integer page);
    ProductDto getProductById(Long id);
    void saveProduct(ProductDto productDto);
    void updateProduct(ProductDto productDto);
    void deleteProduct(Long id);
}
