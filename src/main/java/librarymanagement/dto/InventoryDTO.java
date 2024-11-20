package librarymanagement.dto;

public class InventoryDTO {

    private String isbn;
    private int stock;

    // Getters và Setters
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
