package librarymanagement.service;

import librarymanagement.dto.BookDTO;
import librarymanagement.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface BookService {
//    List<BookDTO> getAllBooks();
    Page<BookDTO> getAllBooks(Pageable pageable);
    List<BookDTO> getAllBooks(boolean onlyAvailable);
    Map<String, List<BookDTO>> groupBooksByGenre();
    List<BookDTO> findBooksPublishedAfter(int year);
    Map<String, Long> countBooksByPublisher();
    List<BookDTO> searchBooks(Predicate<Book> condition);
    void addBooks(List<BookDTO> books);
    String borrowBook(String isbn);

    BookDTO getBookByIsbn(String isbn);
    void addBook(BookDTO book);
    void updateBook(String isbn, BookDTO bookDto);
    void deleteBook(String isbn);
    boolean isIsbnExists(String isbn);
    String generateUniqueISBN();
    //List<BookDTO> search(@Param("keyword") String keyword);
}
