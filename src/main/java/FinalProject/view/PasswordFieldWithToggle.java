package FinalProject.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class PasswordFieldWithToggle extends StackPane {

    private final PasswordField passwordField = new PasswordField();
    private final TextField textField = new TextField();
    private final Button toggleButton = new Button("👁");

    public PasswordFieldWithToggle(String promptText) {
        passwordField.setPromptText(promptText);
        textField.setPromptText(promptText);

        textField.setVisible(false);
        textField.setManaged(false);

        passwordField.textProperty().bindBidirectional(textField.textProperty());

        toggleButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 13px; -fx-padding: 0 8 0 0;");
        StackPane.setAlignment(toggleButton, Pos.CENTER_RIGHT);

        toggleButton.setOnAction(e -> {
            boolean showing = textField.isVisible();
            if (showing) {
                textField.setVisible(false);
                textField.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);
                toggleButton.setText("👁");
                passwordField.requestFocus();
                passwordField.positionCaret(passwordField.getText().length());
            } else {
                passwordField.setVisible(false);
                passwordField.setManaged(false);
                textField.setVisible(true);
                textField.setManaged(true);
                toggleButton.setText("🙈");
                textField.requestFocus();
                textField.positionCaret(textField.getText().length());
            }
        });

        getChildren().addAll(passwordField, textField, toggleButton);
        setMaxWidth(280);
    }

    public String getText() {
        return passwordField.getText();
    }

    public void clear() {
        passwordField.clear();
        textField.clear();
    }
}