package FinalProject.view;

import FinalProject.User;
import FinalProject.service.UserRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminUsersDialog {

    public static void show(Stage ownerStage) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(ownerStage);
        stage.setTitle("Admin İdarəetmə Paneli - İstifadəçilər");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #ffffff;");

        Label title = new Label("👥 Sistem İstifadəçilərinin İdarə Edilməsi");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

        UserRepository repo = UserRepository.getInstance();
        repo.loadDataFromFile();

        ObservableList<User> tableData = FXCollections.observableArrayList(repo.getAllUsers());

        VBox formCard = new VBox(10);
        formCard.setPadding(new Insets(12));
        formCard.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8; -fx-border-color: #e2e8f0; -fx-border-radius: 8;");

        Label formTitle = new Label("➕ Yeni İstifadəçi Əlavə Et");
        formTitle.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        TextField newUsernameField = new TextField();
        newUsernameField.setPromptText("İstifadəçi adı");

        TextField newFullNameField = new TextField();
        newFullNameField.setPromptText("Ad və Soyad");

        TextField newPasswordField = new TextField();
        newPasswordField.setPromptText("Şifrə");

        ComboBox<String> newSubCombo = new ComboBox<>();
        newSubCombo.getItems().addAll("FREE", "PREMIUM");
        newSubCombo.setValue("FREE");
        newSubCombo.setMaxWidth(Double.MAX_VALUE);

        Button createBtn = new Button("Əlavə Et");
        createBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 6 16; -fx-background-radius: 6; -fx-cursor: hand;");

        grid.add(new Label("İstifadəçi Adı:"), 0, 0);
        grid.add(newUsernameField, 0, 1);
        grid.add(new Label("Ad və Soyad:"), 1, 0);
        grid.add(newFullNameField, 1, 1);
        grid.add(new Label("Şifrə:"), 2, 0);
        grid.add(newPasswordField, 2, 1);
        grid.add(new Label("Abunəlik:"), 3, 0);
        grid.add(newSubCombo, 3, 1);
        grid.add(createBtn, 4, 1);

        Label formStatus = new Label();
        formStatus.setStyle("-fx-font-size: 11px;");

        createBtn.setOnAction(e -> {
            String uName = newUsernameField.getText().trim();
            String fName = newFullNameField.getText().trim();
            String pass = newPasswordField.getText().trim();
            String sub = newSubCombo.getValue();

            if (uName.isEmpty() || fName.isEmpty() || pass.isEmpty()) {
                formStatus.setStyle("-fx-text-fill: #ef4444;");
                formStatus.setText("Bütün xanaları doldurun!");
                return;
            }

            boolean added = repo.addUser(new User(uName, fName, pass, sub));
            if (added) {
                formStatus.setStyle("-fx-text-fill: #10b981;");
                formStatus.setText("İstifadəçi '" + uName + "' uğurla bazaya əlavə edildi.");
                newUsernameField.clear();
                newFullNameField.clear();
                newPasswordField.clear();
                newSubCombo.setValue("FREE");

                repo.loadDataFromFile();
                tableData.setAll(repo.getAllUsers());
            } else {
                formStatus.setStyle("-fx-text-fill: #ef4444;");
                formStatus.setText("Bu istifadəçi adı artıq mövcuddur!");
            }
        });

        formCard.getChildren().addAll(formTitle, grid, formStatus);

        TableView<User> table = new TableView<>(tableData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<User, String> colUser = new TableColumn<>("İstifadəçi Adı");
        colUser.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));

        TableColumn<User, String> colName = new TableColumn<>("Ad və Soyad");
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFullName()));

        TableColumn<User, String> colSub = new TableColumn<>("Abunəlik");
        colSub.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSubscriptionType()));

        TableColumn<User, String> colPass = new TableColumn<>("Şifrə");
        colPass.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPassword()));

        table.getColumns().addAll(colUser, colName, colSub, colPass);
        VBox.setVgrow(table, Priority.ALWAYS);

        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER_LEFT);

        Button toggleSubBtn = new Button("🔄 Abunəliyi Dəyiş (FREE / PREMIUM)");
        toggleSubBtn.setStyle("-fx-background-color: #f59e0b; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 14; -fx-background-radius: 6; -fx-cursor: hand;");

        Button deleteBtn = new Button("🗑️ İstifadəçini Sil");
        deleteBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 14; -fx-background-radius: 6; -fx-cursor: hand;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeBtn = new Button("Bağla");
        closeBtn.setStyle("-fx-background-color: #0f172a; -fx-text-fill: white; -fx-padding: 8 18; -fx-background-radius: 6; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> stage.close());

        toggleSubBtn.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Seçim Edin", "Zəhmət olmasa cədvəldən bir istifadəçi seçin.");
                return;
            }

            String currentSub = selected.getSubscriptionType();
            String newSub = "PREMIUM".equalsIgnoreCase(currentSub) ? "FREE" : "PREMIUM";

            repo.updateSubscription(selected.getUsername(), newSub);
            repo.loadDataFromFile();
            tableData.setAll(repo.getAllUsers());
        });

        deleteBtn.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Seçim Edin", "Zəhmət olmasa silmək üçün bir istifadəçi seçin.");
                return;
            }

            if ("admin".equalsIgnoreCase(selected.getUsername())) {
                showAlert("Qadağandır", "Əsas Admin istifadəçisini sistemdən silmək olmaz!");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("İstifadəçini Sil");
            confirm.setHeaderText(null);
            confirm.setContentText("'" + selected.getUsername() + "' hesabını bazadan silmək istədiyinizə əminsiniz?");

            confirm.showAndWait().ifPresent(res -> {
                if (res == ButtonType.OK) {
                    repo.deleteUser(selected.getUsername());
                    repo.loadDataFromFile();
                    tableData.setAll(repo.getAllUsers());
                }
            });
        });

        actionBox.getChildren().addAll(toggleSubBtn, deleteBtn, spacer, closeBtn);
        root.getChildren().addAll(title, formCard, table, actionBox);

        Scene scene = new Scene(root, 720, 520);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.showAndWait();
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}