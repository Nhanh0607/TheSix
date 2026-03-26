package poly.edu.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.repository.OrderDAO;
import poly.edu.repository.OrderDetailDAO;
import poly.edu.service.CartService;
import poly.edu.service.OrderService;
import poly.edu.entity.Order;
import poly.edu.entity.OrderDetail;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired OrderDAO dao;
    @Autowired OrderDetailDAO ddao;
    @Autowired CartService cart; // Tiêm CartService để lấy dữ liệu giỏ hàng

    @Override
    public Order create(Order order) {
        // 1. Lưu Order chính vào DB trước để có ID
        order.setCreateDate(new java.util.Date());
        order.setStatus(0); // 0: Mới đặt
        Order savedOrder = dao.save(order);
        
        // 2. Lấy các món trong giỏ hàng chuyển thành OrderDetail
        // Duyệt qua từng món trong giỏ
        cart.getItems().forEach(item -> {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setPrice(item.getPrice());
            detail.setQuantity(item.getQty());
            
            // Set sản phẩm (Chỉ cần ID là đủ để Hibernate tự link)
            poly.edu.entity.Product p = new poly.edu.entity.Product();
            p.setId(item.getId());
            detail.setProduct(p);
            
            ddao.save(detail); // Lưu chi tiết đơn hàng
        });
        
        // 3. Xóa sạch giỏ hàng sau khi đặt xong
        cart.clear();
        
        return savedOrder;
    }

    @Override
    public List<Order> findByUsername(String username) {
        return dao.findByUsername(username);
    }

    @Override
    public Order findById(Long id) {
        return dao.findById(id).get();
    }
}