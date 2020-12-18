package ru.ndg.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ndg.shop.dto.ProductDto;
import ru.ndg.shop.service.product.ProductService;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping(value = {"/product"})
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showProductsPage(Model model,
                                   @RequestParam(name = "p", required = false) Integer page,
                                   @RequestParam(required = false) Map<String, String> params) {
        model.addAttribute("productsPage", productService.getAllProducts(params, page));
        model.addAttribute("filters_out", params.get("filters_out"));
        return "products";
    }

    @GetMapping(value = "/{id}")
    public String showProductPage(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "product_update";
    }

    @GetMapping(value = "/new")
    public String showNewProductPage(Model model) {
        model.addAttribute("product", new ProductDto());
        return "product_new";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteProductById(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/product";
    }

    @PostMapping(value = "/update")
    public String updateProduct(@Valid @ModelAttribute(name = "product") ProductDto productDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "product_update";
        }
        productService.updateProduct(productDto);
        return "redirect:/product";
    }

    @PostMapping(value = "/new")
    public String saveProduct(@Valid @ModelAttribute(name = "product") ProductDto productDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "product_new";
        }
        productService.saveProduct(productDto);
        return "redirect:/product";
    }
}
