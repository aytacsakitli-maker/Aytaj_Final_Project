package FinalProject.view;

import FinalProject.User;
import FinalProject.service.UserRepository;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProfileDialog {

    public static void show(User user, Runnable onSubscriptionUpdated) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("İstifadəçi Profili");

        VBox root = new VBox(16);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ffffff;");

        Label avatarLabel = new Label("👤");
        avatarLabel.setStyle("-fx-font-size: 42px;");

        Label nameLabel = new Label(user.getFullName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

        Label usernameLabel = new Label("@" + user.getUsername());
        usernameLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        HBox statusBox = new HBox(8);
        statusBox.setAlignment(Pos.CENTER);
        statusBox.setPadding(new Insets(10));
        statusBox.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8; -fx-border-color: #e2e8f0; -fx-border-radius: 8;");

        Label statusTitle = new Label("Mövcud Abunəlik:");
        statusTitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #334155;");

        Label statusBadge = new Label(user.getSubscriptionType());
        boolean isPremium = "PREMIUM".equalsIgnoreCase(user.getSubscriptionType());
        statusBadge.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: " + (isPremium ? "#f59e0b" : "#10b981") + ";");

        statusBox.getChildren().addAll(statusTitle, statusBadge);

        Button upgradeBtn = new Button("⭐ Premium-a Yüksəlt");
        upgradeBtn.setMaxWidth(Double.MAX_VALUE);
        upgradeBtn.setStyle("-fx-background-color: #f59e0b; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        Label upgradeMsg = new Label();
        upgradeMsg.setStyle("-fx-font-size: 12px;");

        if (isPremium) {
            upgradeBtn.setText("✔ Hesabınız Artıq Premiumdur");
            upgradeBtn.setDisable(true);
            upgradeBtn.setStyle("-fx-background-color: #e2e8f0; -fx-text-fill: #94a3b8; -fx-font-weight: bold; -fx-padding: 10; -fx-background-radius: 6;");
        }

        upgradeBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Abunəliyi Yüksəlt");
            confirm.setHeaderText("Premium Üzvlüyə Keçid");
            confirm.setContentText("Bütün qapalı xülasələrə, redaktə və silmə imkanlarına çıxış əldə etmək üçün Premium hesaba keçmək istəyirsiniz?");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean success = UserRepository.getInstance().updateSubscription(user.getUsername(), "PREMIUM");
                    if (success) {
                        user.setSubscriptionType("PREMIUM");
                        statusBadge.setText("PREMIUM");
                        statusBadge.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #f59e0b;");

                        upgradeBtn.setText("✔ Hesabınız Artıq Premiumdur");
                        upgradeBtn.setDisable(true);
                        upgradeBtn.setStyle("-fx-background-color: #e2e8f0; -fx-text-fill: #94a3b8; -fx-font-weight: bold; -fx-padding: 10; -fx-background-radius: 6;");

                        upgradeMsg.setStyle("-fx-text-fill: #10b981;");
                        upgradeMsg.setText("Təbriklər! Hesabınız uğurla Premium-a yüksəldildi.");

                        if (onSubscriptionUpdated != null) {
                            onSubscriptionUpdated.run();
                        }
                    }
                }
            });
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button closeBtn = new Button("Bağla");
        closeBtn.setMaxWidth(Double.MAX_VALUE);
        closeBtn.setStyle("-fx-background-color: #f1f5f9; -fx-text-fill: #334155; -fx-padding: 8; -fx-background-radius: 6; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(avatarLabel, nameLabel, usernameLabel, statusBox, upgradeBtn, upgradeMsg, spacer, closeBtn);

        Scene scene = new Scene(root, 340, 380);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.showAndWait();
    }
}