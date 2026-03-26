package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import poly.edu.entity.Account;
import poly.edu.repository.AccountDAO;
import poly.edu.service.MailerService;

@Controller
public class ForgotPasswordController {
    @Autowired
    AccountDAO dao;
    
    @Autowired
    MailerService mailer;

    @GetMapping("/auth/forgot")
    public String index() {
        return "auth/forgot";
    }

    @PostMapping("/auth/forgot")
    public String process(Model model, @RequestParam("email") String email) {
        try {
            // 1. Tìm tài khoản theo email
            Account user = dao.findByEmail(email);
            
            if(user == null) {
                model.addAttribute("message", "Email này chưa đăng ký tài khoản nào!");
            } else {
                // 2. Tạo mật khẩu mới ngẫu nhiên (6 ký tự số)
                String newPass = String.valueOf((int)(Math.random() * ((999999 - 100000) + 1)) + 100000);
                
                // 3. Cập nhật vào DB
                user.setPassword(newPass);
                dao.save(user);
                
                // 4. Gửi email
                String subject = "The Six Store - Lấy lại mật khẩu";
                String body = "Chào " + user.getFullname() + ",<br><br>"
                            + "Mật khẩu mới của bạn là: <b style='color:red'>" + newPass + "</b><br>"
                            + "Vui lòng đăng nhập và đổi lại mật khẩu ngay.<br><br>"
                            + "Trân trọng,<br>Admin.";
                
                mailer.send(email, subject, body);
                
                model.addAttribute("message", "Mật khẩu mới đã được gửi vào Email của bạn!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Lỗi gửi Email! Vui lòng thử lại sau.");
        }
        return "auth/forgot";
    }
}