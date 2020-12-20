package ru.ndg.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ndg.shop.service.order.OrderService;

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
                                @RequestParam(required = false) Map<String, String> params) {
        model.addAttribute("ordersPage", orderService.getAll(page, params));
        model.addAttribute("filters_out", params.get("filters_out"));
        return "orders";
    }
}
