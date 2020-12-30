package ru.ndg.shop.service.product;

import org.springframework.data.domain.Page;
import ru.ndg.shop.dto.ProductDto;

import java.util.List;
import java.util.Map;

public interface ProductService {

    Page<ProductDto> getAllProducts(Map<String, String> params, Integer page);
    ProductDto getProductById(Long id);
    ProductDto saveProduct(ProductDto productDto);
    ProductDto updateProduct(ProductDto productDto);
    void deleteProduct(Long id);
    List<ProductDto> getAllProductList();
}
