package poly.edu.controller;

import java.io.File;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import poly.edu.entity.Account;
import poly.edu.repository.AccountDAO;

@Controller
public class AccountManagerController {
    @Autowired
    AccountDAO dao;
    
    @Autowired
    ServletContext app;

    @RequestMapping("/admin/account/index")
    public String index(Model model) {
        model.addAttribute("item", new Account());
        model.addAttribute("items", dao.findAll());
        return "admin/account/index";
    }

    @RequestMapping("/admin/account/edit/{username}")
    public String edit(Model model, @PathVariable("username") String username) {
        model.addAttribute("item", dao.findById(username).orElse(new Account()));
        model.addAttribute("items", dao.findAll());
        return "admin/account/index";
    }

    @RequestMapping("/admin/account/create")
    public String create(@ModelAttribute("item") Account item, 
                         @RequestParam("photo_file") MultipartFile file,
                         RedirectAttributes params) {
        if(dao.existsById(item.getUsername())) {
            params.addFlashAttribute("message", "Username đã tồn tại!");
            return "redirect:/admin/account/index";
        }
        saveImage(file, item);
        dao.save(item);
        params.addFlashAttribute("message", "Thêm thành công!");
        return "redirect:/admin/account/index";
    }

    @RequestMapping("/admin/account/update")
    public String update(@ModelAttribute("item") Account item, 
                         @RequestParam("photo_file") MultipartFile file,
                         RedirectAttributes params) {
        if(!file.isEmpty()) saveImage(file, item);
        dao.save(item);
        params.addFlashAttribute("message", "Cập nhật thành công!");
        return "redirect:/admin/account/edit/" + item.getUsername();
    }

    @RequestMapping("/admin/account/delete/{username}")
    public String delete(@PathVariable("username") String username, RedirectAttributes params) {
        try {
            dao.deleteById(username);
            params.addFlashAttribute("message", "Xóa thành công!");
        } catch (Exception e) {
            params.addFlashAttribute("message", "Không thể xóa tài khoản đã có đơn hàng!");
        }
        return "redirect:/admin/account/index";
    }

    @RequestMapping("/admin/account/reset")
    public String reset() { return "redirect:/admin/account/index"; }

    private void saveImage(MultipartFile file, Account item) {
        if(!file.isEmpty()){
            try {
                String filename = file.getOriginalFilename();
                File saveFile = new File(app.getRealPath("/images/" + filename));
                if(!saveFile.getParentFile().exists()) saveFile.getParentFile().mkdirs();
                file.transferTo(saveFile);
                item.setPhoto(filename);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}