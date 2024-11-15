package librarymanagement.dto;

import java.util.Objects;

public class BookDTO {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private int yearPublished;
    private String genre;
    private boolean isAvailable;

    // Constructors
    public BookDTO() {
    }

    public BookDTO(String isbn, String title, String author, String publisher, int yearPublished, String genre, boolean isAvailable) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.yearPublished = yearPublished;
        this.genre = genre;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", yearPublished=" + yearPublished +
                ", genre='" + genre + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDto = (BookDTO) o;
        return yearPublished == bookDto.yearPublished &&
                isAvailable == bookDto.isAvailable &&
                isbn.equals(bookDto.isbn) &&
                title.equals(bookDto.title) &&
                author.equals(bookDto.author) &&
                publisher.equals(bookDto.publisher) &&
                genre.equals(bookDto.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, title, author, publisher, yearPublished, genre, isAvailable);
    }

}

