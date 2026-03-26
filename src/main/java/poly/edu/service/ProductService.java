package poly.edu.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import poly.edu.entity.Product;

public interface ProductService {
    // Các hàm cơ bản cũ
    List<Product> findAll();
    Product findById(Integer id);
    List<Product> findByCategoryId(String cid);

    // --- CÁC HÀM MỚI CHO PHÂN TRANG ---
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByCategoryId(String cid, Pageable pageable);
}