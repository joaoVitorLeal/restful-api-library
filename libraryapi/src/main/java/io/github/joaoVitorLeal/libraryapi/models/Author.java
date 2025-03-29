package io.github.joaoVitorLeal.libraryapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "author", schema = "public")
@Getter
@Setter
@ToString(exclude = "books")
@EntityListeners(AuditingEntityListener.class)
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(length = 50, nullable = false)
    private String nationality;

    /**
     * ORM mapping: One-to-many relationship with Book (inverse side).
     */
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Book> books;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastUpdatedAt;

    @ManyToOne
    private User user;
}
