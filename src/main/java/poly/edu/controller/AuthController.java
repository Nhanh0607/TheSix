package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import poly.edu.entity.Account;
import poly.edu.repository.AccountDAO;
import poly.edu.service.SessionService;

import org.springframework.web.bind.annotation.RequestMapping;
import poly.edu.service.CartService;

@Controller
public class AuthController {
    @Autowired
    AccountDAO dao;
    
    @Autowired
    SessionService session;

    @Autowired
    CartService cartService;

    // --- ĐĂNG NHẬP ---
    @GetMapping("/auth/login")
    public String login(Model model, @RequestParam(value = "message", required = false) String message) {
        // Nếu có thông báo từ Interceptor gửi sang (ví dụ: Please login) thì hiển thị
        if(message != null) {
            model.addAttribute("message", message);
        }
        return "auth/login";
    }

    @PostMapping("/auth/login")
    public String login(Model model, 
                        @RequestParam("username") String username, 
                        @RequestParam("password") String password) {
        try {
            Account user = dao.findById(username).orElse(null); // Tìm user theo mã
            
            if (user == null) {
                model.addAttribute("message", "Sai tên đăng nhập!");
            } else if (!password.equals(user.getPassword())) {
                model.addAttribute("message", "Sai mật khẩu!");
            } else if (!user.getActivated()) {
                model.addAttribute("message", "Tài khoản chưa được kích hoạt!");
            } else {
                // Đăng nhập thành công
                session.set("user", user); // Lưu user vào session
                
                // --- LOGIC MỚI: KIỂM TRA TRANG TRƯỚC ĐÓ ---
                // Kiểm tra xem Interceptor có lưu lại trang nào không
                String securityUri = session.get("security-uri");
                if(securityUri != null) {
                    session.remove("security-uri"); // Xóa đi để dùng 1 lần thôi
                    return "redirect:" + securityUri; // Quay lại trang đó
                }
                // -------------------------------------------

                // Nếu là Admin thì vào trang Admin, User thì về trang chủ
                if(user.isAdmin()) {
                    return "redirect:/admin/index";
                }
                return "redirect:/"; 
            }
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi đăng nhập!");
        }
        return "auth/login"; // Ở lại trang login nếu lỗi
    }

    // --- ĐĂNG XUẤT ---
    @GetMapping("/auth/logout")
    public String logout() {
        session.remove("user"); // Xóa session
        session.remove("security-uri"); // Xóa luôn uri nếu có
        cartService.clear(); // Xóa giỏ hàng khi đăng xuất
        return "redirect:/";
    }

    // --- ĐĂNG KÝ ---
    @GetMapping("/auth/signup")
    public String signup(Model model) {
        model.addAttribute("user", new Account()); // Gửi object rỗng sang form
        return "auth/signup";
    }

    @PostMapping("/auth/signup")
    public String signup(Model model, 
                         @ModelAttribute("user") Account user,
                         @RequestParam("confirmPassword") String confirmPassword) {
        // Kiểm tra trùng username
        if(dao.existsById(user.getUsername())) {
            model.addAttribute("message", "Tên đăng nhập đã tồn tại!");
            return "auth/signup";
        }
        
        // Kiểm tra trùng Email
        if(dao.existsByEmail(user.getEmail())) {
            model.addAttribute("message", "Email này đã được sử dụng!");
            return "auth/signup";
        }

        // Kiểm tra mật khẩu xác nhận
        if(!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("message", "Xác nhận mật khẩu không khớp!");
            return "auth/signup";
        }
        
        // Mặc định kích hoạt và chưa có hình
        user.setActivated(true);
        user.setPhoto("user.png"); // Ảnh mặc định
        
        dao.save(user);
        model.addAttribute("message", "Đăng ký thành công! Mời đăng nhập.");
        return "auth/login"; // Chuyển sang trang đăng nhập
    }
}
