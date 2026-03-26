package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import poly.edu.entity.Account;
import poly.edu.entity.Order;
import poly.edu.repository.OrderDAO;       // Import DAO
import poly.edu.repository.OrderDetailDAO; // Import DAO
import poly.edu.service.CartService;
import poly.edu.service.OrderService;
import poly.edu.service.SessionService;

@Controller
public class OrderController {
    @Autowired OrderService orderService;
    @Autowired CartService cartService;
    @Autowired SessionService session;
    
    @Autowired OrderDAO dao;          // Tiêm OrderDAO
    @Autowired OrderDetailDAO ddao;   // Tiêm OrderDetailDAO

    // 1. Trang thanh toán (Checkout)
    @RequestMapping("/order/checkout")
    public String checkout(Model model) {
        Account user = session.get("user");
        if(user == null) {
            return "redirect:/auth/login";
        }
        
        if(cartService.getCount() == 0) {
            return "redirect:/cart/view";
        }

        Order order = new Order();
        order.setAccount(user);
        // order.setAddress(user.getEmail()); Gợi ý địa chỉ là email hoặc để trống
        
        model.addAttribute("order", order);
        model.addAttribute("cart", cartService);
        return "order/checkout";
    }

    // 2. Xử lý nút "Đặt hàng"
    @PostMapping("/order/purchase")
    public String purchase(@ModelAttribute("order") Order order, Model model) {
        Account user = session.get("user");
        order.setAccount(user);
        
        // Vẫn dùng Service để xử lý logic lưu Order + OrderDetail từ Giỏ hàng
        orderService.create(order); 
        
        return "redirect:/order/detail/" + order.getId();
    }

    // 3. Trang danh sách đơn hàng đã mua
    @RequestMapping("/order/list")
    public String list(Model model, HttpServletRequest request) {
        // Lấy User từ Session (cách bạn đang dùng)
        Account user = session.get("user");
        if(user == null) return "redirect:/auth/login";

        // SỬA: Dùng trực tiếp DAO để gọi hàm findByUsername chúng ta vừa viết
        model.addAttribute("orders", dao.findByUsername(user.getUsername()));
        
        return "order/list";
    }

    // 4. Trang chi tiết đơn hàng cụ thể
    @RequestMapping("/order/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        //  Dùng DAO để lấy dữ liệu chính xác
        model.addAttribute("order", dao.findById(id).get());
        
        // Nếu có OrderDetailDAO thì lấy thêm chi tiết
        try {
            model.addAttribute("details", ddao.findByOrderId(id));
        } catch (Exception e) {
            // Bỏ qua nếu chưa cấu hình xong DAO chi tiết
        }
        
        return "order/detail";
    }
}