package a2.repository;

import a2.model.Customer;
import a2.model.Orders;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Integer>, PagingAndSortingRepository<Customer, Integer> {

    @Query("Select c.ordersCollection from Customer c where c.customerId = :id")
    List<Orders> findOrdersForInvoiceByCustomerID(int id);
}
