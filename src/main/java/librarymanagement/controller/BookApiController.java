package librarymanagement.controller;

import librarymanagement.dto.BookDTO;
import librarymanagement.dto.DeleteResponse;
import librarymanagement.entity.Book;
import librarymanagement.service.BookService;
import librarymanagement.service.InventoryClient;
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
import java.util.Map;
import java.util.function.Predicate;

@RestController
@Validated
@RequestMapping("/api/books")
public class BookApiController {

    @Autowired
    private BookService bookService;

    @Autowired
    private InventoryClient inventoryClient;

    /**
    * These methods jus for testing advanced java technique
     * 1. getAllBooks: filter, map, lambda,
     * 2. getAllBooksGrouping: grouping
     * 3. getAllBooksPublishedAfter: stream & filter
     * 4. getAllBooksCountByPublisher: map & counting
     * 5. searchBooks: filter using predicates
     * 6. addBooks: lambda
    * */
    @GetMapping("/all-books")
    public ResponseEntity<List<BookDTO>> getAllBooks(@RequestParam(defaultValue = "false") boolean onlyAvailable) {
        List<BookDTO> books = bookService.getAllBooks(onlyAvailable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/all-books-group-by-genre")
    public ResponseEntity<Map<String, List<BookDTO>>> getAllBooksGrouping() {
        Map<String, List<BookDTO>> books = bookService.groupBooksByGenre();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/find-books-published-after")
    public ResponseEntity<List<BookDTO>> getAllBooksPublishedAfter(@RequestParam(defaultValue = "1000") int year) {
        List<BookDTO> books = bookService.findBooksPublishedAfter(year);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/count-books-by-publisher")
    public ResponseEntity<Map<String, Long>> getAllBooksCountByPublisher() {
        Map<String, Long> books = bookService.countBooksByPublisher();
        return ResponseEntity.ok(books);
    }

    //List<BookDTO> searchBooks(Predicate<Book> condition)
    @GetMapping("/search-by-author")
    public ResponseEntity<List<BookDTO>> searchBooks(@RequestParam() String keyword) {
        Predicate<Book> byAuthor = book -> book.getAuthor().equalsIgnoreCase(keyword);
        Predicate<Book> isAvailable = Book::isAvailable;
        List<BookDTO> books = bookService.searchBooks(byAuthor.and(isAvailable));
        return ResponseEntity.ok(books);
    }

    @PostMapping("/add-books")
    public ResponseEntity<List<BookDTO>> addBooks(@RequestBody List<BookDTO> books) {
        bookService.addBooks(books);
        return ResponseEntity.ok(books);
    }

    /**
     * These are api implementations
    * */
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

    @PostMapping("/borrow/{isbn}")
    public ResponseEntity<String> borrowBook(@PathVariable String isbn) {
        int stock = inventoryClient.getStock(isbn); // Gọi Inventory Service
        if (stock <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Book is out of stock");
        }

        inventoryClient.updateStock(isbn, stock - 1); // Giảm số lượng tồn kho
        return ResponseEntity.ok("Book borrowed successfully");
    }


//    @GetMapping("search/{keyword}")
//    public List<BookDTO> search(@PathVariable String keyword) {
//        return bookService.search(keyword);
//    }
}
