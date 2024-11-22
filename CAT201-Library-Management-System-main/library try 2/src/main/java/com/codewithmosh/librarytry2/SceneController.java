package com.codewithmosh.librarytry2;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;


public class SceneController {

    private void switchScene(ActionEvent event, String fxmlFile, String styleClass) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        root.getStyleClass().add(styleClass);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("PageStyling.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMainPage(ActionEvent event) throws IOException {
        switchScene(event, "MainPage.fxml", "main-page");
    }

    public void switchToUserMode(ActionEvent event) throws IOException {
        switchScene(event, "UserMode.fxml", "user-mode");
    }

    public void switchToLibrarianMode(ActionEvent event) throws IOException {
        switchScene(event, "LibrarianMode.fxml", "librarian-mode");
    }

    public void switchToBorrowBookPage(ActionEvent event) throws IOException {
        switchScene(event, "BorrowBook.fxml", "borrow-book");
    }

    public void showMessage(Label label, String message, boolean isSuccess) {
        label.setText(message);
        if (isSuccess) {
            label.getStyleClass().add("prompt-message-success");
        } else {
            label.getStyleClass().add("prompt-message-failure");
        }
        PauseTransition messageDelay = new PauseTransition(Duration.seconds(2));
        messageDelay.setOnFinished(event -> label.setText(""));
        messageDelay.play();
    }

    // Search & Display
    @FXML
    private TextField searchTextField;
    @FXML
    private Label titleLabel,authorLabel,isbnLabel,availabilityLabel,borrowerNameLabel;

    public void search(TextField searchTextField, Label titleLabel, Label authorLabel, Label isbnLabel,
                       Label availabilityLabel, Label borrowerNameLabel) {
        String searchTerm = searchTextField.getText();  // Get the search query from the text field
        Book foundBook = Library.SearchFunction(searchTerm);  // Search for the book using the common search function

        if (foundBook != null) {
            titleLabel.setText("Title:  " + foundBook.getTitle());
            authorLabel.setText("Author:  " + foundBook.getAuthor());
            isbnLabel.setText("ISBN:  " + foundBook.getIsbn());
            availabilityLabel.setText("Availability:  " + foundBook.getAvailability());
            borrowerNameLabel.setText("Borrower Name:  " + foundBook.getBorrowerName());
        } else {
            titleLabel.setText("No match found.");
            authorLabel.setText("");
            isbnLabel.setText("");
            availabilityLabel.setText("");
            borrowerNameLabel.setText("");
        }
    }

    // User Search
    public void search() {
        search(searchTextField, titleLabel, authorLabel, isbnLabel, availabilityLabel, borrowerNameLabel);
    }

    // Librarian Search
    @FXML
    private TextField librarianSearchTextField;
    @FXML
    private Label librarianTitleLabel, librarianAuthorLabel, librarianIsbnLabel, librarianAvailabilityLabel, librarianBorrowerNameLabel;

    public void librarianSearch() {
        search(librarianSearchTextField, librarianTitleLabel, librarianAuthorLabel, librarianIsbnLabel,
                librarianAvailabilityLabel, librarianBorrowerNameLabel);
    }




    // Library Book List Display
    @FXML
    private TableView<Book> table;
    @FXML
    private TableColumn<Book, String> authorColumn,availabilityColumn,borrowerNameColumn,isbnColumn,titleColumn;

    public void displayLibraryBook() {
        List<Book> books = Book.displayLibraryBook();

        if (books != null && !books.isEmpty()) {
            System.out.println("Loaded books: " + books.size());

            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
            isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
            availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
            borrowerNameColumn.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));

            ObservableList<Book> observableBooks = FXCollections.observableArrayList(books);
            table.setItems(observableBooks);
        } else {
            System.out.println("No books found or empty list.");
        }
    }

    // addBook
    @FXML
    private TextField addTitleField,addAuthorField,addIsbnField;
    @FXML
    private Label successLabel,failLabel,deleteSuccessLabel,deleteFailLabel;

    Boolean isDelete=true;


    public void addBook() {
        String title = addTitleField.getText();
        String author = addAuthorField.getText();
        String isbn = addIsbnField.getText();

        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            showMessage(failLabel, "All fields are required!",false);
        } else if (Library.isBookExist(title, author, isbn)) {
            showMessage(failLabel, "Book already exists!",false);
        }else {
            Library.addBook(title, author, isbn, "Available", "N/A");
            Main.saveBooksToFile();

            addTitleField.clear();
            addAuthorField.clear();
            addIsbnField.clear();
            showMessage(successLabel, "Book added successfully!",true);
        }
    }

    // deleteBook
    public void deleteBook() {
        String title = addTitleField.getText();
        String author = addAuthorField.getText();
        String isbn = addIsbnField.getText();

        isDelete = Library.deleteBook(isbn,title,author);

        if (isDelete) {
            showMessage(deleteSuccessLabel, "Book deleted successfully!",true);
        }else{
            showMessage(deleteFailLabel, "Book not found.",false);
        }
    }

    // borrowBook
    @FXML
    private TextField infoTextField, nameTextField, nameReturnTextField;
    @FXML
    private Label successLabel2, successLabel3,failLabel2, failLabel3;
    String info, borrowerName;

    public void borrow() {
        info = infoTextField.getText();
        borrowerName = nameTextField.getText();

        Book bookToBorrow = Library.SearchFunction(info);

        if (bookToBorrow != null) {
            if (bookToBorrow.getAvailability().equalsIgnoreCase("Available")) {
                if (bookToBorrow.getTitle().equalsIgnoreCase(info) || bookToBorrow.getIsbn().equalsIgnoreCase(info) || bookToBorrow.getAuthor().equalsIgnoreCase(info)) {
                    bookToBorrow.borrowBook(info, borrowerName);
                    showMessage(successLabel2, "Book is successfully borrowed!", true);
                } else {
                    showMessage(failLabel2, "This book is not available.", false);
                }
            } else {
                showMessage(failLabel2, "Book already borrowed by " + bookToBorrow.getBorrowerName(), false);
            }
            Main.saveBooksToFile();
        } else {
            showMessage(failLabel2, "Book not found", false);
        }
        nameTextField.clear();
    }

    // return book
    public void Return() {
        info = infoTextField.getText();
        borrowerName = nameReturnTextField.getText();

        Book bookToReturn = Library.SearchFunction(info);

        if (bookToReturn != null) {
            if (bookToReturn.getAvailability().equalsIgnoreCase("Checked Out")) {
                if (bookToReturn.getBorrowerName().equalsIgnoreCase(borrowerName)) {
                    if (bookToReturn.getTitle().equalsIgnoreCase(info) || bookToReturn.getIsbn().equalsIgnoreCase(info) || bookToReturn.getAuthor().equalsIgnoreCase(info)) {
                        bookToReturn.returnBook(info, borrowerName);
                        showMessage(successLabel3, "Book is successfully returned!", true);
                    } else {
                        showMessage(failLabel3, "This book title does not match the borrowed book.", false);
                    }
                } else {
                    showMessage(failLabel3, "You can only return your own book.", false);
                }
            } else {
                showMessage(failLabel3, "The book is already available.", false);
            }
            Main.saveBooksToFile();
        } else {
            showMessage(failLabel3, "Book not found", false);
        }
        nameReturnTextField.clear();
    }

}



