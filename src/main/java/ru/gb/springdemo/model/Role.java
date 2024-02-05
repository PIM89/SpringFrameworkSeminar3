package ru.gb.springdemo.model;


// Реализовать таблицы User(id, name, password) и Role(id, name), связанные многие ко многим

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "users_role",
            joinColumns=  @JoinColumn(name="role_id"),
            inverseJoinColumns= @JoinColumn(name="user_id"))
    private List<UserApp> userList;

    public Role(String name) {
        this.name = name;
    }
}
