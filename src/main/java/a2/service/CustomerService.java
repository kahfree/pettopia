package a2.service;

import a2.model.Customer;
import a2.model.Orders;
import a2.repository.CustomerRepository;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "customer")
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepo;


    public List<Customer> findAll() { return (List<Customer>) customerRepo.findAll(); }
    public Optional<Customer> getCustomerByID(int id){ return customerRepo.findById(id);}
    public Customer addCustomer(Customer customer){ return customerRepo.save(customer);}
    public void deleteCustomer(int id){customerRepo.deleteById(id);}
    public List<Orders> findOrdersForInvoiceByCustomerID(int id){ return customerRepo.findOrdersForInvoiceByCustomerID(id);}
    public Page<Customer> findAllPageable(int pageNo, int pageSize){
        Pageable paging = PageRequest.of(pageNo,pageSize);
        return customerRepo.findAll(paging);
    }
}
