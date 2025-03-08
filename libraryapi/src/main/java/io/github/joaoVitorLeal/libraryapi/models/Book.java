package io.github.joaoVitorLeal.libraryapi.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "book", schema = "public")
@Data  // Incorpora: @Getters, @Setters, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor(gera um construtor com atributos que possuem modificador 'final')
@ToString(exclude = "author") // removendo author do toString()
@EntityListeners(AuditingEntityListener.class)
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isbn", length = 20, nullable = false)
    private String isbn;

    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    @Enumerated(EnumType.STRING)  // Annotation para  Enums
    @Column(name = "genre", length = 30, nullable = false)
    private BookGenre genre;

    @Column(name = "price", precision = 18, scale = 2) // 'precision' define o número total de dígitos (antes e depois do ponto decimal), e 'scale' especifica o número de casas decimais
    private BigDecimal price;

    /**
     * Mapeamento objeto-relacional.
     * Definindo o TIPO de relacionamento -> Relacionamento muitos (livros) para um (autor).
     * ________
     * Cascade:
     * O atributo "cascade" permite que todas as operações realizadas na entidade "Book" (como persistir, atualizar ou remover)
     *  sejam automaticamente aplicadas à entidade "Author" associada. Isso cria um efeito "em cascata", propagando as ações
     *  de uma entidade para outra relacionada.
     * */
    @ManyToOne(
            //cascade = CascadeType.ALL // Efeito cascata - MENOS UTILIZADO; RISCO DE DELEÇÃO ACIDENTAL!

//            fetch = FetchType.EAGER // Vem por PADRÃO em relacionamentos do tipo @ManyToOne ou @OneToOne. Isso significa que,
                                     // ao carregar a entidade principal, as entidades relacionadas serão carregadas automaticamente no mesmo momento.

            fetch = FetchType.LAZY  // No FetchType.LAZY, apenas a entidade principal é carregada inicialmente.
                                   // As entidades relacionadas não são carregadas até que sejam explicitamente acessadas,
                                  // o que pode melhorar o desempenho ao evitar carregamentos desnecessários.
    )
    @JoinColumn(name = "id_author") // Mapeamento da coluna da relação
    private Author author;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    @Column(name = "user_id")
    private UUID userId;
}
/*
 * Outras @Annotations (Lombok):
 * @NoArgsConstructor -> Gera um construtor vazio.
 * @AllArgsConstructor ->  Gera um construtor com todos os atributos.
 * Ambas são aplicadas acima da declaração da classe.
 * */
