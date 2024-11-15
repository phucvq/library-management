package librarymanagement.repository;

import librarymanagement.entity.Book;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookMapper {

    //@Select("SELECT * FROM books")
//    List<Book> findAll();
    List<Book> findAll(@Param("pageSize") int pageSize, @Param("offset") int offset);

    //@Select("SELECT * FROM books WHERE isbn = #{isbn}")
    Book findByIsbn(String isbn);

    //@Insert("INSERT INTO books (title, author, publisher, year_published, genre, is_available) VALUES (#{title}, #{author}, #{publisher}, #{yearPublished}, #{genre}, #{isAvailable})")
    //@Options(useGeneratedKeys = true, keyProperty = "isbn")
    void insertBook(Book book);

   // @Update("UPDATE books SET title = #{title}, author = #{author}, publisher = #{publisher}, year_published = #{yearPublished}, genre = #{genre}, is_available = #{isAvailable} WHERE isbn = #{isbn}")
    void updateBook(Book book);

    //@Delete("DELETE FROM books WHERE isbn = #{isbn}")
    void deleteByIsbn(String isbn);

    long countBooks();

    //List<Book> search(@Param("keyword") String keyword);
}
