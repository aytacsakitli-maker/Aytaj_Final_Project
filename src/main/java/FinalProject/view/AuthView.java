package FinalProject.view;

import FinalProject.User;
import FinalProject.service.UserService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class AuthView {

    private final UserService userService;
    private final Consumer<User> onLoginSuccess;

    public AuthView(UserService userService, Consumer<User> onLoginSuccess) {
        this.userService = userService;
        this.onLoginSuccess = onLoginSuccess;
    }

    public void show(Stage stage) {
        stage.setTitle("Ağıllı Kitabxana - Giriş");

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab loginTab = new Tab("Daxil Ol");
        VBox loginBox = new VBox(12);
        loginBox.setPadding(new Insets(25));
        loginBox.setAlignment(Pos.CENTER);

        TextField loginUserField = new TextField();
        loginUserField.setPromptText("İstifadəçi adı");
        loginUserField.setMaxWidth(280);

        PasswordFieldWithToggle loginPassField = new PasswordFieldWithToggle("Şifrə");

        Label loginStatus = new Label();
        loginStatus.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 12px;");

        Button loginBtn = new Button("Sistemə Giriş");
        loginBtn.setMaxWidth(280);
        loginBtn.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 9; -fx-background-radius: 6; -fx-cursor: hand;");

        Hyperlink forgotPasswordLink = new Hyperlink("Şifrəni unutmusunuz? Yeniləyin");
        forgotPasswordLink.setStyle("-fx-text-fill: #2563eb; -fx-font-size: 12px; -fx-underline: true;");
        forgotPasswordLink.setVisible(false);
        forgotPasswordLink.setManaged(false);

        loginBtn.setOnAction(e -> {
            String username = loginUserField.getText().trim();
            String password = loginPassField.getText().trim();

            User user = userService.login(username, password);
            if (user != null) {
                onLoginSuccess.accept(user);
            } else {
                loginStatus.setText("İstifadəçi adı və ya şifrə yanlışdır!");
                forgotPasswordLink.setVisible(true);
                forgotPasswordLink.setManaged(true);
            }
        });

        forgotPasswordLink.setOnAction(e -> {
            openResetPasswordModal(stage, loginUserField.getText().trim());
        });

        loginBox.getChildren().addAll(
                new Label("Mövcud Hesab"),
                loginUserField,
                loginPassField,
                loginStatus,
                loginBtn,
                forgotPasswordLink
        );
        loginTab.setContent(loginBox);

        Tab registerTab = new Tab("Qeydiyyat");
        VBox registerBox = new VBox(10);
        registerBox.setPadding(new Insets(20));
        registerBox.setAlignment(Pos.CENTER);

        TextField regFullNameField = new TextField();
        regFullNameField.setPromptText("Ad və Soyad");
        regFullNameField.setMaxWidth(280);

        TextField regUsernameField = new TextField();
        regUsernameField.setPromptText("İstifadəçi adı");
        regUsernameField.setMaxWidth(280);

        PasswordFieldWithToggle regPassField = new PasswordFieldWithToggle("Şifrə");

        ComboBox<String> subTypeCombo = new ComboBox<>();
        subTypeCombo.getItems().addAll("FREE", "PREMIUM");
        subTypeCombo.setValue("FREE");
        subTypeCombo.setMaxWidth(280);

        Label regStatus = new Label();
        regStatus.setStyle("-fx-font-size: 12px;");

        Button registerBtn = new Button("Hesab Yarat");
        registerBtn.setMaxWidth(280);
        registerBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8; -fx-background-radius: 6; -fx-cursor: hand;");

        registerBtn.setOnAction(e -> {
            String name = regFullNameField.getText().trim();
            String user = regUsernameField.getText().trim();
            String pass = regPassField.getText().trim();
            String sub = subTypeCombo.getValue();

            if (name.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                regStatus.setStyle("-fx-text-fill: #ef4444;");
                regStatus.setText("Bütün xanaları doldurun!");
                return;
            }

            boolean ok = userService.register(user, name, pass, sub);
            if (ok) {
                regStatus.setStyle("-fx-text-fill: #10b981;");
                regStatus.setText("Uğurla yaradıldı! İndi 'Daxil Ol' bölməsinə keçin.");
                regFullNameField.clear();
                regUsernameField.clear();
                regPassField.clear();
            } else {
                regStatus.setStyle("-fx-text-fill: #ef4444;");
                regStatus.setText("Bu istifadəçi adı artıq mövcuddur!");
            }
        });

        registerBox.getChildren().addAll(
                new Label("Yeni Abunəlik"),
                regFullNameField,
                regUsernameField,
                regPassField,
                new Label("Abunəlik Növü:"),
                subTypeCombo,
                regStatus,
                registerBtn
        );
        registerTab.setContent(registerBox);

        tabPane.getTabs().addAll(loginTab, registerTab);

        Scene scene = new Scene(tabPane, 380, 420);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void openResetPasswordModal(Stage ownerStage, String prefilledUsername) {
        Stage modal = new Stage();
        modal.initModality(Modality.WINDOW_MODAL);
        modal.initOwner(ownerStage);
        modal.setTitle("Şifrənin Yenilənməsi");

        VBox root = new VBox(12);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ffffff;");

        Label modalTitle = new Label("Yeni Şifrə Təyin Edin");
        modalTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

        TextField usernameField = new TextField(prefilledUsername);
        usernameField.setPromptText("İstifadəçi adı");
        usernameField.setMaxWidth(260);

        PasswordFieldWithToggle newPasswordField = new PasswordFieldWithToggle("Yeni şifrə");

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 12px;");

        Button submitBtn = new Button("Şifrəni Dəyiş");
        submitBtn.setMaxWidth(260);
        submitBtn.setStyle("-fx-background-color: #f59e0b; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8; -fx-cursor: hand;");

        submitBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String newPass = newPasswordField.getText().trim();

            if (username.isEmpty() || newPass.isEmpty()) {
                statusLabel.setStyle("-fx-text-fill: #ef4444;");
                statusLabel.setText("Bütün xanaları doldurun!");
                return;
            }

            if (!userService.userExists(username)) {
                statusLabel.setStyle("-fx-text-fill: #ef4444;");
                statusLabel.setText("Belə bir istifadəçi adı tapılmadı!");
                return;
            }

            boolean updated = userService.updatePassword(username, newPass);
            if (updated) {
                statusLabel.setStyle("-fx-text-fill: #10b981;");
                statusLabel.setText("Şifrə uğurla yeniləndi! Pəncərə bağlanır...");
                new Thread(() -> {
                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                    javafx.application.Platform.runLater(modal::close);
                }).start();
            } else {
                statusLabel.setStyle("-fx-text-fill: #ef4444;");
                statusLabel.setText("Xəta baş verdi!");
            }
        });

        root.getChildren().addAll(modalTitle, usernameField, newPasswordField, statusLabel, submitBtn);

        Scene modalScene = new Scene(root, 320, 260);
        modal.setScene(modalScene);
        modal.centerOnScreen();
        modal.showAndWait();
    }
}