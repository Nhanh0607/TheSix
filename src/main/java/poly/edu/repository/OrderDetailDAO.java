package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import poly.edu.entity.OrderDetail;

public interface OrderDetailDAO extends JpaRepository<OrderDetail, Long> {
    @Query("SELECT d FROM OrderDetail d WHERE d.order.id = ?1")
    List<OrderDetail> findByOrderId(Long id);
}