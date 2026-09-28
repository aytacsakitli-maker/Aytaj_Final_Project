package FinalProject.view;

import FinalProject.Book;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.function.Consumer;

public class EditBookDialog {

    public static void show(Book book, Consumer<Book> onBookUpdated) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Kitabı Redaktə Et - " + book.getTitle());

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER_LEFT);

        TextField titleField = new TextField(book.getTitle());
        TextField authorField = new TextField(book.getAuthor());

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Şəxsi İnkişaf", "Psixoloji", "Numeroloji");
        categoryBox.setValue(book.getCategory());
        categoryBox.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> accessBox = new ComboBox<>();
        accessBox.getItems().addAll("FREE", "PREMIUM");
        accessBox.setValue(book.getAccessType());
        accessBox.setMaxWidth(Double.MAX_VALUE);

        TextArea contentArea = new TextArea(book.getContentSummary());
        contentArea.setPrefRowCount(6);
        contentArea.setWrapText(true);

        HBox imagePickerBox = new HBox(8);
        TextField imagePathField = new TextField(book.getImagePath() != null ? book.getImagePath() : "");
        imagePathField.setPrefWidth(260);

        Button browseBtn = new Button("Dəyiş...");
        browseBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Yeni üz qabığı seç");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Şəkillər", "*.png", "*.jpg", "*.jpeg"));
            File file = chooser.showOpenDialog(stage);
            if (file != null) {
                imagePathField.setText(file.getAbsolutePath());
            }
        });
        imagePickerBox.getChildren().addAll(imagePathField, browseBtn);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 11px;");

        Button saveBtn = new Button("Dəyişiklikləri Yadda Saxla");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 9; -fx-background-radius: 6; -fx-cursor: hand;");

        saveBtn.setOnAction(e -> {
            if (titleField.getText().trim().isEmpty() || authorField.getText().trim().isEmpty()) {
                errorLabel.setText("Kitab adı və müəllif boş buraxıla bilməz!");
                return;
            }

            Book updated = new Book(
                    book.getId(),
                    titleField.getText().trim(),
                    authorField.getText().trim(),
                    categoryBox.getValue(),
                    accessBox.getValue(),
                    contentArea.getText().trim(),
                    imagePathField.getText().trim()
            );

            onBookUpdated.accept(updated);
            stage.close();
        });

        root.getChildren().addAll(
                new Label("Kitab Adı:"), titleField,
                new Label("Müəllif:"), authorField,
                new Label("Kateqoriya:"), categoryBox,
                new Label("Giriş Səviyyəsi:"), accessBox,
                new Label("Üz Qabığı Şəkli:"), imagePickerBox,
                new Label("Xülasə və Qeydlər:"), contentArea,
                errorLabel, saveBtn
        );

        Scene scene = new Scene(root, 420, 570);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.showAndWait();
    }
}