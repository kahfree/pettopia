package a2.service;


import a2.model.Orders;
import a2.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;

    public List<Orders> getOrdersByCustomerID(int id){ return orderRepo.findOrdersByCustomerID(id);}
    public Orders getOrderForInvoiceByID(int id){ return orderRepo.findOrderForInvoiceByID(id);}
    public Optional<Orders> findOrder(int id){ return orderRepo.findById(id);}
}
