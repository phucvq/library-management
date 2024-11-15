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

-- Thêm dữ liệu mẫu
INSERT INTO books (isbn, title, author, publisher, year_published, genre, is_available) VALUES
                                                                                            ('9780134685991', 'Effective Java', 'Joshua Bloch', 'Addison-Wesley', 2018, 'Programming', true),
                                                                                            ('9780321356680', 'Java Concurrency in Practice', 'Brian Goetz', 'Addison-Wesley', 2006, 'Programming', true),
                                                                                            ('9780132350884', 'Clean Code', 'Robert C. Martin', 'Prentice Hall', 2008, 'Programming', false);
