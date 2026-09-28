package FinalProject.view;

import FinalProject.Book;
import FinalProject.User;
import FinalProject.service.BookService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class BookDetailView {

    public static void show(Book book, User currentUser, BookService bookService, Runnable onDataChanged) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(book.getTitle() + " - Detallar");

        VBox root = new VBox(18);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #ffffff;");

        HBox topBox = new HBox(20);
        topBox.setAlignment(Pos.CENTER_LEFT);

        ImageView cover = new ImageView();
        cover.setFitWidth(130);
        cover.setFitHeight(185);
        cover.setPreserveRatio(false);

        try {
            String path = book.getImagePath();
            if (path != null && !path.isBlank()) {
                if (path.startsWith("http")) {
                    cover.setImage(new Image(path, 130, 185, true, true, true));
                } else {
                    File f = new File(path);
                    if (f.exists()) cover.setImage(new Image(f.toURI().toString()));
                }
            } else {
                cover.setImage(new Image("https://placehold.co/130x185/334155/ffffff?text=Kitab"));
            }
        } catch (Exception ignored) {}

        VBox meta = new VBox(8);
        Label title = new Label(book.getTitle());
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

        Label author = new Label("Müəllif: " + book.getAuthor());
        author.setStyle("-fx-font-size: 14px; -fx-text-fill: #475569;");

        Label category = new Label("Kateqoriya: " + book.getCategory());
        category.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        Label access = new Label("Giriş növü: " + book.getAccessType());
        boolean isPrem = "PREMIUM".equalsIgnoreCase(book.getAccessType());
        access.setStyle("-fx-font-weight: bold; -fx-text-fill: " + (isPrem ? "#f59e0b" : "#10b981") + ";");

        meta.getChildren().addAll(title, author, category, access);
        topBox.getChildren().addAll(cover, meta);

        boolean hasAccess = !"PREMIUM".equalsIgnoreCase(book.getAccessType()) ||
                "PREMIUM".equalsIgnoreCase(currentUser.getSubscriptionType());

        Label notesTitle = new Label("Kitabdan Vacib Qeydlər və Xülasə:");
        notesTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        Label content = new Label();
        content.setWrapText(true);

        if (hasAccess) {
            content.setText(book.getContentSummary());
            content.setStyle("-fx-font-size: 14px; -fx-line-spacing: 5px; -fx-text-fill: #334155;");
        } else {
            content.setText("🔒 Bu kitab PREMIUM abunəlik tələb edir.\nMəzmunu oxumaq üçün profilinizdən abunəliyinizi yüksəldin.");
            content.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #dc2626; -fx-padding: 20;");
        }

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(220);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: #e2e8f0; -fx-border-radius: 6;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        HBox actionBox = new HBox(12);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        Button editBtn = new Button("✏️ Redaktə Et");
        editBtn.setStyle("-fx-background-color: #f1f5f9; -fx-text-fill: #0f172a; -fx-border-color: #cbd5e1; -fx-border-radius: 6; -fx-padding: 7 14; -fx-cursor: hand;");
        editBtn.setOnAction(e -> {
            EditBookDialog.show(book, updatedBook -> {
                bookService.updateBook(book, updatedBook);
                if (onDataChanged != null) onDataChanged.run();
                stage.close();
            });
        });

        Button deleteBtn = new Button("🗑️ Sil");
        deleteBtn.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #dc2626; -fx-font-weight: bold; -fx-border-color: #fca5a5; -fx-border-radius: 6; -fx-padding: 7 14; -fx-cursor: hand;");
        deleteBtn.setOnAction(e -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Kitabı Sil");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("'" + book.getTitle() + "' kitabını bazadan silmək istədiyinizə əminsiniz?");

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    bookService.deleteBook(book);
                    if (onDataChanged != null) onDataChanged.run();
                    stage.close();
                }
            });
        });

        boolean canManage = "PREMIUM".equalsIgnoreCase(currentUser.getSubscriptionType()) ||
                "admin".equalsIgnoreCase(currentUser.getUsername());
        editBtn.setVisible(canManage);
        editBtn.setManaged(canManage);
        deleteBtn.setVisible(canManage);
        deleteBtn.setManaged(canManage);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeBtn = new Button("Bağla");
        closeBtn.setStyle("-fx-background-color: #0f172a; -fx-text-fill: white; -fx-padding: 8 20; -fx-background-radius: 6; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> stage.close());

        actionBox.getChildren().addAll(editBtn, deleteBtn, spacer, closeBtn);
        root.getChildren().addAll(topBox, notesTitle, scrollPane, actionBox);

        Scene scene = new Scene(root, 650, 540);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.showAndWait();
    }
}