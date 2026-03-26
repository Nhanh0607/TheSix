package poly.edu.controller;

import java.io.File;
import java.util.List;

import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import poly.edu.entity.Product;
import poly.edu.repository.CategoryDAO;
import poly.edu.repository.ProductDAO;

@Controller
public class ProductManagerController {
    @Autowired
    ProductDAO dao;
    
    @Autowired
    CategoryDAO cdao;

    @Autowired
    ServletContext app; // Dùng để lấy đường dẫn lưu ảnh

    // 1. HIỆN DANH SÁCH VÀ FORM TRỐNG
    @RequestMapping("/admin/product/index")
    public String index(Model model) {
        Product item = new Product();
        model.addAttribute("item", item);
        model.addAttribute("items", dao.findAll());
        model.addAttribute("cates", cdao.findAll());
        return "admin/product/index";
    }

    // 2. BẤM NÚT EDIT -> ĐỔ DỮ LIỆU LÊN FORM
    @RequestMapping("/admin/product/edit/{id}")
    public String edit(Model model, @PathVariable("id") Integer id) {
        Product item = dao.findById(id).orElse(new Product());
        model.addAttribute("item", item);
        model.addAttribute("items", dao.findAll());
        model.addAttribute("cates", cdao.findAll());
        return "admin/product/index";
    }

    // 3. THÊM MỚI SẢN PHẨM
    @RequestMapping("/admin/product/create")
    public String create(Model model, 
                         @ModelAttribute("item") Product item,
                         @RequestParam("attach") MultipartFile attach,
                         RedirectAttributes params) {
        
        // 1. Xử lý ảnh (Giữ nguyên code cũ)
        if(!attach.isEmpty()){
            saveImage(attach, item);
        }

        // 2. Validate giá (Giữ nguyên code cũ)
        if(item.getPrice() != null && item.getPrice() < 0) {
            model.addAttribute("message", "Giá tiền không được nhỏ hơn 0!");
            model.addAttribute("items", dao.findAll());
            model.addAttribute("cates", cdao.findAll());
            return "admin/product/index";
        }
        
        item.setId(null); //tránh đè dữ lịu

        // 3. Lưu và thông báo
        dao.save(item);
        params.addFlashAttribute("message", "Thêm mới thành công!");
        return "redirect:/admin/product/index";
    }

    // 4. CẬP NHẬT SẢN PHẨM
    @RequestMapping("/admin/product/update")
    public String update(Model model, 
                         @ModelAttribute("item") Product item,
                         @RequestParam("attach") MultipartFile attach,
                         RedirectAttributes params) {
        
        // A. Xử lý ảnh: Chỉ lưu ảnh mới nếu người dùng có chọn file
        if(!attach.isEmpty()){
            saveImage(attach, item);
        }
        // Nếu attach rỗng, Hibernate tự động giữ nguyên giá trị image cũ nhờ vào input hidden bên View

        // B. Validate giá tiền
        if(item.getPrice() != null && item.getPrice() < 0) {
            model.addAttribute("message", "Giá tiền không được nhỏ hơn 0!");
            model.addAttribute("items", dao.findAll());
            model.addAttribute("cates", cdao.findAll());
             // Trả về trang edit để người dùng thấy lỗi
            return "admin/product/index"; 
        }
        
        // C. Lưu và thông báo
        dao.save(item);
        params.addFlashAttribute("message", "Cập nhật thành công!");
        return "redirect:/admin/product/edit/" + item.getId();
    }

    // 5. XÓA SẢN PHẨM
    @RequestMapping("/admin/product/delete/{id}")
    public String delete(Model model, 
                         @PathVariable("id") Integer id,
                         RedirectAttributes params) {
        try {
            dao.deleteById(id);
            params.addFlashAttribute("message", "Xóa thành công!");
        } catch (Exception e) {
            // Bắt lỗi khóa ngoại (Sản phẩm đã có trong OrderDetail)
            params.addFlashAttribute("message", "Không thể xóa sản phẩm này vì đã có đơn hàng liên quan!");
        }
        return "redirect:/admin/product/index";
    }
    
    // 6. LÀM MỚI FORM
    @RequestMapping("/admin/product/reset")
    public String reset(Model model) {
        return "redirect:/admin/product/index";
    }

    // --- HÀM HỖ TRỢ LƯU FILE ---
    private void saveImage(MultipartFile attach, Product item) {
        try {
            // 1. Lấy tên file gốc
            String filename = attach.getOriginalFilename();
            
            // 2. Định nghĩa đường dẫn lưu: src/main/resources/static/images
            // Lưu ý: app.getRealPath("/") trỏ đến thư mục deploy tạm của Tomcat
            File file = new File(app.getRealPath("/images/" + filename));
            
            // 3. Tạo thư mục nếu chưa có
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            
            // 4. Ghi file vào ổ cứng
            attach.transferTo(file);
            
            // 5. Gán tên file vào đối tượng Product để lưu xuống DB
            item.setImage(filename);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi lưu file: " + e.getMessage());
        }
    }
}