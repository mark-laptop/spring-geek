package ru.ndg.shop.service.customer;

import org.springframework.data.domain.Page;
import ru.ndg.shop.dto.CustomerDto;

import java.util.Map;

public interface CustomerService {

    Page<CustomerDto> getAllCustomers(Map<String, String> params, Integer page);
    CustomerDto getCustomerById(Long id);
    void saveCustomer(CustomerDto customerDto);
    void updateCustomer(CustomerDto customerDto);
    void deleteCustomerById(Long id);
}
