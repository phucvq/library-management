-- Tạo bảng và dữ liệu mẫu
CREATE TABLE IF NOT EXISTS books (
                                     isbn VARCHAR(13) PRIMARY KEY,
    title VARCHAR(255),
    author VARCHAR(255),
    publisher VARCHAR(255),
    year_published INT,
    genre VARCHAR(50),
    is_available BOOLEAN
    );

-- Xóa dữ liệu cũ (nếu có)
DELETE FROM books;

-- Dữ liệu khởi tạo ban đầu (không có dữ liệu để thêm sách mới)
