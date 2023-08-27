package a2.service;


import a2.model.OrderItem;
import a2.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderRepo;

    public List<OrderItem> getOrderItemsByOrderID(int id){ return orderRepo.findOrderItemsByOrderID(id);}
}
