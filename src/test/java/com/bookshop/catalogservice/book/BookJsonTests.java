package com.bookshop.catalogservice.book;

import com.bookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookJsonTests {

    @Autowired
    private JacksonTester<Book> json;

    @Test
    void testSerialize() throws Exception {
        var book = new Book("1234567890", "Title", "Author", 9.99);
        var result = json.write(book);
        assertThat(result).hasJsonPathStringValue("@.isbn");
        assertThat(result).hasJsonPathStringValue("@.title");
        assertThat(result).hasJsonPathStringValue("@.author");
        assertThat(result).hasJsonPathNumberValue("@.price");
        assertThat(result).extractingJsonPathStringValue("@.isbn").isEqualTo(book.isbn());
        assertThat(result).extractingJsonPathStringValue("@.title").isEqualTo(book.title());
        assertThat(result).extractingJsonPathStringValue("@.author").isEqualTo(book.author());
        assertThat(result).extractingJsonPathNumberValue("@.price").isEqualTo(book.price());
    }

    @Test
    void testDeserialize() throws Exception {
        var bookJson = """
                {
                    "isbn": "1234567890",
                    "title": "Title",
                    "author": "Author",
                    "price": 9.99
                }
                """;

        assertThat(json.parse(bookJson))
                .usingRecursiveComparison()
                .isEqualTo(new Book("1234567890", "Title", "Author", 9.99));
    }
}
