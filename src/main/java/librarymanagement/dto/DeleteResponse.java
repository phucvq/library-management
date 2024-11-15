package librarymanagement.dto;

public class DeleteResponse {
    private String isbn;
    private String message;

    public DeleteResponse(String isbn, String message) {
        this.isbn = isbn;
        this.message = message;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

