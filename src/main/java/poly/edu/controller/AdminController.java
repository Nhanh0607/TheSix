package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.edu.repository.AccountDAO;
import poly.edu.repository.OrderDAO;
import poly.edu.repository.ProductDAO;
import poly.edu.service.SessionService;
import poly.edu.entity.Account;

@Controller
public class AdminController {
    @Autowired ProductDAO pdao;
    @Autowired AccountDAO adao;
    @Autowired OrderDAO odao;
    @Autowired SessionService session;

    @RequestMapping("/admin/index")
    public String index(Model model) {
        // Kiểm tra quyền Admin trước khi cho vào
        Account user = session.get("user");
        if(user == null || !user.isAdmin()) {
            return "redirect:/auth/login"; // Đá về trang login nếu không phải Admin
        }

        // Lấy số liệu thống kê để hiển thị lên Dashboard
        model.addAttribute("prodCount", pdao.count());
        model.addAttribute("userCount", adao.count());
        model.addAttribute("orderCount", odao.count());
        
        // Tính tổng doanh thu (Giả lập: Lấy tất cả đơn hàng đã hoàn thành)
        // Trong thực tế sẽ dùng Query SQL SUM, ở đây tạm thời dùng size() đơn hàng làm ví dụ
        
        return "admin/dashboard";
    }
}