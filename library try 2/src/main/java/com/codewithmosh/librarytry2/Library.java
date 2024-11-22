package com.codewithmosh.librarytry2;

import java.util.List;

public class Library {

    static List<Book> books = Main.getLibraryBookData();

    // Check Book Exist
    public static Boolean isBookExist(String title, String author, String isbn) {
        // return true if book existed
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)||book.getTitle().equalsIgnoreCase(title)||book.getAuthor().equalsIgnoreCase(author)) {
                System.out.println("This book already exists in the library.");
                return true;
            }
        }
        return false;
    }

    // Add Book Function
    public static void addBook(String title, String author, String isbn, String availability, String borrowerName) {
        if(isBookExist(title, author, isbn)){
            System.out.println("This book already exists in the library.");
            return;
        }else{
            Book newBook = new Book(title, author, isbn, availability, borrowerName);
            books.add(newBook);
            Main.saveBooksToFile();
            System.out.println("Book added successfully!");
        }
    }

    // Search Function
    public static Book SearchFunction(String searchTerm) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(searchTerm) || book.getIsbn().equalsIgnoreCase(searchTerm)|| book.getAuthor().equalsIgnoreCase(searchTerm)) {
                return book;
            }
        }
        return null;
    }

    // Extra: Delete Book Function
    public static boolean deleteBook(String isbn, String title, String author) {
        for (Book book : books) {
            if (book.getIsbn().equalsIgnoreCase(isbn) || book.getTitle().equalsIgnoreCase(title) || book.getAuthor().equalsIgnoreCase(author)) {
                books.remove(book);
                Main.saveBooksToFile();
                System.out.println("Book " + book.getTitle() + " has been deleted.");
                return true;
            }
        }
        System.out.println( "Book not found.");
        return false;
    }
}
