package FinalProject.view;

import FinalProject.Book;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.function.Consumer;

public class BookCard extends VBox {

    public BookCard(Book book, Consumer<Book> onBookSelected) {
        setPrefWidth(210);
        setPrefHeight(320);
        setPadding(new Insets(12));
        setAlignment(Pos.TOP_CENTER);
        setSpacing(8);

        setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.06), 8, 0, 0, 4); " +
                "-fx-cursor: hand;");

        setOnMouseEntered(e -> setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(79,70,229,0.25), 12, 0, 0, 6); " +
                "-fx-cursor: hand;"));
        setOnMouseExited(e -> setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.06), 8, 0, 0, 4); " +
                "-fx-cursor: hand;"));

        ImageView cover = new ImageView();
        cover.setFitWidth(180);
        cover.setFitHeight(200);
        cover.setPreserveRatio(false);

        try {
            String path = book.getImagePath();
            if (path != null && !path.isBlank()) {
                if (path.startsWith("http")) {
                    cover.setImage(new Image(path, 180, 200, true, true, true));
                } else {
                    File file = new File(path);
                    if (file.exists()) {
                        cover.setImage(new Image(file.toURI().toString()));
                    }
                }
            } else {
                cover.setImage(new Image("https://placehold.co/180x200/334155/ffffff?text=Kitab"));
            }
        } catch (Exception ignored) {}

        Label title = new Label(book.getTitle());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #1e293b;");
        title.setMaxWidth(180);
        title.setAlignment(Pos.CENTER);

        Label author = new Label(book.getAuthor());
        author.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        Label badge = new Label(book.getAccessType());
        boolean isPrem = "PREMIUM".equalsIgnoreCase(book.getAccessType());
        badge.setStyle("-fx-background-color: " + (isPrem ? "#fef3c7" : "#ecfdf5") + "; " +
                "-fx-text-fill: " + (isPrem ? "#b45309" : "#047857") + "; " +
                "-fx-font-weight: bold; -fx-font-size: 10px; -fx-padding: 3 8; -fx-background-radius: 10;");

        getChildren().addAll(cover, title, author, badge);

        setOnMouseClicked(e -> {
            if (onBookSelected != null) {
                onBookSelected.accept(book);
            }
        });
    }
}