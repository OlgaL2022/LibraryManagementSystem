package controller;

import java.util.Scanner;

public class LibraryMenu {

    Scanner scanner = new Scanner(System.in);

    LibraryController libraryController = new LibraryController();

    public void startLibrarian() {
        System.out.println("--------------------------------------------");
        this.showOptionsLibrarian();
        this.handleLibrarianChoice();
    }

    public void startReader() {
        System.out.println("--------------------------------------------");
        this.showOptionsReader();
        this.handleReaderChoice();
    }

    private void handleLibrarianChoice() {
        System.out.print("Please choose an option: ");
        String librarianChoice = scanner.nextLine();

        switch (librarianChoice) {
            case "1":
                libraryController.displayBookList();
                break;
            case "2":
                libraryController.addNewBook();
                break;
            case "3":
                libraryController.viewBook();
                break;
            case "4":
                libraryController.removeBook();
                break;
            case "5":
                libraryController.updateBook();
                break;
            case "6":
                libraryController.displayReaderList();
                break;
            case "7":
                libraryController.closeConnection();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again");
        }
        startLibrarian();
    }

    private void handleReaderChoice() {
        System.out.print("Please choose an option: ");
        String userChoice = scanner.nextLine();

        switch (userChoice) {
            case "1":
                libraryController.addNewUser();
                break;
            case "2":
                libraryController.displayBookList();
                break;
            case "3":
                libraryController.viewBookByTitle();
                break;
            case "4":
                libraryController.borrowBook();
                break;
            case "5":
                libraryController.listBorrowedBooksByUserid();
                break;
            case "6":
                libraryController.returnBook();
                break;
            case "7":
                libraryController.closeConnection();
                System.exit(0);
            default:
                System.out.println("Invalid option. Please try again");
        }
        startReader();
    }

    private void showOptionsLibrarian() {
        System.out.println(
                """
                        Please choose activity:\s

                         1.View all books
                         2.Add new book
                         3.Find book by id
                         4.Delete book
                         5.Update book
                         6.View all readers
                         7.Exit"""
        );
    }

    private void showOptionsReader() {
        System.out.println(
                """
                        Please choose activity:\s

                         1.Register
                         2.View all books
                         3.Find book by title
                         4.Borrow book
                         5.View your borrowed books
                         6.Return borrowed Books
                         7.Exit"""
        );
    }
}



