package librarymanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import librarymanagement.dto.BookDTO;
import librarymanagement.entity.Book;
import librarymanagement.repository.BookMapper;
import librarymanagement.util.BookConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceMockitoTest {

    @InjectMocks
    private BookServiceImpl bookService; // Lớp cần test

    @Mock
    private BookMapper bookMapper; // Mock Mapper

    @Mock
    private BookConverter bookConverter; // Mock Converter

    @Mock
    private InventoryClient inventoryClient; // Mock Inventory Client


    @Test
    void testExistsByIsbnMock() {
        Mockito.when(bookMapper.existsByIsbn(Mockito.anyString())).thenReturn(false);

        boolean result = bookMapper.existsByIsbn("9780000000001");

        assertFalse(result); // Assert the mocked behavior
        Mockito.verify(bookMapper).existsByIsbn("9780000000001");
    }

    @Test
    void testGetAllBooks() {
        // Mock các phương thức cần thiết
        List<Book> books = List.of(
                new Book("9780000000001", "Effective Java", "Joshua Bloch", "Addison-Wesley", 2018, "Programming", true),
                new Book("9780000000002", "Java Concurrency in Practice", "Brian Goetz", "Addison-Wesley", 2006, "Programming", false)
        );

        Mockito.when(bookMapper.getAllBooks()).thenReturn(books);
        Mockito.when(bookConverter.convertToDto(Mockito.any(Book.class)))
                .thenAnswer(invocation -> {
                    Book book = invocation.getArgument(0);
                    return new BookDTO(
                            book.getISBN(),
                            book.getTitle(),
                            book.getAuthor(),
                            book.getPublisher(),
                            book.getYearPublished(),
                            book.getGenre(),
                            book.isAvailable());
                });
//        Mockito.when(inventoryClient.getStock(Mockito.anyString())).thenReturn(10);
        Mockito.when((inventoryClient.getStock("9780000000001"))).thenReturn(10);
        Mockito.when(inventoryClient.getStock("9780000000002")).thenReturn(0);

        // Gọi phương thức và kiểm tra kết quả
        List<BookDTO> result = bookService.getAllBooks(false);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Effective Java", result.get(0).getTitle());
       // assertEquals("Joshua Bloch", result.get(0).getAuthor());

        // Xác minh các phương thức được gọi
        Mockito.verify(bookMapper).getAllBooks();
        Mockito.verify(inventoryClient, Mockito.times(2)).getStock(Mockito.anyString());
    }

    @Test
    void testGroupBooksByGenre() {
        // Dữ liệu thử nghiệm
        List<Book> books = List.of(
                new Book("9780000000001", "Effective Java", "Joshua Bloch", "Addison-Wesley", 2018, "Programming", true),
                new Book("9780000000002", "Clean Code", "Robert C. Martin", "Prentice Hall", 2008, "Programming", true),
                new Book("9780000000003", "The Catcher in the Rye", "J.D. Salinger", "Little, Brown", 1951, "Fiction", true)
        );

        // Mock các phụ thuộc
        Mockito.when(bookMapper.getAllBooks()).thenReturn(books);
        Mockito.when(bookConverter.convertToDto(Mockito.any(Book.class)))
                .thenAnswer(invocation -> {
                    Book book = invocation.getArgument(0);
                    return new BookDTO(
                            book.getISBN(),
                            book.getTitle(),
                            book.getAuthor(),
                            book.getPublisher(),
                            book.getYearPublished(),
                            book.getGenre(),
                            book.isAvailable()
                    );
                });







        // Gọi hàm cần test
        Map<String, List<BookDTO>> groupedBooks = bookService.groupBooksByGenre();

        // Kiểm tra kết quả
        assertNotNull(groupedBooks);
        assertEquals(2, groupedBooks.size()); // Có 2 thể loại: Programming, Fiction

        // Kiểm tra nhóm "Programming"
        List<BookDTO> programmingBooks = groupedBooks.get("Programming");
        assertNotNull(programmingBooks);
        assertEquals(2, programmingBooks.size());
        assertTrue(programmingBooks.stream().anyMatch(book -> book.getTitle().equals("Effective Java")));
        assertTrue(programmingBooks.stream().anyMatch(book -> book.getTitle().equals("Clean Code")));

        // Kiểm tra nhóm "Fiction"
        List<BookDTO> fictionBooks = groupedBooks.get("Fiction");
        assertNotNull(fictionBooks);
        assertEquals(1, fictionBooks.size());
        assertTrue(fictionBooks.stream().anyMatch(book -> book.getTitle().equals("The Catcher in the Rye")));

        // Xác minh các phương thức mock đã được gọi
        Mockito.verify(bookMapper).getAllBooks();
        Mockito.verify(bookConverter, Mockito.times(3)).convertToDto(Mockito.any(Book.class));
    }

}
