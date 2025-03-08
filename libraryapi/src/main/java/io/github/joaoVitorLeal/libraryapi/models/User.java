package io.github.joaoVitorLeal.libraryapi.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user", schema = "public")
@Data
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String login;

    @Column
    private String password;

    @Column(name = "roles")
    private List<String> roles;

    public User() {
    }

}
