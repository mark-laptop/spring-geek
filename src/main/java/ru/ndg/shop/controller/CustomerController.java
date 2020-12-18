package ru.ndg.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ndg.shop.dto.CustomerDto;
import ru.ndg.shop.service.customer.CustomerService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@Controller
@RequestMapping(value = "/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String showCustomersPage(Model model,
                                    @RequestParam(name = "p", required = false) Integer page,
                                    @RequestParam(required = false) Map<String, String> params,
                                    Principal principal) {
        params.put("isAdmin", Boolean.toString(isAdmin(principal)));
        params.put("principalName", principal.getName());
        model.addAttribute("customersPage", customerService.getAllCustomers(params, page));
        model.addAttribute("filters_out", params.get("filters_out"));
        return "customers";
    }

    @GetMapping(value = "/{id}")
    public String showCustomerPage(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("customer", customerService.getCustomerById(id));
        return "customer_update";
    }

    @GetMapping(value = "/new")
    public String showNewCustomerPage(Model model) {
        model.addAttribute("customer", new CustomerDto());
        return "customer_new";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteCustomer(@PathVariable(name = "id") Long id) {
        customerService.deleteCustomerById(id);
        return "redirect:/customer";
    }

    @PostMapping(value = "/update")
    public String updateCustomer(@Valid @ModelAttribute(name = "customer") CustomerDto customerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "customer_update";
        }
        customerService.updateCustomer(customerDto);
        return "redirect:/customer";
    }

    @PostMapping(value = "/new")
    public String createCustomer(@Valid @ModelAttribute(name = "customer") CustomerDto customerDto,
                                 Principal principal,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "customer_new";
        }
        String username = (principal != null) ? principal.getName() : "";
        customerDto.setUsername(username);
        customerService.saveCustomer(customerDto);
        return "redirect:/customer";
    }

    private boolean isAdmin(Principal principal) {
        for (GrantedAuthority authority : ((Authentication) principal).getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }
}
