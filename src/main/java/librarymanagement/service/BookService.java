package librarymanagement.service;

import librarymanagement.dto.BookDTO;
import librarymanagement.entity.Book;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
//    List<BookDTO> getAllBooks();
    Page<BookDTO> getAllBooks(Pageable pageable);
    BookDTO getBookByIsbn(String isbn);
    void addBook(BookDTO book);
    void updateBook(String isbn, BookDTO bookDto);
    void deleteBook(String isbn);
    //List<BookDTO> search(@Param("keyword") String keyword);
}
