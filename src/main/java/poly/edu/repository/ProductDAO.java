package poly.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.entity.Product;

public interface ProductDAO extends JpaRepository<Product, Integer> {
    // 1. Tìm sản phẩm theo loại (dùng cho các chức năng cũ nếu cần)
    java.util.List<Product> findByCategoryId(String cid);

    // 2. Tìm sản phẩm theo loại có phân trang (Dùng cho trang chủ mới)
    Page<Product> findByCategoryId(String cid, Pageable pageable);
}