package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import poly.edu.entity.Category;
import poly.edu.repository.CategoryDAO;

@Controller
public class CategoryManagerController {
    @Autowired
    CategoryDAO dao;

    @RequestMapping("/admin/category/index")
    public String index(Model model) {
        Category item = new Category();
        model.addAttribute("item", item);
        model.addAttribute("items", dao.findAll());
        return "admin/category/index";
    }

    @RequestMapping("/admin/category/edit/{id}")
    public String edit(Model model, @PathVariable("id") String id) {
        Category item = dao.findById(id).orElse(new Category());
        model.addAttribute("item", item);
        model.addAttribute("items", dao.findAll());
        return "admin/category/index";
    }

    @RequestMapping("/admin/category/create")
    public String create(@ModelAttribute("item") Category item, RedirectAttributes params) {
        dao.save(item);
        params.addFlashAttribute("message", "Thêm mới thành công!");
        return "redirect:/admin/category/index";
    }

    @RequestMapping("/admin/category/update")
    public String update(@ModelAttribute("item") Category item, RedirectAttributes params) {
        dao.save(item);
        params.addFlashAttribute("message", "Cập nhật thành công!");
        return "redirect:/admin/category/edit/" + item.getId();
    }

    @RequestMapping("/admin/category/delete/{id}")
    public String delete(@PathVariable("id") String id, RedirectAttributes params) {
        try {
            dao.deleteById(id);
            params.addFlashAttribute("message", "Xóa thành công!");
        } catch (Exception e) {
            params.addFlashAttribute("message", "Không thể xóa loại hàng đang có sản phẩm!");
        }
        return "redirect:/admin/category/index";
    }

    @RequestMapping("/admin/category/reset")
    public String reset() {
        return "redirect:/admin/category/index";
    }
}