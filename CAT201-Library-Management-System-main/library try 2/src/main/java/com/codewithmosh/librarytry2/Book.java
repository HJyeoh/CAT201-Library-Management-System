package com.codewithmosh.librarytry2;

import java.util.ArrayList;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private String availability;
    private String borrowerName;

    public Book(String title, String author, String isbn, String availability, String borrowerName) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.availability = availability;
        this.borrowerName = borrowerName;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAvailability() {
        return availability;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    // Display Book Function
    public static ArrayList<Book> displayLibraryBook() {
        return Main.getLibraryBookData();
    }

   // Borrow Book Function
    public void borrowBook(String info, String name) {
        if (this.availability.equalsIgnoreCase("Available")) {
            if (this.title.equalsIgnoreCase(info) || this.isbn.equalsIgnoreCase(info)|| this.getAuthor().equalsIgnoreCase(info)) {
                this.availability = "Checked Out";
                this.borrowerName = name;
                System.out.println("Book is Borrowed");
                Main.saveBooksToFile();
            } else {
                System.out.println("This book is not available");
            }
        } else {
            System.out.println("Book already borrowed by " + this.borrowerName);
        }
    }

    // Return Book Function
    public void returnBook(String info, String name) {
        if (this.availability.equalsIgnoreCase("Checked Out")) {
            if(this.borrowerName.equalsIgnoreCase(name)){
                if (this.title.equalsIgnoreCase(info) || this.isbn.equalsIgnoreCase(info)|| this.getAuthor().equalsIgnoreCase(info)) {
                    this.availability = "Available";
                    this.borrowerName = "N/A";
                    System.out.println("Book is Returned");
                    Main.saveBooksToFile();
                } else {
                    System.out.println("This book title does not match the borrowed book.");
                }
            }else{
                System.out.println("You can only return your own book.");
            }
        } else {
            System.out.println("The book is already available.");
        }
    }
}
