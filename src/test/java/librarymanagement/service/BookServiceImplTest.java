package librarymanagement.service;

import librarymanagement.dto.BookDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test") // This notation is for using resources in test (h2 database)
@Sql(scripts = "/setup.sql")
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private DataSource dataSource;

    @Test
    void testDatasource() throws Exception {
        System.out.println("Datasource: " + dataSource.getConnection().getMetaData().getURL());
    }

    @Test
    void testGetAllBooks() {
        Pageable pageable = PageRequest.of(0, 2); // Lấy trang đầu tiên, mỗi trang 2 bản ghi
        Page<BookDTO> books = bookService.getAllBooks(pageable);

        assertNotNull(books);
        assertEquals(2, books.getContent().size()); // Có 2 bản ghi trong trang đầu tiên
        assertEquals(3, books.getTotalElements()); // Tổng số bản ghi là 3
    }

    @Test
    void testGetBookByIsbn() {
        String isbn = "9780134685991";
        BookDTO book = bookService.getBookByIsbn(isbn);

        assertNotNull(book);
        assertEquals("Effective Java", book.getTitle());
        assertEquals(isbn, book.getIsbn());
    }

    @Test
    void testAddBook() {
        BookDTO newBook = new BookDTO();
        newBook.setIsbn("9780000000004");
        newBook.setTitle("New Book");
        newBook.setAuthor("New Author");
        newBook.setPublisher("New Publisher");
        newBook.setYearPublished(2021);
        newBook.setGenre("Adventure");
        newBook.setAvailable(true);

        bookService.addBook(newBook);

        BookDTO addedBook = bookService.getBookByIsbn("9780000000004");
        assertNotNull(addedBook);
        assertEquals("New Book", addedBook.getTitle());
    }

    @Test
    void testUpdateBook() {
        String isbn = "9780134685991";
        BookDTO updatedBook = new BookDTO();
        updatedBook.setTitle("Updated Book");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setPublisher("Updated Publisher");
        updatedBook.setYearPublished(2022);
        updatedBook.setGenre("Updated Genre");
        updatedBook.setAvailable(false);

        bookService.updateBook(isbn, updatedBook);

        BookDTO result = bookService.getBookByIsbn(isbn);
        assertNotNull(result);
        assertEquals("Updated Book", result.getTitle());
        assertEquals("Updated Author", result.getAuthor());
    }

    @Test
    void testDeleteBook() {
        String isbnToDelete = "9780321356680";
        BookDTO book = bookService.getBookByIsbn(isbnToDelete);;
        assertNotNull(book, "Book with ISBN " + isbnToDelete + " should exist before deletion");
        bookService.deleteBook(isbnToDelete);

        Exception exception = assertThrows(Exception.class, () -> bookService.getBookByIsbn(isbnToDelete));
        assertTrue(exception.getMessage().contains("not found"));
    }
}
