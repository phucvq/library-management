package librarymanagement.entity;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private int yearPublished;
    private String genre;
    private boolean isAvailable;

    public Book(){}

    public Book(String isbn,
                String title,
                String author,
                String publisher,
                int yearPublished,
                String genre,
                boolean isAvailable) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
        this.yearPublished = yearPublished;
        this.isAvailable = isAvailable;
    }

    public String getISBN() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setISBN(String isbn) { this.isbn = isbn; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return "Book{ ISBN=" + isbn +
                ", Title=" + title +
                ", Author=" + author +
                ", Genre=" + genre +
                ", Publisher=" + publisher +
                ", Year published=" +
                ", Available=" + isAvailable + "}";
    }
}

