package librarymanagement.controller;

import librarymanagement.service.BookService;
import librarymanagement.service.InventoryClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(BookApiController.class)
public class BookApiControllerMockitoTest {

    @Autowired
    private MockMvc mockMvc; // Công cụ giả lập HTTP requests

    @MockBean
    private InventoryClient inventoryClient; // Mock InventoryClient để giả lập API calls

    private final String isbn = "9780134685991"; //

    @Test
    void testBorrowBook_OutOfStock() throws Exception {
        // Giả lập stock = 0
        Mockito.when(inventoryClient.getStock(isbn)).thenReturn(0);

        // Gửi HTTP POST request
        mockMvc.perform(post("/borrow/{isbn}", isbn))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Book is out of stock"));

        // Đảm bảo updateStock không được gọi
        Mockito.verify(inventoryClient, Mockito.never()).updateStock(Mockito.anyString(), Mockito.anyInt());
    }


}
