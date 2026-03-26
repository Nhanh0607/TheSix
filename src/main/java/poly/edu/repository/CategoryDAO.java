package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.entity.Category;

public interface CategoryDAO extends JpaRepository<Category, String> {
    // JpaRepository đã có sẵn findAll, findById, save, delete...
}