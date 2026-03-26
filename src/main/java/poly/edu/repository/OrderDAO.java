package poly.edu.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import poly.edu.entity.Order;

public interface OrderDAO extends JpaRepository<Order, Long> {
    
    // Lấy danh sách đơn hàng của người dùng theo Username
    @Query("SELECT o FROM Order o WHERE o.account.username = ?1")
    List<Order> findByUsername(String username);
}