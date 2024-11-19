package librarymanagement.service;

import librarymanagement.dto.BookDTO;
import librarymanagement.entity.Book;
import librarymanagement.exception.ResourceNotFoundException;
import librarymanagement.repository.BookMapper;
import librarymanagement.util.BookConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final BookConverter bookConverter;

    public BookServiceImpl(BookMapper bookMapper, BookConverter bookConverter) {
        this.bookMapper = bookMapper;
        this.bookConverter = bookConverter;
    }

    /**
     * These methods jus for testing advanced java technique
     * 1. getAllBooks: filter, map, lambda,
     * 2. getAllBooksGrouping: grouping
     * 3. getAllBooksPublishedAfter: stream & filter
     * 4. getAllBooksCountByPublisher: map & counting
     * 5. searchBooks: filter using predicates
     * 6. addBooks: lambda
     * */
    // Just demo for using filter and map
    // Using lambda in the predicate
    @Override
    public List<BookDTO> getAllBooks(boolean onlyAvailable) {
        List<Book> books = bookMapper.getAllBooks();
        Predicate<Book> availableFilter = book -> !onlyAvailable || book.isAvailable();
        return books.stream()
                .filter(availableFilter)
                .map(bookConverter::convertToDto)
                .collect(Collectors.toList());
    }

    // Just demo for grouping
    public Map<String, List<BookDTO>> groupBooksByGenre() {
        List<Book> books = bookMapper.getAllBooks();
        return books.stream()
                .map(bookConverter::convertToDto)
                .collect(Collectors.groupingBy(BookDTO::getGenre)); // Method reference
    }

    // filter
    public List<BookDTO> findBooksPublishedAfter(int year) {
        List<Book> books = bookMapper.getAllBooks();
        // Using Stream API vandà Filter
        return books.stream()
                .filter(book -> book.getYearPublished() > year)
                .map(bookConverter::convertToDto)
                .collect(Collectors.toList());
    }

    // map & counting
    public Map<String, Long> countBooksByPublisher() {
        List<Book> books = bookMapper.getAllBooks();
        // Using Stream API and Collectors.groupingBy with Collectors.counting
        return books.stream()
                .collect(Collectors.groupingBy(Book::getPublisher, Collectors.counting()));
    }

    // search book by predicate
    public List<BookDTO> searchBooks(Predicate<Book> condition) {
        List<Book> books = bookMapper.getAllBooks();
        // Using Stream API and Predicate
        return books.stream()
                .filter(condition)
                .map(bookConverter::convertToDto)
                .collect(Collectors.toList());
    }

    // using lambda
    public void addBooks(List<BookDTO> books) {
        books.forEach(bookDto -> bookMapper.insertBook(bookConverter.convertToEntity(bookDto)));
    }



    /**
    * These are API implementation
    * */
    @Override
    public Page<BookDTO> getAllBooks(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int offset = pageable.getPageNumber() * pageSize;

        List<Book> books = bookMapper.findAll(pageSize, offset);
        long totalRecords = bookMapper.countBooks(); // Đếm tổng số bản ghi

        return new PageImpl<>(
                books.stream().map(bookConverter::convertToDto).collect(Collectors.toList()),
                pageable,
                totalRecords
        );
    }

    @Override
    public BookDTO getBookByIsbn(String isbn) {
        Book book = bookMapper.findByIsbn(isbn);
        if (book == null) {
            throw new ResourceNotFoundException("Book not found with ISBN: " + isbn);
        }
        return bookConverter.convertToDto(book);
    }

    @Override
    public void addBook(BookDTO bookDto) {
        Book book = bookConverter.convertToEntity(bookDto);
        bookMapper.insertBook(book);
    }

    @Override
    public void updateBook(String isbn, BookDTO bookDto) {
        Book existingBook = bookMapper.findByIsbn(isbn);
        if (existingBook == null) {
            throw new ResourceNotFoundException("Book not found with ISBN: " + isbn);
        }
        existingBook.setTitle(bookDto.getTitle());
        existingBook.setAuthor(bookDto.getAuthor());
        existingBook.setPublisher(bookDto.getPublisher());
        existingBook.setGenre(bookDto.getGenre());
        existingBook.setYearPublished(bookDto.getYearPublished());
        existingBook.setAvailable(bookDto.isAvailable());
        bookMapper.updateBook(existingBook);
    }

    @Override
    public void deleteBook(String isbn) {
        Book book = bookMapper.findByIsbn(isbn);
        if (book == null) {
            throw new ResourceNotFoundException("Book not found with ISBN: " + isbn);
        }
        bookMapper.deleteByIsbn(isbn);
    }

//    @Override
//    public List<BookDTO> search(String keyword) {
//        List<Book> books = bookMapper.search(keyword);
//        return books.stream()
//                .map(bookConverter::convertToDto)
//                .collect(Collectors.toList());
//    }
}

