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
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final BookConverter bookConverter;

    public BookServiceImpl(BookMapper bookMapper, BookConverter bookConverter) {
        this.bookMapper = bookMapper;
        this.bookConverter = bookConverter;
    }

//    @Override
//    public List<BookDTO> getAllBooks() {
//        List<Book> books = bookMapper.findAll();
//        return books.stream()
//                .map(bookConverter::convertToDto)
//                .collect(Collectors.toList());
//    }

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

