package ru.ndg.shop.service.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ndg.shop.config.mapper.MapperFacade;
import ru.ndg.shop.dto.ProductDto;
import ru.ndg.shop.exception.ProductNotFoundException;
import ru.ndg.shop.filter.ProductFilter;
import ru.ndg.shop.model.Product;
import ru.ndg.shop.repository.ProductRepository;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MapperFacade mapperFacade;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, MapperFacade mapperFacade) {
        this.productRepository = productRepository;
        this.mapperFacade = mapperFacade;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> getAllProducts(Map<String, String> params, Integer page) {

        page = correctPage(page);
        int countProducts = getCountProducts(params);
        ProductFilter productFilter = new ProductFilter(params);
        return productRepository.findAll(productFilter.getSpecification(),
                PageRequest.of(page, countProducts, productFilter.getSort()))
                .map(product -> mapperFacade.map(product, ProductDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.orElseThrow(() -> {
            String message = String.format("Product with id: %d not found in database.", id);
            log.error(message);
            return new ProductNotFoundException(message);
        });
        return mapperFacade.map(product, ProductDto.class);
    }

    @Override
    @Transactional
    public void saveProduct(ProductDto productDto) {
        productRepository.save(mapperFacade.map(productDto, Product.class));
    }

    @Override
    @Transactional
    public void updateProduct(ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(productDto.getId());
        Product productFromDB = optionalProduct.orElseThrow(() ->
                new ProductNotFoundException(String.format("Product by id: %d not found!", productDto.getId())));
        mapperFacade.mapObject(productDto, productFromDB);
        productRepository.save(productFromDB);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private int getCountProducts(Map<String, String> params) {
        int count_products = 5;
        if (params != null) {
            String countProductsParam = params.get("count_products");
            try {
                count_products = Integer.parseInt(countProductsParam);
                if (count_products < 1 || count_products > 100) {
                    count_products = 5;
                }
            } catch (NumberFormatException ignore) {}
        }
        return count_products;
    }

    private int correctPage(Integer page) {
        if(page == null || page < 1) return  0;
        return page - 1;
    }
}
