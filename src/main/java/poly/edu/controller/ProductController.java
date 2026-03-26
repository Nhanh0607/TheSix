package poly.edu.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import poly.edu.entity.Category;
import poly.edu.entity.Product;
import poly.edu.repository.CategoryDAO;
import poly.edu.repository.ProductDAO;

@Controller
public class ProductController {

    // Tiêm các DAO để lấy dữ liệu từ Database
    @Autowired
    ProductDAO pdao;

    @Autowired
    CategoryDAO cdao;

    // HÀM XỬ LÝ KHI BẤM VÀO LOGO HOẶC TRANG CHỦ
    @RequestMapping({"/", "/home", "/index"})
    public String home() {
        // Tự động chuyển hướng về trang danh sách sản phẩm
        return "redirect:/product/list";
    }
    // 1. HÀM HIỂN THỊ DANH SÁCH SẢN PHẨM (TRANG CHỦ CỬA HÀNG)
    @RequestMapping("/product/list")
    public String list(Model model, 
                       @RequestParam("cid") Optional<String> cid, 
                       @RequestParam("p") Optional<Integer> p) {
        
        // Lấy danh sách danh mục để đổ ra thanh menu bên trái (sidebar)
        List<Category> cates = cdao.findAll();
        model.addAttribute("cates", cates);

        // Thiết lập phân trang: Lấy trang hiện tại (mặc định là 0), mỗi trang hiển thị 6 sản phẩm
        Pageable pageable = PageRequest.of(p.orElse(0), 6); 
        Page<Product> page;

        // Kiểm tra xem người dùng có đang lọc theo danh mục (cid) không
        if (cid.isPresent() && !cid.get().isEmpty()) {
            // Lọc sản phẩm theo ID của danh mục
            page = pdao.findByCategoryId(cid.get(), pageable);
            model.addAttribute("cid", cid.get()); // Gửi cid qua giao diện để highlight menu
        } else {
            // Nếu không lọc, hiển thị tất cả sản phẩm
            page = pdao.findAll(pageable);
        }

        // Gửi dữ liệu qua file list.html
        model.addAttribute("page", page);
        model.addAttribute("items", page.getContent()); // page.getContent() lấy ra danh sách sản phẩm của trang hiện tại

        return "product/list";
    }

    // 2. HÀM HIỂN THỊ CHI TIẾT 1 SẢN PHẨM
    @RequestMapping("/product/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        
        // Tìm sản phẩm trong Database theo ID
        Product item = pdao.findById(id).orElse(null);
        
        // Nếu ai đó nhập bừa một ID không tồn tại trên thanh địa chỉ, đẩy họ về trang danh sách
        if (item == null) {
            return "redirect:/product/list"; 
        }
        
        // Gửi thông tin sản phẩm tìm được qua file detail.html
        model.addAttribute("item", item);
        
        return "product/detail";
    }
}