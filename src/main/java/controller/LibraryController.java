package controller;

import model.*;
import repository.LibraryRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.Objects.nonNull;
import static model.Status.AVAILABLE;

public class LibraryController {

    Scanner scanner = new Scanner(System.in);
    LibraryRepository libraryRepository = new LibraryRepository();

    public void addNewBook() {
        try {
            Book book = collectBookInfo();
            libraryRepository.addBookToDB(book);
            System.out.println("Book was added successfully");
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getClass() + " -" + exception.getMessage());
        }
    }

    private Book collectBookInfo() {
        Book book = new Book();
        book.setTitle(getUserInput("Enter book's title: "));
        book.setAuthor(getUserInput("Enter book's author: "));
        book.setGenre(getUserInput("Enter book's genre: "));
        book.setPublishYear(Integer.parseInt(getUserInput("Enter publishing year: ")));
        book.setQuantity(Integer.parseInt(getUserInput("Enter quantity: ")));
        book.setStatus(AVAILABLE);

        return book;
    }

    private String getUserInput(String message) {
        System.out.print(message);
        String value = scanner.nextLine();
        return value;
    }

    public void displayBookList() {
        try {
            ArrayList<Book> bookList = libraryRepository.getAllBooksFromDB();
            bookList.forEach(System.out::println);
        } catch (SQLException exception) {
            System.out.println("Error: " + exception.getClass() + exception.getMessage());
        }
    }

    public void displayReaderList() {
        try {
            ArrayList<User> readerList = libraryRepository.getAllReadersFromDB();
            readerList.forEach(System.out::println);
        } catch (SQLException exception) {
            System.out.println("Error: " + exception.getClass() + exception.getMessage());
        }
    }

    public void viewBook() {
        try {
            Integer bookId = Integer.parseInt(getUserInput("Enter the id of book to view: "));
            Book book = libraryRepository.getSingleBookFromDB(bookId);
            System.out.println(book);
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getClass() + exception.getMessage());
        }
    }

    public Book viewBookByTitle() {
        try {
            String title = getUserInput("Enter the title of book : ");
            Book book = libraryRepository.getBookByTitle(title);
            System.out.println(book);
            return book;
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getClass() + exception.getMessage());
        }
        return null;
    }

    public void removeBook() {
        Integer bookId = Integer.parseInt(getUserInput("Enter the id of book to delete:"));
        try {
            libraryRepository.deleteBook(bookId);
            System.out.println("Book deleted successfully");
        } catch (SQLException exception) {
            System.out.println("Error: " + exception.getClass() + exception.getMessage());
        }
    }

    public void updateBook() {
        int bookId = Integer.parseInt(getUserInput("Enter the id of book to update:"));
        try {
            libraryRepository.getSingleBookFromDB(bookId);
        } catch (Exception e) {
            System.out.println("Book with this id not found");
            System.exit(0);
        }
        System.out.println("What do you want to update?: " +
                "\n 1.Title" +
                "\n 2.Author" +
                "\n 3.Genre" +
                "\n 4.Publishing year" +
                "\n 5.Quantity" +
                "\n 6.Status" +
                "\n 7.Exit"
        );
        Integer userChoice = scanner.nextInt();
        scanner.nextLine();

        try {
            if (userChoice == 1) {
                System.out.print("Enter new title: ");
                String newTitle = scanner.nextLine();
                libraryRepository.updateTitle(bookId, newTitle);
                System.out.print("Book title changed successfully!\n");
            } else if (userChoice == 2) {
                System.out.print("Enter new author: ");
                String newAuthor = scanner.nextLine();
                libraryRepository.updateAuthor(bookId, newAuthor);
                System.out.print("Book author changed successfully!\n");
            } else if (userChoice == 3) {
                System.out.print("Enter new genre: ");
                String newGenre = scanner.nextLine();
                libraryRepository.updateGenre(bookId, newGenre);
                System.out.print("Book publishing year changed successfully!\n");
            } else if (userChoice == 4) {
                System.out.print("Enter new publishing year: ");
                Integer newYear = Integer.parseInt(scanner.nextLine());
                libraryRepository.updateYear(bookId, newYear);
                System.out.print("Book publishing year changed successfully!\n");
            } else if (userChoice == 5) {
                System.out.print("Enter new quantity: ");
                Integer newQuantity = Integer.parseInt(scanner.nextLine());
                libraryRepository.updateQuantity(bookId, newQuantity);
                System.out.println("Book quantity changed successfully!\n");
            } else if (userChoice == 6) {
                System.out.println("To change the status of the book ");
                String userInput = getUserInput("enter 1 for available, 2 for not available");
                Status newStatus = userInput.equals("1") ? AVAILABLE : Status.NOT_AVAILABLE;
                libraryRepository.updateStatus(bookId, newStatus);
                System.out.println("Book status changed successfully!\n");
            } else if (userChoice == 7) {
                System.exit(0);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addNewUser() {
        try {
            User user = collectReaderInfo();
            libraryRepository.addReaderToDB(user);
            System.out.println("Reader was added successfully: " + user);
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getClass() + " -" + exception.getMessage());
        }
    }

    private User collectReaderInfo() {
        User user = new User();
        user.setUserName(getUserInput("Enter your username: "));
        user.setPassword(getUserInput("Enter your password: "));
        return user;
    }

    public boolean isBookAvailable(Book book) throws SQLException {
        int borrowCount = libraryRepository.getBorrowCountByBookId(book.getId());
        return book.quantity - borrowCount > 0;
    }

    public void borrowBook() {

        try {
            int userId = Integer.parseInt(getUserInput("Enter your id: "));
            Book book = viewBookByTitle();
            if (nonNull(book) && isBookAvailable(book)) {
                createBorrow(book.getId(), userId);
            }
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getClass() + exception.getMessage());
        }
    }

    private void createBorrow(int bookId, int userId) throws SQLException {

        if (libraryRepository.borrowBook(bookId, userId)) {
            System.out.println("Book was borrowed successfully! You need to return the book by: " + LocalDate.now().plusMonths(1));
        }
    }

    public void returnBook() {
        try {
            int userId = Integer.parseInt(getUserInput("Enter your id: "));
            int bookId = Integer.parseInt(getUserInput("Enter the id of book to return: "));
            if (libraryRepository.returnBookDB(bookId, userId)) {
                System.out.println("Thank you for returning the book!");
            }
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getClass() + exception.getMessage());
        }
    }

    public void listBorrowedBooksByUserid() {
        try {
            int userId = Integer.parseInt(getUserInput("Enter your id to find your borrowed books: "));
            List<Book> borrowedBooks = libraryRepository.findBorrowedBooksByUserId(userId);
            if (!borrowedBooks.isEmpty()) {
                System.out.println("You borrowed the following books: ");
                borrowedBooks.forEach(b -> System.out.printf("\tBook ID %s - %s (%s, %d)\n", b.getId(), b.getTitle(), b.getAuthor(), b.getPublishYear()));
            } else {
                System.out.println("You don't have any borrowed books!");
            }
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getClass() + exception.getMessage());
        }
    }

    public void closeConnection() {
        libraryRepository.closeOpenConnections();
    }
}

