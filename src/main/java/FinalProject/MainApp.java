package FinalProject;

import FinalProject.service.BookService;
import FinalProject.service.UserService;
import FinalProject.view.AddBookDialog;
import FinalProject.view.AdminUsersDialog;
import FinalProject.view.AuthView;
import FinalProject.view.BookDetailView;
import FinalProject.view.ProfileDialog;
import FinalProject.view.ViewBuilder;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private final BookService bookService = new BookService();
    private final UserService userService = UserService.getInstance();
    private final ViewBuilder ui = new ViewBuilder();
    private User currentUser = null;

    @Override
    public void start(Stage primaryStage) {
        AuthView authView = new AuthView(userService, user -> {
            this.currentUser = user;
            showMainLibraryWindow(primaryStage);
        });

        authView.show(primaryStage);
    }

    private void updateStageTitle(Stage stage) {
        stage.setTitle("Ağıllı Kitabxana - " + currentUser.getFullName() + " (" + currentUser.getSubscriptionType() + ")");
    }

    private void showMainLibraryWindow(Stage stage) {
        updateStageTitle(stage);

        bookService.loadFromFile();

        // İstifadəçinin admin olub-olmadığını yoxlayırıq
        boolean isAdmin = "admin".equalsIgnoreCase(currentUser.getUsername());

        Parent root = ui.buildUI(
                currentUser.getFullName(),
                currentUser.getSubscriptionType(),
                isAdmin,
                () -> AddBookDialog.show(newBook -> {
                    bookService.addBook(newBook);
                    refreshBookGrid();
                }),
                () -> ProfileDialog.show(currentUser, () -> {
                    updateStageTitle(stage);
                    showMainLibraryWindow(stage);
                }),
                () -> AdminUsersDialog.show(stage)
        );

        Scene scene = new Scene(root, 1150, 700);
        stage.setScene(scene);

        refreshBookGrid();
        setupSearch();

        stage.centerOnScreen();
        stage.show();
    }

    private void refreshBookGrid() {
        ui.renderBooks(bookService.getBookList(), selectedBook ->
                BookDetailView.show(selectedBook, currentUser, bookService, this::refreshBookGrid)
        );
    }

    private void setupSearch() {
        ui.getSearchField().textProperty().addListener((obs, oldVal, query) -> {
            if (query == null || query.isBlank()) {
                refreshBookGrid();
            } else {
                var filtered = bookService.getBookList().stream()
                        .filter(b -> b.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                b.getAuthor().toLowerCase().contains(query.toLowerCase()))
                        .toList();
                ui.renderBooks(filtered, selectedBook ->
                        BookDetailView.show(selectedBook, currentUser, bookService, this::refreshBookGrid)
                );
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}