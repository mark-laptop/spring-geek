package ru.ndg.shop.service.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ndg.shop.config.mapper.MapperFacade;
import ru.ndg.shop.dto.CustomerDto;
import ru.ndg.shop.exception.CustomerNotFoundException;
import ru.ndg.shop.filter.CustomerFilter;
import ru.ndg.shop.model.Customer;
import ru.ndg.shop.repository.CustomerRepository;
import ru.ndg.shop.repository.UserRepository;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final MapperFacade mapperFacade;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, UserRepository userRepository, MapperFacade mapperFacade) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.mapperFacade = mapperFacade;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> getAllCustomers(Map<String, String> params, Integer page) {

        page = correctPage(page);
        int countCustomers = getCountCustomers(params);
        CustomerFilter customerFilter = new CustomerFilter(params);
        return customerRepository.findAll(customerFilter.getSpecification(),
                PageRequest.of(page, countCustomers, customerFilter.getSort()))
                .map(customer -> mapperFacade.map(customer, CustomerDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        Optional<Customer> optionalProduct = customerRepository.findById(id);
        Customer product = optionalProduct.orElseThrow(() -> {
            String message = String.format("Customer with id: %d not found in database.", id);
            log.error(message);
            return new CustomerNotFoundException(message);
        });
        return mapperFacade.map(product, CustomerDto.class);
    }

    @Override
    @Transactional
    public void saveCustomer(CustomerDto customerDto) {
        Customer customer = mapperFacade.map(customerDto, Customer.class);
        customer.setUser(userRepository.findByUsername(customerDto.getUsername()));
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void updateCustomer(CustomerDto customerDto) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerDto.getId());
        Customer customerFromDB = optionalCustomer.orElseThrow(() ->
                new CustomerNotFoundException(String.format("Customer by id: %d not found!", customerDto.getId())));
        mapperFacade.mapObject(customerDto, customerFromDB);
        customerRepository.save(customerFromDB);
    }

    @Override
    @Transactional
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }

    private int getCountCustomers(Map<String, String> params) {
        int countCustomers = 5;
        if (params != null) {
            String countCustomersParam = params.get("count_customers");
            try {
                countCustomers = Integer.parseInt(countCustomersParam);
                if (countCustomers < 1 || countCustomers > 100) {
                    countCustomers = 5;
                }
            } catch (NumberFormatException ignore) {}
        }
        return countCustomers;
    }

    private int correctPage(Integer page) {
        if(page == null || page < 1) return 0;
        return page - 1;
    }
}
