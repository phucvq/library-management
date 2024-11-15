package librarymanagement.util;

import librarymanagement.dto.BookDTO;
import librarymanagement.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookConverter {
    public Book convertToEntity(BookDTO bookDto) {
        return new Book(
                bookDto.getIsbn(),
                bookDto.getTitle(),
                bookDto.getAuthor(),
                bookDto.getPublisher(),
                bookDto.getYearPublished(),
                bookDto.getGenre(),
                bookDto.isAvailable()
        );
    }

    public BookDTO convertToDto(Book book) {
        return new BookDTO(
                book.getISBN(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getYearPublished(),
                book.getGenre(),
                book.isAvailable()
        );
    }
}
