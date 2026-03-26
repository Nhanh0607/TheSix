package poly.edu.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.edu.repository.CategoryDAO;
import poly.edu.entity.Category;
import poly.edu.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryDAO cdao;

    @Override
    public List<Category> findAll() {
        return cdao.findAll();
    }
}