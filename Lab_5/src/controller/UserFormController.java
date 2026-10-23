package controller;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import model.User;

/**
 *
 * @author simha
 */
public class UserFormController {

    private static final String NAME_PATTERN = "[A-Za-z]+([ '-][A-Za-z]+)*";
    private static final String EMAIL_PATTERN = "^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)*\\.[A-Za-z]{2,}$";

    @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField emailField;
    @FXML
    private Label nameError;
    @FXML
    private Label ageError;
    @FXML
    private Label emailError;

    @FXML
    private void initialize() {
        // Age field accepts digits only (max 3)
        ageField.setTextFormatter(new TextFormatter<String>(change
                -> change.getControlNewText().matches("\\d{0,3}") ? change : null));

        // Clear a field's error as soon as the user edits it
        nameField.textProperty().addListener((obs, oldText, newText) -> clearError(nameField, nameError));
        ageField.textProperty().addListener((obs, oldText, newText) -> clearError(ageField, ageError));
        emailField.textProperty().addListener((obs, oldText, newText) -> clearError(emailField, emailError));
    }

    @FXML
    private void handleSubmit() {
        List<String> errors = new ArrayList<>();
        TextField firstInvalid = null;

        String name = nameField.getText().trim();
        String nameMessage = validateName(name);
        if (nameMessage != null) {
            showError(nameField, nameError, nameMessage);
            errors.add(nameMessage);
            firstInvalid = nameField;
        }

        String ageText = ageField.getText().trim();
        String ageMessage = validateAge(ageText);
        if (ageMessage != null) {
            showError(ageField, ageError, ageMessage);
            errors.add(ageMessage);
            firstInvalid = firstInvalid == null ? ageField : firstInvalid;
        }

        String email = emailField.getText().trim();
        String emailMessage = validateEmail(email);
        if (emailMessage != null) {
            showError(emailField, emailError, emailMessage);
            errors.add(emailMessage);
            firstInvalid = firstInvalid == null ? emailField : firstInvalid;
        }

        if (!errors.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please fix the following:",
                    "• " + String.join("\n• ", errors));
            firstInvalid.requestFocus();
            return;
        }

        User user = new User(name, Integer.parseInt(ageText), email);
        showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Details entered:", user.toString());
    }

    @FXML
    private void handleClear() {
        nameField.clear();
        ageField.clear();
        emailField.clear();
        nameField.requestFocus();
    }

    private String validateName(String name) {
        if (name.isEmpty()) {
            return "Name is required.";
        }
        if (!name.matches(NAME_PATTERN)) {
            return "Name can only contain letters and spaces.";
        }
        if (name.length() < 2 || name.length() > 50) {
            return "Name must be 2 to 50 characters.";
        }
        return null;
    }

    private String validateAge(String ageText) {
        if (ageText.isEmpty()) {
            return "Age is required.";
        }
        int age = Integer.parseInt(ageText);
        if (age < 1 || age > 120) {
            return "Age must be between 1 and 120.";
        }
        return null;
    }

    private String validateEmail(String email) {
        if (email.isEmpty()) {
            return "Email is required.";
        }
        if (!email.matches(EMAIL_PATTERN)) {
            return "Enter a valid email (e.g. name@example.com).";
        }
        return null;
    }

    private void showError(TextField field, Label errorLabel, String message) {
        errorLabel.setText(message);
        if (!field.getStyleClass().contains("error-field")) {
            field.getStyleClass().add("error-field");
        }
    }

    private void clearError(TextField field, Label errorLabel) {
        errorLabel.setText("");
        field.getStyleClass().remove("error-field");
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.initOwner(nameField.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
