package com.codewithmosh.librarytry2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private static List<Book> libraryBookData;
    public static final String filePath = "/com/codewithmosh/librarytry2/bookList.txt";
    @Override
    public void start(Stage stage) {
        try {
            loadLibraryData();

            Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
            root.getStyleClass().add("main-page");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("PageStyling.css").toExternalForm());
            stage.setTitle("Library Management System");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

    // Read file
    public static void loadLibraryData() {
        libraryBookData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Main.class.getResourceAsStream(filePath)))) {

            String line = reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] bookData = line.split(",");
                if (bookData.length >= 5) {
                    Book book = new Book(bookData[0], bookData[1], bookData[2], bookData[3], bookData[4]);
                    libraryBookData.add(book);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    public static List<Book> getLibraryBookData() {
        return libraryBookData;
    }

    // Save Books to file
    public static void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Title, Author, ISBN, Availability, Borrower Name"); // Writing header
            writer.newLine();
            for (Book book : libraryBookData) {
                writer.write(book.getTitle() + "," + book.getAuthor() + "," + book.getIsbn() + "," + book.getAvailability() + "," + book.getBorrowerName());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
