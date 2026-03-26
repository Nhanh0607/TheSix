package poly.edu.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import poly.edu.entity.Product;
import poly.edu.repository.ProductDAO;
import poly.edu.service.CartItem;
import poly.edu.service.CartService;

@SessionScope 
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ProductDAO dao;

    // Dùng Map để lưu giữ hàng: Key là ID sản phẩm, Value là CartItem
    Map<Integer, CartItem> map = new HashMap<>();

    @Override
    public void add(Integer id) {
        CartItem item = map.get(id);
        if (item == null) {
            // Nếu chưa có thì lấy từ DB bỏ vào giỏ
            Product p = dao.findById(id).get();
            item = new CartItem(p.getId(), p.getName(), p.getPrice(), 1);
            map.put(id, item);
        } else {
            // Có rồi thì tăng số lượng lên 1
            item.setQty(item.getQty() + 1);
        }
    }

    @Override
    public void remove(Integer id) {
        map.remove(id);
    }

    @Override
    public void update(Integer id, int qty) {
        CartItem item = map.get(id);
        if (item != null) {
            item.setQty(qty);
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Collection<CartItem> getItems() {
        return map.values();
    }

    @Override
    public int getCount() {
        return map.values().stream()
                .mapToInt(item -> item.getQty())
                .sum();
    }

    @Override
    public double getAmount() {
        return map.values().stream()
                .mapToDouble(item -> item.getPrice() * item.getQty())
                .sum();
    }
}