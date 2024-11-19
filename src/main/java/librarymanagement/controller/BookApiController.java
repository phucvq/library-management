package librarymanagement.controller;

import librarymanagement.dto.BookDTO;
import librarymanagement.dto.DeleteResponse;
import librarymanagement.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/books")
public class BookApiController {

    @Autowired
    private BookService bookService;

//    @GetMapping()
//    public List<BookDTO> getAllBooks() {
//        return bookService.getAllBooks();
//    }

    @GetMapping
    public ResponseEntity<Page<BookDTO>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<BookDTO> books = bookService.getAllBooks(PageRequest.of(page, size));
        return ResponseEntity.ok(books);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO> getBookByIsbn(
            @PathVariable @Size(max = 13, message = "ISBN must not exceed 13 characters") String isbn) {
        BookDTO book = bookService.getBookByIsbn(isbn);
        if (book != null) {
            return ResponseEntity.ok(book);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO book) {
        bookService.addBook(book);
        return ResponseEntity.ok(book);
    }

    @PutMapping("update/{isbn}")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable @Size(max = 13, message = "ISBN must not exceed 13 characters") String isbn,
            @Valid @RequestBody BookDTO book) {
        bookService.updateBook(isbn, book);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping("delete/{isbn}")
    public ResponseEntity<DeleteResponse> deleteBook(
            @PathVariable @Size(max = 13, message = "ISBN must not exceed 13 characters") String isbn) {
        bookService.deleteBook(isbn);
        DeleteResponse response = new DeleteResponse(isbn, "Deleted book successfully");
        return ResponseEntity.ok(response);
    }

//    @GetMapping("search/{keyword}")
//    public List<BookDTO> search(@PathVariable String keyword) {
//        return bookService.search(keyword);
//    }
}
