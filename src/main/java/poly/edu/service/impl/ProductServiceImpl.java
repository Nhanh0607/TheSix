package poly.edu.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import poly.edu.entity.Product;
import poly.edu.repository.ProductDAO;
import poly.edu.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductDAO pdao;

    @Override
    public List<Product> findAll() {
        return pdao.findAll();
    }

    @Override
    public Product findById(Integer id) {
        return pdao.findById(id).get();
    }

    @Override
    public List<Product> findByCategoryId(String cid) {
        return pdao.findByCategoryId(cid);
    }

    // --- TRIỂN KHAI 2 HÀM MỚI ---
    @Override
    public Page<Product> findAll(Pageable pageable) {
        return pdao.findAll(pageable);
    }

    @Override
    public Page<Product> findByCategoryId(String cid, Pageable pageable) {
        return pdao.findByCategoryId(cid, pageable);
    }
}