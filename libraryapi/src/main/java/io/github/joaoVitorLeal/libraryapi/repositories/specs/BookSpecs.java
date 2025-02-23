package io.github.joaoVitorLeal.libraryapi.repositories.specs;

import io.github.joaoVitorLeal.libraryapi.models.Book;
import io.github.joaoVitorLeal.libraryapi.models.BookGenre;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecs {

    // isbn, titulo, nome autor, genero, ano de publicacao
    // WHERE isbn = :isbn
    public static Specification<Book> isbnEqual(String isbn) {
        return (root, query, cb) -> cb.equal(root.get("isbn"), isbn);
    }

    public static Specification<Book> titleLike (String title) {
        return (root, query, cb) ->
                cb.like( cb.upper( root.get("title")), "%" + title.toUpperCase() + "%");
    }

    public static Specification<Book> genreEqual(BookGenre genre) {
        return (root, query, cb) -> cb.equal(root.get("genre"), genre);
    }

    public static Specification<Book> publicationYearEqual(Integer publicationYear) {
        // and to_char(publication_date, 'YYYY') = :publication_date;
        return (root, query, cb) ->
                cb.equal( cb.function("to_char", String.class, root.get("publicationDate"), cb.literal("YYYY")), publicationYear.toString());
    }

    public static Specification<Book> authorNameLike (String authorName) {
        return (root, query, cb) -> {
            Join<Object, Object> joinAuthor = root.join("author", JoinType.LEFT);
            return cb.like( cb.upper(joinAuthor.get("name")), "%" + authorName.toUpperCase() + "%" );
        };
    }

    @Deprecated
    public static Specification<Book> easyAuthorNameLike (String authorName) {
        return (root, query, cb) -> {
            return cb.like( cb.upper(root.get("author").get("name")),  "%" + authorName.toUpperCase() + "%" );
        };
    }

}
