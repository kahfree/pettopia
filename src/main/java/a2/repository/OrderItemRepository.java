package a2.repository;

import a2.model.Customer;
import a2.model.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderItemRepository extends CrudRepository<Customer, Integer> {
    @Query("Select o from OrderItem o where o.orderId.orderId = :id")
    List<OrderItem> findOrderItemsByOrderID(int id);
}
