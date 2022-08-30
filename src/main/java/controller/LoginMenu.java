package controller;

import model.UserType;

import java.util.Scanner;

public class LoginMenu {

    public void menu() {
        LibraryMenu libraryMenu = new LibraryMenu();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("--------------------------------------------");
            System.out.println("Welcome to the Library. Please select below:");
            System.out.println("1  If you are a " + UserType.LIBRARIAN + " please press 1 ");
            System.out.println("2. If you are a " + UserType.READER + " please press 2 ");
            System.out.println("3. To exit, please press 3");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    libraryMenu.startLibrarian();
                    break;
                case 2:
                    libraryMenu.startReader();
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Invalid option! Try again!");
                    break;
            }
            break;
        }
    }
}

