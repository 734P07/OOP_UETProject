package com.btl;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javax.mail.MessagingException;


public class loginController implements Initializable{

    @FXML
    private Button close;

    @FXML
    private Button closeButton;

    @FXML
    private Label confirmPasswordLabel;

    @FXML
    private PasswordField confirm_password;

    @FXML
    private Button createAccountBtn;

    @FXML
    private Button createButton;

    @FXML
    private TextField create_email;

    @FXML
    private AnchorPane create_form;

    @FXML
    private PasswordField create_password;

    @FXML
    private TextField create_username;

    @FXML
    private Button forgotPassBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private AnchorPane loginRight_form;
    
    @FXML 
    private AnchorPane forgotPassword_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;
    
    @FXML
    private Button forgotPassword_close;

    @FXML
    private Button forgotPassword_confirm;
    
    @FXML
    private TextField emailForgot_text;
    
    @FXML
    private AnchorPane verifyCode_form;
    
    @FXML
    private TextField verifyCode_text;
    
    @FXML
    private Button verifyCode_Btn;
    
    @FXML
    private Button backToForgotPw;
    
    @FXML
    private AnchorPane resetPassword_form;
    
    @FXML
    private AnchorPane verifyCreateAccount_form;
    
    @FXML
    private TextField verifyCreateCode_text;
    
    @FXML
    private PasswordField newPassword_text;
    
    @FXML
    private PasswordField confirmNewPassword_text;
    
    @FXML
    private Button resetPassword_Btn;
    
    @FXML 
    private Button verifyCreateCode_Btn;
    
    @FXML 
    private Button backToCreateAccount;
    
    //Database tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    private String verificationCode;
    private String verifyCreateAccount;
    
    private double x = 0;
    private double y = 0;
    
    /**
     * control login feature.
     */
    public void loginController() throws IOException {
        Stage loadingStage = new Stage();
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefSize(300, 30);
        VBox container = new VBox(progressBar);
        container.setAlignment(Pos.CENTER);
        Scene new_scene = new Scene(new Group(progressBar),300,30);
        loadingStage.setScene(new_scene);
        loadingStage.setTitle("Loading...");
        loadingStage.setMinWidth(300);
        loadingStage.setMinHeight(30);
        loadingStage.show();
        
        Task<Boolean> loginTask = new Task<Boolean>() {
            protected Boolean call() throws Exception {
                String sql = "SELECT * FROM account_data WHERE username = ? and password = ?";

                connect = Database.connectDb();

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, username.getText());
                prepare.setString(2, password.getText());

                result = prepare.executeQuery();

                if(username.getText().isEmpty() || password.getText().isEmpty()){
                    showAlert(AlertType.ERROR, "Error Message","Please fill all blank fields");
                }else{ 
                    return result.next();
                }
                return false;
            }
        };    
                
        loginTask.setOnSucceeded(e -> {
            loadingStage.close();
            if (loginTask.getValue()) {
                try {
                    getAccountData.username = username.getText();

                    loginBtn.getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    root.setOnMousePressed((MouseEvent event) ->{
                        x = event.getSceneX();
                        y = event.getSceneY();
                    });

                    root.setOnMouseDragged((MouseEvent event) ->{
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);
                    });

                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
            } else {
                showAlert(AlertType.ERROR, "Error Message","Wrong Username/Password");
            }
        });
        
        loginTask.setOnFailed(e -> {
            loadingStage.close();
            Throwable throwable = loginTask.getException();
            if (throwable instanceof IllegalArgumentException) {
                showAlert(AlertType.ERROR, "Error Message",throwable.getMessage());
            } else {
                showAlert(AlertType.ERROR, "Error Message","Please filled in all blanks");
            }
        });
        
        new Thread(loginTask).start();
    }
    
    public void creatButtonAction(ActionEvent event) {
        if (create_username.getText().isEmpty() || create_password.getText().isEmpty() || confirm_password.getText().isEmpty() || create_email.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Error Message","Please filled in all blanks!");
        } else {
            if (!create_password.getText().equals(confirm_password.getText())) {
                confirmPasswordLabel.setText("Password does not match");
                confirmPasswordLabel.setStyle("-fx-text-fill: red;");
                confirmPasswordLabel.setVisible(true);
                return;
            } else if(!create_email.getText().endsWith("@gmail.com")) {
                showAlert(AlertType.ERROR, "Error Message","Your email is invalid");
                return;
            } else {
                confirmPasswordLabel.setText("Password Match");
                confirmPasswordLabel.setVisible(true);
                confirmPasswordLabel.setStyle("-fx-text-fill: green;");
                
                Stage loadingStage = new Stage();
                ProgressBar progressBar = new ProgressBar();
                progressBar.setPrefSize(300, 30);
                VBox container = new VBox(progressBar);
                container.setAlignment(Pos.CENTER);
                Scene new_scene = new Scene(new Group(progressBar),300,30);
                loadingStage.setScene(new_scene);
                loadingStage.setTitle("Loading...");
                loadingStage.setMinWidth(300);
                loadingStage.setMinHeight(30);
                loadingStage.show();
                
                Task<Boolean> checkUserTask = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        connect = Database.connectDb();
                        String checkUser = "SELECT * FROM account_data WHERE username = ? OR email = ?";
                        prepare = connect.prepareStatement(checkUser);
                        prepare.setString(1, create_username.getText());
                        prepare.setString(2, create_email.getText());
                        result = prepare.executeQuery();
                        return result.next();
                    }
                };

                checkUserTask.setOnSucceeded(e -> {
                    if (checkUserTask.getValue()) {
                        showAlert(AlertType.ERROR, "Error Message", "Username or Email already exists!");
                    } else {
                        showAlert(AlertType.INFORMATION, "Email Sent", "Verify Code was sent to your email!");
                        create_form.setVisible(false);
                        verifyCreateAccount_form.setVisible(true);
                        loadingStage.hide();
                        try {
                            sendVerificationCreate(create_email.getText());
                        } catch (MessagingException f) {
                            f.printStackTrace();
                        }
                    }
                });

                checkUserTask.setOnFailed(e -> {
                    Throwable throwable = checkUserTask.getException();
                    showAlert(AlertType.ERROR, "Error Message", throwable.getMessage());
                });

                new Thread(checkUserTask).start();
            }
        }
    }
    
    public void sendVerificationCreate(String email) throws MessagingException {
        this.verifyCreateAccount = generateVerificationCode();
        String subject = "Your Verification Code";
        String text = "Your code is: " + this.verifyCreateAccount;
        EmailSender.sendEmail(email, subject, text);
    }
    
    public void addUserToDB() {
        Stage loadingStage = new Stage();
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefSize(300, 30);
        VBox container = new VBox(progressBar);
        container.setAlignment(Pos.CENTER);
        Scene new_scene = new Scene(new Group(progressBar),300,30);
        loadingStage.setScene(new_scene);
        loadingStage.setTitle("Loading...");
        loadingStage.setMinWidth(300);
        loadingStage.setMinHeight(30);
        loadingStage.show();
        
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (!verifyCreateAccount.equals(verifyCreateCode_text.getText())) {
                    Platform.runLater(() -> showAlert(AlertType.ERROR, "Verification Code Error", "Your verification code does not match"));
                    loadingStage.hide();
                } else {
                    connect = Database.connectDb();
                    String insertUser = "INSERT INTO account_data (username, password, email) VALUES (?, ?, ?)";
                    prepare = connect.prepareStatement(insertUser);
                    prepare.setString(1, create_username.getText());
                    prepare.setString(2, create_password.getText());
                    prepare.setString(3, create_email.getText());
                    prepare.executeUpdate();
                    Platform.runLater(() -> {
                        showAlert(AlertType.INFORMATION, "Account Created", "Your account has been successfully created.");
                        resetCreateAccountForm();
                        loadingStage.hide();
                    });
                }
                return null;
            }
        };
        task.setOnFailed(e -> {
            Throwable throwable = task.getException();
            throwable.printStackTrace();
            Platform.runLater(() -> showAlert(AlertType.ERROR, "Database Error", "Error while creating account."));
            loadingStage.hide();
        });
        new Thread(task).start();
    }

    public void resetCreateAccountForm() {
        create_username.clear();
        create_password.clear();
        confirm_password.clear();
        create_email.clear();
        confirmPasswordLabel.setVisible(false);
        loginRight_form.setVisible(true);
        create_form.setVisible(false);
        verifyCreateAccount_form.setVisible(false);
    }
    
    public void forgotPassword_Btn(ActionEvent event) {
        String email = emailForgot_text.getText();
        if (email.isEmpty()) {
            showAlert(AlertType.ERROR, "Error Message", "Please fill in the email field!");
        } else {
            isEmailExists(email);
        }
    }

    public void isEmailExists(String email) {
        Stage loadingStage = new Stage();
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefSize(300, 30);
        VBox container = new VBox(progressBar);
        container.setAlignment(Pos.CENTER);
        Scene new_scene = new Scene(new Group(progressBar),300,30);
        loadingStage.setScene(new_scene);
        loadingStage.setTitle("Loading...");
        loadingStage.setMinWidth(300);
        loadingStage.setMinHeight(30);
        loadingStage.show();
        
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                connect = Database.connectDb();
                String checkEmail = "SELECT * FROM account_data WHERE email = ?";
                prepare = connect.prepareStatement(checkEmail);
                prepare.setString(1, email);
                result = prepare.executeQuery();
                return result.next();
            }
        };
        task.setOnSucceeded(e -> {
            boolean exists = task.getValue();
            boolean _visible = true;
            if (exists) {
                try {
                    sendVerificationEmail(email);
                } catch (MessagingException me) {
                    Platform.runLater(() -> showAlert(AlertType.ERROR, "Email Sending Failed", "Failed to send verification email."));
                    _visible = false;
                    loadingStage.hide();
                }
            } else {
                Platform.runLater(() -> showAlert(AlertType.ERROR, "Error", "Email does not exist!"));
                loadingStage.hide();
            }
            if (_visible){
                Platform.runLater(() -> showAlert(AlertType.INFORMATION, "Email Sent", "Verification email has been sent."));
                forgotPassword_form.setVisible(false);
                verifyCode_form.setVisible(true);
                loadingStage.hide();
            }
        });
        task.setOnFailed(e -> {
            Throwable throwable = task.getException();
            throwable.printStackTrace();
            Platform.runLater(() -> showAlert(AlertType.ERROR, "Database Error", "Error checking email existence."));
        });
        new Thread(task).start();
    }

    
    public String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    
    public void sendVerificationEmail(String email) throws MessagingException {
        this.verificationCode = generateVerificationCode();
        String subject = "Your Verification Code";
        String text = "Your code is: " + this.verificationCode;
        EmailSender.sendEmail(email, subject, text);
    }
    
    public void verifyCode_Btn_Action(ActionEvent event) {
        String inputCode = this.verifyCode_text.getText();
        if (inputCode.equals(this.verificationCode)) {
            verifyCode_form.setVisible(false);
            resetPassword_form.setVisible(true);
        } else {
            showAlert(AlertType.ERROR, "Error Message","The verification code is not valid.");
        }
    }
    
    public void resetPasswordBtn_Action(ActionEvent event) {
        String newPw = newPassword_text.getText();
        String cfNewPw = confirmNewPassword_text.getText();
        
        if (newPw.isEmpty() || cfNewPw.isEmpty()) {
            showAlert(AlertType.ERROR, "Error Message","Please filled in all blanks");
        } else if (!newPw.equals(cfNewPw)) {
            showAlert(AlertType.ERROR, "Error Message","Password does not match");
        } else {
            try {
                resetPasswordDB(emailForgot_text.getText(), newPw);
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Error Message", "Failed to reset password!");
            }
        }
    }
    
    public void resetPasswordDB(String email, String newPw) {
        Stage loadingStage = new Stage();
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefSize(300, 30);
        VBox container = new VBox(progressBar);
        container.setAlignment(Pos.CENTER);
        Scene new_scene = new Scene(new Group(progressBar),300,30);
        loadingStage.setScene(new_scene);
        loadingStage.setTitle("Loading...");
        loadingStage.setMinWidth(300);
        loadingStage.setMinHeight(30);
        loadingStage.show();
        
        
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                connect = Database.connectDb();
                String sql = "UPDATE account_data SET password = ? WHERE email = ?";
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, newPw);
                prepare.setString(2, email);
                prepare.executeUpdate();
                return null;
            }
        };
        
        task.setOnSucceeded(e -> Platform.runLater(() -> {
            loadingStage.hide();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Successfully");
            alert.setHeaderText(null);
            alert.setContentText("Password successfully reset");

            alert.setOnHidden(evt -> {
                resetPassword_form.setVisible(false);
                loginRight_form.setVisible(true);
            });

            alert.showAndWait();
        }));
        task.setOnFailed(e -> {
            Throwable throwable = task.getException();
            throwable.printStackTrace();
            Platform.runLater(() -> showAlert(AlertType.ERROR, "Error Message", "Failed to reset password!"));
            loadingStage.hide();
        });
        new Thread(task).start();
    }


    public void loginCreateAccount() {
        loginRight_form.setVisible(false);
        create_form.setVisible(true);
        forgotPassword_form.setVisible(false);
    }
    
    public void forgotPasswordAccount() {
        loginRight_form.setVisible(false);
        create_form.setVisible(false);
        forgotPassword_form.setVisible(true);
    }
    
    public void createClose() {
        loginRight_form.setVisible(true);
        create_form.setVisible(false);
    }
    
    public void forgotPasswordClose() {
        loginRight_form.setVisible(true);
        forgotPassword_form.setVisible(false);
    }
    
    public void backtoForgotPw_form() {
        verifyCode_form.setVisible(false);
        forgotPassword_form.setVisible(true);
    }
    
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public void closeVerifyCreate_form() {
        create_form.setVisible(true);
        verifyCreateAccount_form.setVisible(false);
    }
 
    /**
     *  close program.
     */
    public void close() {
        System.exit(0);
    }

    /**
     * .
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginRight_form.setVisible(true);
        create_form.setVisible(false);
        confirmPasswordLabel.setVisible(false);
        verifyCreateAccount_form.setVisible(false);
        
        confirm_password.textProperty().addListener((observable,oldValue,newValue) -> {
            if(create_password.getText().equals(newValue)) {
                confirmPasswordLabel.setText("Password Match");
                confirmPasswordLabel.setStyle("-fx-text-fill: green;");
            } else {
                confirmPasswordLabel.setText("Password does not match");
                confirmPasswordLabel.setStyle("-fx-text-fill: red;");
            }
        });
    }
}
