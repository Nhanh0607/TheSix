package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import poly.edu.entity.Account;
import poly.edu.repository.AccountDAO;
import poly.edu.service.SessionService;

@Controller
public class AccountController {
    @Autowired
    AccountDAO dao;
    
    @Autowired
    SessionService session;

    // 1. Hiển thị trang hồ sơ
    @RequestMapping("/account/profile")
    public String profile(Model model) {
        Account user = session.get("user");
        // Reload lại thông tin từ DB để đảm bảo mới nhất
        Account currentUser = dao.findById(user.getUsername()).get();
        model.addAttribute("user", currentUser);
        return "account/profile";
    }

    // 2. Cập nhật thông tin cá nhân
    @PostMapping("/account/update")
    public String updateProfile(Model model, 
                                @RequestParam("fullname") String fullname,
                                @RequestParam("email") String email,
                                @RequestParam("photo") String photo) {
        Account user = session.get("user");
        
        // Validate đơn giản
        if(fullname.isEmpty() || email.isEmpty()) {
            model.addAttribute("message", "Vui lòng không để trống họ tên hoặc email!");
            model.addAttribute("user", user);
            return "account/profile";
        }

        user.setFullname(fullname);
        user.setEmail(email);
        if(!photo.isEmpty()) {
            user.setPhoto(photo);
        }
        
        dao.save(user);
        session.set("user", user); // Cập nhật lại session
        model.addAttribute("message", "Cập nhật hồ sơ thành công!");
        model.addAttribute("user", user);
        return "account/profile";
    }

    // 3. Đổi mật khẩu
    @PostMapping("/account/change-password")
    public String changePassword(Model model, 
                                 @RequestParam("currentPass") String currentPass,
                                 @RequestParam("newPass") String newPass,
                                 @RequestParam("confirmPass") String confirmPass) {
        Account user = session.get("user");
        model.addAttribute("user", user); // Giữ lại thông tin user để hiển thị

        // BẮT LỖI NGƯỜI DÙNG NHẬP LIỆU
        if(!currentPass.equals(user.getPassword())) {
            model.addAttribute("error_pass", "Mật khẩu hiện tại không đúng!");
            return "account/profile";
        }
        
        if(newPass.isEmpty() || newPass.length() < 3) {
            model.addAttribute("error_pass", "Mật khẩu mới phải từ 3 ký tự trở lên!");
            return "account/profile";
        }
        
        if(!newPass.equals(confirmPass)) {
            model.addAttribute("error_pass", "Xác nhận mật khẩu mới không khớp!");
            return "account/profile";
        }

        // Nếu mọi thứ ok
        user.setPassword(newPass);
        dao.save(user);
        model.addAttribute("message_pass", "Đổi mật khẩu thành công!");
        
        return "account/profile";
    }
}