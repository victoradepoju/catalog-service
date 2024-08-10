package com.bookshop.catalogservice.domain;

import com.bookshop.catalogservice.domain.exception.BookAlreadyExistsException;
import com.bookshop.catalogservice.domain.exception.BookNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Iterable<Book> viewBookList() {
        return bookRepository.findAll();
    }

    public Book viewBookDetails(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
    }

    public Book addBookToCatalog(Book book) {
        if (bookRepository.existsByIsbn(book.isbn())) {
            throw new BookAlreadyExistsException(book.isbn());
        }
        return bookRepository.save(book);
    }

    public void removeBookFromCatalog(String isbn) {
        bookRepository.deleteByIsbn(isbn);
    }

    public Book editBookDetails(String isbn, Book book) {
        return bookRepository.findByIsbn(isbn)
                .map(existingBook -> {
                    var bookToUpdate = new Book(
                            existingBook.isbn(),
                            book.title() != null ? book.title() : existingBook.title(),
                            book.author() != null ? book.author() : existingBook.author(),
                            book.price() != null ? book.price() : existingBook.price()
                    );
                    return bookRepository.save(bookToUpdate);
                })
                // When changing the details for a book that does not exist in the catalog, add it to the catalog
                .orElseGet(() -> addBookToCatalog(book));
    }
}
