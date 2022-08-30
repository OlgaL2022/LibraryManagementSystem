CREATE DATABASE IF NOT EXISTS Library_Management;

USE Library_Management;

CREATE TABLE IF NOT EXISTS books(
    book_id int NOT NULL auto_increment,
    title text NOT NULL,
    author text NOT NULL,
	genre text NOT NULL,
    publishing_year INTEGER,
    quantity INTEGER,
    status VARCHAR(25),
    PRIMARY KEY(book_id)
);

CREATE TABLE IF NOT EXISTS readers(
    user_id int NOT NULL auto_increment,
    user_name VARCHAR(25) NOT NULL UNIQUE,
    user_password VARCHAR(25) NOT NULL,
    primary key(user_id)
);

CREATE TABLE IF NOT EXISTS issued_books(
    book_id INT NOT NULL,
    user_id INT NOT NULL,
    issued DATE NOT NULL,
    return_due DATE NOT NULL,
    returned_at DATE,
    FOREIGN KEY(book_id) REFERENCES books (book_id),
    FOREIGN KEY (user_id) REFERENCES readers (user_id),
    PRIMARY KEY (book_id, user_id)
);
