package librarymanagement.service;

import librarymanagement.dto.InventoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InventoryClient {

    private final RestTemplate restTemplate;

    public InventoryClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public int getStock(String isbn) {
        try {
            String url = "http://localhost:8081/api/inventory/" + isbn;
            InventoryDTO inventory = restTemplate.getForObject(url, InventoryDTO.class);
            return inventory != null ? inventory.getStock() : 0;
        } catch (Exception e) {
            System.err.println("Error fetching stock for ISBN: " + isbn + " - " + e.getMessage());
            return -1;
        }
    }

    public void updateStock(String isbn, int stock) {
        String url = "http://localhost:8081/api/inventory/" + isbn;
        restTemplate.put(url, stock);
    }

    public void addStock(String isbn, int stock) {
        String url = "http://localhost:8081/api/inventory";
        InventoryDTO inventory = new InventoryDTO();
        inventory.setIsbn(isbn);
        inventory.setStock(stock);
        restTemplate.postForObject(url, inventory, InventoryDTO.class);
    }
}

