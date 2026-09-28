package FinalProject.view;

import FinalProject.Book;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.List;
import java.util.function.Consumer;

public class ViewBuilder {

    private final FlowPane bookGrid = new FlowPane();
    private final TextField searchField = new TextField();

    public Parent buildUI(String userName, String subType, boolean isAdmin, Runnable onAddNewBook, Runnable onOpenProfile, Runnable onOpenAdminPanel) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8fafc;");

        HBox header = new HBox(12);
        header.setPadding(new Insets(16, 25, 16, 25));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e2e8f0; -fx-border-width: 0 0 1 0;");

        Label logo = new Label("📚 Ağıllı Kitabxana");
        logo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        searchField.setPromptText("Kitab və ya müəllif axtar...");
        searchField.setPrefWidth(200);
        searchField.setStyle("-fx-background-radius: 20; -fx-padding: 7 14; -fx-border-color: #cbd5e1; -fx-border-radius: 20;");

        Button addBookBtn = new Button("+ Yeni Kitab");
        addBookBtn.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 7 14; -fx-background-radius: 20; -fx-cursor: hand;");
        addBookBtn.setOnAction(e -> {
            if (onAddNewBook != null) onAddNewBook.run();
        });

        // Yalnız Admin olduqda görünən düymə:
        Button adminBtn = new Button("⚙️ Admin Panel");
        adminBtn.setStyle("-fx-background-color: #0f172a; -fx-text-fill: #38bdf8; -fx-font-weight: bold; -fx-padding: 7 14; -fx-background-radius: 20; -fx-cursor: hand;");
        adminBtn.setVisible(isAdmin);
        adminBtn.setManaged(isAdmin);
        adminBtn.setOnAction(e -> {
            if (onOpenAdminPanel != null) onOpenAdminPanel.run();
        });

        Button profileBtn = new Button("👤 " + userName + " (" + subType + ")");
        boolean isPrem = "PREMIUM".equalsIgnoreCase(subType);
        profileBtn.setStyle("-fx-background-color: " + (isPrem ? "#fef3c7" : "#e0f2fe") + "; " +
                "-fx-text-fill: " + (isPrem ? "#b45309" : "#0369a1") + "; " +
                "-fx-font-weight: bold; -fx-padding: 7 14; -fx-background-radius: 20; -fx-cursor: hand;");

        profileBtn.setOnAction(e -> {
            if (onOpenProfile != null) onOpenProfile.run();
        });

        header.getChildren().addAll(logo, spacer, searchField, addBookBtn, adminBtn, profileBtn);
        root.setTop(header);

        bookGrid.setPadding(new Insets(25));
        bookGrid.setHgap(20);
        bookGrid.setVgap(20);
        bookGrid.setAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = new ScrollPane(bookGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f8fafc; -fx-background-color: transparent; -fx-border-color: transparent;");
        root.setCenter(scrollPane);

        return root;
    }

    public void renderBooks(List<Book> books, Consumer<Book> onBookSelected) {
        bookGrid.getChildren().clear();

        if (books == null || books.isEmpty()) {
            Label emptyLabel = new Label("Heç bir kitab tapılmadı.");
            emptyLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #94a3b8;");
            bookGrid.getChildren().add(emptyLabel);
            return;
        }

        for (Book book : books) {
            bookGrid.getChildren().add(new BookCard(book, onBookSelected));
        }
    }

    public TextField getSearchField() {
        return searchField;
    }
}