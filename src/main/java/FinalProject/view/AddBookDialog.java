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
import java.util.UUID;
import java.util.function.Consumer;

public class AddBookDialog {

    public static void show(Consumer<Book> onBookCreated) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Yeni Kitab Əlavə Et");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER_LEFT);

        TextField titleField = new TextField();
        titleField.setPromptText("Kitabın adı");

        TextField authorField = new TextField();
        authorField.setPromptText("Müəllif");

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Şəxsi İnkişaf", "Psixoloji", "Numeroloji");
        categoryBox.setValue("Şəxsi İnkişaf");
        categoryBox.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> accessBox = new ComboBox<>();
        accessBox.getItems().addAll("FREE", "PREMIUM");
        accessBox.setValue("FREE");
        accessBox.setMaxWidth(Double.MAX_VALUE);

        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Kitabdan əsas qeydlər və qısa xülasə...");
        contentArea.setPrefRowCount(4);
        contentArea.setWrapText(true);

        HBox imagePickerBox = new HBox(8);
        TextField imagePathField = new TextField();
        imagePathField.setPromptText("Şəkil fayl yolu və ya URL");
        imagePathField.setPrefWidth(260);

        Button browseBtn = new Button("Fayl Seç...");
        browseBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Üz qabığı seç");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Şəkillər", "*.png", "*.jpg", "*.jpeg"));
            File file = chooser.showOpenDialog(stage);
            if (file != null) {
                imagePathField.setText(file.getAbsolutePath());
            }
        });
        imagePickerBox.getChildren().addAll(imagePathField, browseBtn);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 11px;");

        Button saveBtn = new Button("Yadda Saxla");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8;");

        saveBtn.setOnAction(e -> {
            if (titleField.getText().trim().isEmpty() || authorField.getText().trim().isEmpty()) {
                errorLabel.setText("Kitab adı və müəllif boş ola bilməz!");
                return;
            }

            Book newBook = new Book(
                    UUID.randomUUID().toString().substring(0, 8),
                    titleField.getText().trim(),
                    authorField.getText().trim(),
                    categoryBox.getValue(),
                    accessBox.getValue(),
                    contentArea.getText().trim(),
                    imagePathField.getText().trim()
            );

            if (onBookCreated != null) {
                onBookCreated.accept(newBook);
            }
            stage.close();
        });

        root.getChildren().addAll(
                new Label("Kitab Adı:"), titleField,
                new Label("Müəllif:"), authorField,
                new Label("Kateqoriya:"), categoryBox,
                new Label("Giriş Növü:"), accessBox,
                new Label("Üz Qabığı:"), imagePickerBox,
                new Label("Xülasə Qeydləri:"), contentArea,
                errorLabel, saveBtn
        );

        Scene scene = new Scene(root, 400, 540);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.showAndWait();
    }
}