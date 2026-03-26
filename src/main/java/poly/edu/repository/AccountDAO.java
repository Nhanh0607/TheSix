package poly.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.entity.Account;

public interface AccountDAO extends JpaRepository<Account, String> {
    // Tìm user theo email (nếu cần chức năng quên mật khẩu)
    Account findByEmail(String email);
    boolean existsByEmail(String email);
}