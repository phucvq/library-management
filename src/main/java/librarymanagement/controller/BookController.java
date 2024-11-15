package librarymanagement.controller;

import librarymanagement.dto.BookDTO;
import librarymanagement.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // Hiển thị danh sách sách
//    @GetMapping
//    public String listBooks(Model model) {
//        List<BookDTO> books = bookService.getAllBooks();
//        model.addAttribute("books", books);
//        return "books"; // Trả về file books.html trong thư mục templates
//    }

    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "0") int page, // Số trang hiện tại (bắt đầu từ 0)
            @RequestParam(defaultValue = "5") int size, // Số bản ghi trên mỗi trang
            Model model) {

        // Lấy danh sách sách theo phân trang
        Page<BookDTO> booksPage = bookService.getAllBooks(PageRequest.of(page, size));

        // Thêm các thuộc tính vào Model
        model.addAttribute("books", booksPage.getContent()); // Danh sách sách trên trang hiện tại
        model.addAttribute("currentPage", page);             // Trang hiện tại
        model.addAttribute("totalPages", booksPage.getTotalPages()); // Tổng số trang
        model.addAttribute("totalItems", booksPage.getTotalElements()); // Tổng số sách
        model.addAttribute("pageSize", size);                // Kích thước trang

        return "books"; // Trả về file books.html trong thư mục templates
    }



    // Hiển thị form thêm sách
    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new BookDTO());
        return "add-book"; // Trả về file add-book.html
    }

    // Thêm sách mới
    @PostMapping("/add")
    public String addBook(@ModelAttribute BookDTO book) {
        bookService.addBook(book);
        return "redirect:/books"; // Quay lại trang danh sách sách
    }

    // Hiển thị form chỉnh sửa sách
    @GetMapping("/edit/{isbn}")
    public String showEditBookForm(@PathVariable String isbn, Model model) {
        BookDTO book = bookService.getBookByIsbn(isbn);
        model.addAttribute("book", book);
        return "edit-book"; // Trả về file edit-book.html
    }

    // Cập nhật sách
    @PostMapping("/edit")
    public String updateBook(@ModelAttribute BookDTO book) {
        bookService.updateBook(book.getIsbn(), book);
        return "redirect:/books";
    }

    // Xóa sách
    @GetMapping("/delete/{isbn}")
    public String deleteBook(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
        return "redirect:/books";
    }
}

