package ru.ndg.shop.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ndg.shop.config.mapper.MapperFacade;
import ru.ndg.shop.dto.OrderDto;
import ru.ndg.shop.exception.OrderNotFoundException;
import ru.ndg.shop.filter.OrderFilter;
import ru.ndg.shop.model.Order;
import ru.ndg.shop.repository.OrderRepository;

import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final MapperFacade mapperFacade;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, MapperFacade mapperFacade) {
        this.orderRepository = orderRepository;
        this.mapperFacade = mapperFacade;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getAll(Integer page, Map<String, String> params) {
        page = correctPage(page);
        OrderFilter orderFilter = new OrderFilter(params);
        Page<Order> orderPages = orderRepository.findAll(orderFilter.getSpecification(), PageRequest.of(page, 5, orderFilter.getSort()));
        return orderPages.map(order -> mapperFacade.map(order, OrderDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Not found order by id: " + id));
        return mapperFacade.map(order, OrderDto.class);
    }

    @Override
    @Transactional
    public OrderDto save(OrderDto orderDto) {
        Order order = mapperFacade.map(orderDto, Order.class);
        orderRepository.save(order);
        return mapperFacade.map(order, OrderDto.class);
    }

    @Override
    @Transactional
    public OrderDto update(OrderDto orderDto) {
        Order order = orderRepository.findById(orderDto.getId())
                .orElseThrow(() -> new OrderNotFoundException("\"Not found order by id: " + orderDto.getId()));
        mapperFacade.map(orderDto, order);
        orderRepository.save(order);
        return mapperFacade.map(order, OrderDto.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    private Integer correctPage(Integer page) {
        if (page == null || page < 1) return 0;
        return page - 1;
    }
}
