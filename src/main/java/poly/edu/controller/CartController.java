package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import poly.edu.entity.Account;
import poly.edu.service.CartService;
import poly.edu.service.SessionService;

@Controller
public class CartController {
    
    @Autowired
    CartService cart;

    // Tiêm SessionService để lấy thông tin đăng nhập
    @Autowired
    SessionService session; 
  //1. Trang xem giỏ hàng
    @RequestMapping("/cart/view")
    public String view(Model model) {
        // Tùy chọn: Có thể chặn luôn việc xem giỏ hàng nếu chưa đăng nhập
        Account user = session.get("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("cart", cart);
        return "cart/view";
    }

    //2 cách xử lý thêm/sửa/xóa giỏ hàng
    @RequestMapping("/cart/add/{id}")
    public String add(@PathVariable("id") Integer id) {
        // KIỂM TRA ĐĂNG NHẬP
        Account user = session.get("user");
        if (user == null) {
            // Trả về trang đăng nhập nếu chưa đăng nhập
            return "redirect:/auth/login"; 
        }

        cart.add(id);
        return "redirect:/cart/view"; 
    }

    @RequestMapping("/cart/remove/{id}")
    public String remove(@PathVariable("id") Integer id) {
        Account user = session.get("user");
        if (user == null) return "redirect:/auth/login";

        cart.remove(id);
        return "redirect:/cart/view";
    }

    @RequestMapping("/cart/update")
    public String update(@RequestParam("id") Integer id, 
                         @RequestParam("qty") Integer qty) {
        Account user = session.get("user");
        if (user == null) return "redirect:/auth/login";

        cart.update(id, qty);
        return "redirect:/cart/view";
    }

    @RequestMapping("/cart/clear")
    public String clear() {
        Account user = session.get("user");
        if (user == null) return "redirect:/auth/login";

        cart.clear();
        return "redirect:/cart/view";
    }
}