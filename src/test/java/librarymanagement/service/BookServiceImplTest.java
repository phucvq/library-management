package librarymanagement.service;

import librarymanagement.dto.BookDTO;
import librarymanagement.util.ISBNGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test") // This notation is for using resources in test (h2 database)
//@Sql(scripts = "/setup.sql")
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
    @Sql(scripts = "classpath:sql/service/setup_testGetAllBooks.sql")
    void testGetAllBooks() {
        Pageable pageable = PageRequest.of(0, 2); // Lấy trang đầu tiên, mỗi trang 2 bản ghi
        Page<BookDTO> books = bookService.getAllBooks(pageable);

        assertNotNull(books);
        assertEquals(2, books.getContent().size()); // Có 2 bản ghi trong trang đầu tiên
        assertEquals(3, books.getTotalElements()); // Tổng số bản ghi là 3
    }

    @Test
    @Sql(scripts = "classpath:sql/service/setup_testGetBookByIsbn.sql")
    void testGetBookByIsbn() {
        String isbn = "9780134685991";
        BookDTO book = bookService.getBookByIsbn(isbn);

        assertNotNull(book);
        assertEquals("Effective Java", book.getTitle());
        assertEquals(isbn, book.getIsbn());
    }

    @Test
    @Sql(scripts = "classpath:sql/service/setup_testGetBookByIsbn.sql")
    void testGetBookByGeneratedIsbn() {
        BookDTO newBook = new BookDTO();
        newBook.setTitle("Book to Retrieve");
        newBook.setAuthor("Author");
        newBook.setPublisher("Publisher");
        newBook.setYearPublished(2021);
        newBook.setGenre("Fiction");
        newBook.setAvailable(true);

        bookService.addBook(newBook);

        BookDTO addedBook = bookService.getAllBooks(false).stream()
                .filter(book -> book.getTitle().equals("Book to Retrieve"))
                .findFirst()
                .orElse(null);
        assertNotNull(addedBook, "Book to retrieve should exist");
        String generatedIsbn = addedBook.getIsbn();

        BookDTO result = bookService.getBookByIsbn(generatedIsbn);
        assertNotNull(result);
        assertEquals("Book to Retrieve", result.getTitle());
    }


    @Test
    @Sql(scripts = "classpath:sql/service/setup_testAddBook.sql")
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
    @Sql(scripts = "classpath:sql/service/setup_testAddBook.sql")
    void testAddBookWithGeneratedISBN() {
        BookDTO newBook = new BookDTO();
        newBook.setTitle("New Book");
        newBook.setAuthor("New Author");
        newBook.setPublisher("New Publisher");
        newBook.setYearPublished(2021);
        newBook.setGenre("Adventure");
        newBook.setAvailable(true);

        bookService.addBook(newBook);

        List<BookDTO> allBooks = bookService.getAllBooks(false);
        BookDTO addedBook = allBooks.stream()
                .filter(book -> book.getTitle().equals("New Book"))
                .findFirst()
                .orElse(null);

        assertNotNull(addedBook, "New book should be added");
        assertNotNull(addedBook.getIsbn(), "ISBN should be generated");
        assertEquals(13, addedBook.getIsbn().length(), "Generated ISBN should have exactly 13 characters");
    }

    @Test
    @Sql(scripts = "classpath:sql/service/setup_testUpdateBook.sql")
    void testUpdateBookWithGeneratedISBN() {
        BookDTO newBook = new BookDTO();
        newBook.setTitle("Book to Update");
        newBook.setAuthor("Author");
        newBook.setPublisher("Publisher");
        newBook.setYearPublished(2021);
        newBook.setGenre("Fiction");
        newBook.setAvailable(true);

        bookService.addBook(newBook);

        BookDTO addedBook = bookService.getAllBooks(false).stream()
                .filter(book -> book.getTitle().equals("Book to Update"))
                .findFirst()
                .orElse(null);
        assertNotNull(addedBook, "Book to update should exist");
        String generatedIsbn = addedBook.getIsbn();

        BookDTO updatedBook = new BookDTO();
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setPublisher("Updated Publisher");
        updatedBook.setYearPublished(2022);
        updatedBook.setGenre("Updated Genre");
        updatedBook.setAvailable(false);

        bookService.updateBook(generatedIsbn, updatedBook);

        BookDTO result = bookService.getBookByIsbn(generatedIsbn);
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Author", result.getAuthor());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:sql/service/setup_testUpdateBook.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:sql/service/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
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
    @Sql(scripts = "classpath:sql/service/setup_testDeleteBook.sql")
    void testDeleteBookWithGeneratedISBN() {
        BookDTO newBook = new BookDTO();
        newBook.setTitle("Book to Delete");
        newBook.setAuthor("Author");
        newBook.setPublisher("Publisher");
        newBook.setYearPublished(2021);
        newBook.setGenre("Fiction");
        newBook.setAvailable(true);

        bookService.addBook(newBook);

        BookDTO addedBook = bookService.getAllBooks(false).stream()
                .filter(book -> book.getTitle().equals("Book to Delete"))
                .findFirst()
                .orElse(null);
        assertNotNull(addedBook, "Book to delete should exist");

        String generatedIsbn = addedBook.getIsbn();
        bookService.deleteBook(generatedIsbn);

        Exception exception = assertThrows(Exception.class, () -> bookService.getBookByIsbn(generatedIsbn));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @Sql(scripts = "classpath:sql/service/setup_testDeleteBook.sql")
    void testDeleteBook() {
        String isbnToDelete = "9780321356680";
        BookDTO book = bookService.getBookByIsbn(isbnToDelete);;
        assertNotNull(book, "Book with ISBN " + isbnToDelete + " should exist before deletion");
        bookService.deleteBook(isbnToDelete);

        Exception exception = assertThrows(Exception.class, () -> bookService.getBookByIsbn(isbnToDelete));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @Sql(scripts = "classpath:sql/service/setup_testGetBookByIsbn.sql")
    void testExistsByIsbn() {
        boolean exists = bookService.isIsbnExists("9780134685991");
        assertTrue(exists);

        boolean notExists = bookService.isIsbnExists("9780000000000");
        assertFalse(notExists);
    }

    @Test
    void testGeneratedISBN() {
        String isbn = ISBNGenerator.generateISBN();
        assertNotNull(isbn, "Generated ISBN should not be null");
        assertEquals(13, isbn.length(), "ISBN should have exactly 13 characters");
        assertTrue(isbn.chars().allMatch(Character::isDigit), "ISBN should contain only digits");
    }

}
