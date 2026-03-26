package poly.edu.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "Roles")
public class Role implements Serializable {
    @Id
    String id;
    String name;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    List<Authority> authorities;
}