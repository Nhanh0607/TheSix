package poly.edu.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import poly.edu.entity.Account;
import poly.edu.entity.Authority;

public interface AuthorityDAO extends JpaRepository<Authority, Integer> {
    
    // Tìm quyền của danh sách tài khoản (Hỗ trợ phân quyền)
    @Query("SELECT DISTINCT a FROM Authority a WHERE a.account IN ?1")
    List<Authority> authoritiesOf(List<Account> accounts);
}