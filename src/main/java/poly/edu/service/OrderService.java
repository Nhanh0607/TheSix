package poly.edu.service;

import java.util.List;
import poly.edu.entity.Order;

public interface OrderService {
    // Tạo đơn hàng từ thông tin người dùng nhập
    Order create(Order order);
    
    // Tìm đơn hàng của một người dùng
    List<Order> findByUsername(String username);
    
    // Tìm đơn hàng theo ID
    Order findById(Long id);
}