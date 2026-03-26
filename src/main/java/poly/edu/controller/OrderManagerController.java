package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import poly.edu.entity.Order;
import poly.edu.repository.OrderDAO;
import poly.edu.repository.OrderDetailDAO;

@Controller
public class OrderManagerController {
    @Autowired
    OrderDAO dao;
    
    @Autowired
    OrderDetailDAO ddao;

    @RequestMapping("/admin/order/index")
    public String index(Model model) {
        model.addAttribute("item", new Order());
        model.addAttribute("items", dao.findAll());
        return "admin/order/index";
    }

    @RequestMapping("/admin/order/edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id) {
        Order item = dao.findById(id).orElse(new Order());
        model.addAttribute("item", item);
        model.addAttribute("items", dao.findAll());
        
        // Cố gắng load chi tiết đơn hàng (nếu lỗi thì bỏ qua để không sập trang)
        try {
             model.addAttribute("details", ddao.findByOrderId(id)); 
        } catch (Exception e) {
             // Bỏ qua nếu chưa cấu hình xong DAO chi tiết
        }
        
        return "admin/order/index";
    }

    @RequestMapping("/admin/order/update")
    public String update(@ModelAttribute("item") Order item, RedirectAttributes params) {
        // 1. Tìm đơn hàng gốc trong Database (để giữ nguyên ngày tháng, user...)
        Order orderDB = dao.findById(item.getId()).orElse(null);
        
        if (orderDB != null) {
            // 2. Chỉ cập nhật các thông tin được phép thay đổi từ Form
            orderDB.setAddress(item.getAddress());
            orderDB.setPhone(item.getPhone());
            orderDB.setStatus(item.getStatus()); // Cập nhật trạng thái
            
            // 3. Lưu lại
            dao.save(orderDB);
            params.addFlashAttribute("message", "Cập nhật đơn hàng thành công!");
        } else {
            params.addFlashAttribute("message", "Đơn hàng không tồn tại!");
        }
        
        return "redirect:/admin/order/edit/" + item.getId();
    }
    
    @RequestMapping("/admin/order/reset")
    public String reset() {
        return "redirect:/admin/order/index";
    }
}