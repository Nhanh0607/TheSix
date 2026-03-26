package poly.edu.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity 
@Table(name = "Orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    String address;
    
    String phone; 
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreateDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd") 
    Date createDate = new Date();
    
    @Column(name = "Status")
    Integer status = 0; 
    
    @ManyToOne
    @JoinColumn(name = "Username")
    Account account;
    
    @OneToMany(mappedBy = "order")
    List<OrderDetail> orderDetails;
}