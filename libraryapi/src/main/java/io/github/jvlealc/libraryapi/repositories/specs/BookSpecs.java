package io.github.jvlealc.libraryapi.repositories.specs;

import io.github.jvlealc.libraryapi.models.Book;
import io.github.jvlealc.libraryapi.models.BookGenre;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecs {

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

    /**
     * Filters books by publication year using SQL's to_char function.
     */
    public static Specification<Book> publicationYearEqual(Integer publicationYear) {
        // and to_char(publication_date, 'YYYY') = :publication_date;
        return (root, query, cb) ->
                cb.equal( cb.function("to_char", String.class, root.get("publicationDate"), cb.literal("YYYY")), publicationYear.toString());
    }


    /**
     * Filters books by author name (case-insensitive, left join).
     */
    public static Specification<Book> authorNameLike (String authorName) {
        return (root, query, cb) -> {
            Join<Object, Object> joinAuthor = root.join("author", JoinType.LEFT);
            return cb.like( cb.upper(joinAuthor.get("name")), "%" + authorName.toUpperCase() + "%" );
        };
    }

    @Deprecated // Use authorNameLike() instead for better performance.
    public static Specification<Book> easyAuthorNameLike (String authorName) {
        return (root, query, cb) -> {
            return cb.like( cb.upper(root.get("author").get("name")),  "%" + authorName.toUpperCase() + "%" );
        };
    }
}
