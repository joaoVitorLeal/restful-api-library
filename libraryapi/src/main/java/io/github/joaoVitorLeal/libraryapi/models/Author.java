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
@ToString(exclude = "books") // excludes = Excluir propriedades para o toString()
@EntityListeners(AuditingEntityListener.class) // Utilizado para escutar/monitorar se ocorre alguma operação nesta entidade. Opéra em conjunto com as annotations @CreatedDate e @LastModifiedDate.
public class Author {                          // Para que ela funcione deve ser colocado a Annotation @EnableJpaAuditing na classe principal da aplicação ou nas configurations

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "nationality", length = 50, nullable = false)
    private String nationality;

    /**
     * Mapeamento objeto-relacional.
     * Relacionamento de um autor para muitos livros (One-to-Many).
     */
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY) // 'mappedBy' indica que não será criada uma coluna de chave estrangeira em Author que faça referência a Book.
    private List<Book> books;                              // A propriedade 'Author' na entidade 'Book' mapeia esta relação.

    @CreatedDate // Captura a data de criação
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate // Captura a data da última modificação
    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
