package ru.ndg.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ndg.shop.dto.OrderDto;
import ru.ndg.shop.service.order.OrderService;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

@Controller
@RequestMapping(value = "/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String showOrderPage(Model model,
                                @RequestParam(name = "p", required = false) Integer page,
                                @RequestParam(required = false) Map<String, String> params,
                                Principal principal) {
        params.put("isAdmin", Boolean.toString(isAdmin(principal)));
        params.put("principalName", principal.getName());
        model.addAttribute("ordersPage", orderService.getAll(page, params));
        model.addAttribute("filters_out", params.get("filters_out"));
        return "orders";
    }

    @GetMapping(value = "{id}")
    public String showUpdateOrderPage(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("order", orderService.getById(id));
        return "order_update";
    }

    @GetMapping(value = "/new")
    public String showCreateOrderPage(Model model) {
        model.addAttribute(new OrderDto());
        return "order_new";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteOrder(@PathVariable(name = "id") Long id) {
        orderService.delete(id);
        return "redirect:/orders";
    }

    private boolean isAdmin(Principal principal) {
        Collection<? extends GrantedAuthority> authorities = ((Authentication) principal).getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }
}
