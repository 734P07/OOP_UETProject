package com.btl;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class loginController implements Initializable{

    @FXML
    private Label CreationMessageLabel;

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
    
    //Database tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    private double x = 0;
    private double y = 0;
    
    /**
     * control login feature.
     */
    public void loginController() {
        String sql = "SELECT * FROM account_data WHERE username = ? and password = ?";
        
        connect = Database.connectDb();
        
        try{
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());
            
            result = prepare.executeQuery();
            Alert alert;
            
            if(username.getText().isEmpty() || password.getText().isEmpty()){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                if(result.next()){
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
                    
                }else{
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username/Password");
                    alert.showAndWait();
                }
            }
            
        }catch(Exception e){e.printStackTrace();}
    }
    
    public void creatButtonAction(ActionEvent event) {
        if (create_username.getText().isEmpty() || create_password.getText().isEmpty() || confirm_password.getText().isEmpty() || create_email.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please filled in all blanks!");
            alert.showAndWait();
            CreationMessageLabel.setVisible(true);
            CreationMessageLabel.setText("Failed to create account");
        } else {
            if (create_password.getText().equals(confirm_password.getText())) {
                confirmPasswordLabel.setText("Valid Password");
                CreationMessageLabel.setVisible(true);
                CreationMessageLabel.setText("Succeed create account");
            } else {
                confirmPasswordLabel.setText("Password does not match");
                CreationMessageLabel.setVisible(true);
                CreationMessageLabel.setText("Failed to create account");
            }
            confirmPasswordLabel.setVisible(true);
        }
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
        CreationMessageLabel.setVisible(false);
    }
}
