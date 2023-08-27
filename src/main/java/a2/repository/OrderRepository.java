package a2.repository;

import a2.model.Customer;
import a2.model.Orders;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Orders, Integer> {
    @Query("Select o from Orders o where o.customerId.customerId = :id")
    List<Orders> findOrdersByCustomerID(int id);

    @Query("Select o from Orders o where o.orderId = :id and (o.orderStatusId.status = 'Pending' or o.orderStatusId.status = 'Processing')")
    Orders findOrderForInvoiceByID(int id);
}
