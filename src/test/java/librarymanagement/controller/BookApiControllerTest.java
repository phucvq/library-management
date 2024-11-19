package librarymanagement.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BookApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    @Test
    void testDatasource() throws Exception {
        System.out.println("Datasource: " + dataSource.getConnection().getMetaData().getURL());
    }

    @Test
    @Sql(scripts = "classpath:sql/controller/setup_testGetAllBooksApi.sql")
    void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].isbn").value("9780134685991"))
                .andExpect(jsonPath("$.content[0].title").value("Effective Java"))
                .andExpect(jsonPath("$.content[1].isbn").value("9780321356680"))
                .andExpect(jsonPath("$.content[1].title").value("Java Concurrency in Practice"))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    @Sql(scripts = "classpath:sql/controller/setup_testGetBookByIsbnApi.sql")
    void testGetBookByIsbn() throws Exception {
        mockMvc.perform(get("/api/books/isbn/9780134685991")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("9780134685991"))
                .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    @Test
    @Sql(scripts = "classpath:sql/controller/setup_testAddBookApi.sql")
    void testCreateBook() throws Exception {
        mockMvc.perform(post("/api/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "isbn": "9780000000003",
                                    "title": "New Book",
                                    "author": "New Author",
                                    "publisher": "New Publisher",
                                    "yearPublished": 2021,
                                    "genre": "Adventure",
                                    "isAvailable": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("9780000000003"))
                .andExpect(jsonPath("$.title").value("New Book"));
    }

    @Test
    @Sql(scripts = "classpath:sql/controller/setup_testDeleteBookApi.sql")
    void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/delete/9780134685991")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/books/isbn/9780134685991")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
