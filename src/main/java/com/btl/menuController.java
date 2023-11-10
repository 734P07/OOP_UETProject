package com.btl;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import app.jackychu.api.simplegoogletranslate.Language;
import app.jackychu.api.simplegoogletranslate.SimpleGoogleTranslate;
import javafx.event.ActionEvent;

public class menuController implements Initializable {
    @FXML
    private Button contactBtn;

    @FXML
    private AnchorPane contact_form;

    @FXML
    private Button exitBtn;

    @FXML
    private Button gamesBtn;

    @FXML
    private AnchorPane games_form;

    @FXML
    private Button homeBtn;

    @FXML
    private AnchorPane home_form;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button searchBtn;

    @FXML
    private AnchorPane search_form;

    @FXML
    private Button signoutBtn;

    @FXML
    private Button translateBtn;

    @FXML
    private Label translate_afterLanguage;

    @FXML
    private TextArea translate_afterTranslate;

    @FXML
    private Label translate_beforeLanguage;

    @FXML
    private TextArea translate_beforeTranslate;

    @FXML
    private AnchorPane translate_form;

    @FXML
    private Button translate_swapBtn;

    @FXML
    private Button translate_translateBtn;

    @FXML
    private Label username;

    
    private double x = 0;
    private double y = 0;
    
    /**
     *  close program.
     */
    public void close() {
        System.exit(0);
    }
    
    /**
     * sign out button.
     */
    public void signout() {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to sign out?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK)) {

                signoutBtn.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("loginScene.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * displayUsername.
     */
    public void displayUsername() {
        username.setText(getAccountData.username);
    }
    
    /**
     * translate button.
     * @throws java.lang.Exception
     */
    public void translateTranslate() throws Exception {
        Language fromLang;
        Language toLang;
        translate_afterTranslate.clear();
        
        if(translate_beforeLanguage.getText().equals("English")){
            fromLang = Language.en;
            toLang = Language.vi;
        } else {
            fromLang = Language.vi;
            toLang = Language.en;
        }
              
        for(String line : translate_beforeTranslate.getText().split("\\n")) {
            String[] sentences = line.split("[\\.\\!;?:\"]+");
            
            for(String sentence : sentences){
                translate(fromLang, toLang, sentence);
            }
            
            translate_afterTranslate.appendText("\n");
        }
    }
    
    /**
     * translate feature.
     * @param fromLang translate from this language
     * @param toLang to this language
     * @param text string we need to translate
     * @throws Exception
     */
    public void translate(Language fromLang, Language toLang, String text) throws Exception {
        SimpleGoogleTranslate translate = new SimpleGoogleTranslate();        
        String result = translate.doTranslate(fromLang, toLang, text);

        result = result.substring(result.lastIndexOf("\"") + 1);
        //System.out.println(result);
        if(!result.equals("N/A")) {
            translate_afterTranslate.appendText(result + ". ");
        }
    }
    
    /**
     * Swap button feature.
     */
    public void translateSwap() {
        String temp = translate_beforeLanguage.getText();
        translate_beforeLanguage.setText(translate_afterLanguage.getText());
        translate_afterLanguage.setText(temp);
        
        translate_beforeLanguage.setAlignment(Pos.BASELINE_CENTER);
        translate_afterLanguage.setAlignment(Pos.BASELINE_CENTER);
    }
    
    /**
     * switch form whenever you click the navigation button
     * @param event event catcher
     */
    public void switchForm(ActionEvent event) {
        
        home_form.setVisible(false);
        search_form.setVisible(false);
        translate_form.setVisible(false);
        games_form.setVisible(false);
        contact_form.setVisible(false);
        
        homeBtn.setStyle("-fx-background-color:transparent");
        searchBtn.setStyle("-fx-background-color:transparent");
        translateBtn.setStyle("-fx-background-color:transparent");
        gamesBtn.setStyle("-fx-background-color:transparent");
        contactBtn.setStyle("-fx-background-color:transparent");
        
        if (event.getSource() == homeBtn) {
            
            home_form.setVisible(true);
            homeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            
        } else if (event.getSource() == searchBtn) {

            search_form.setVisible(true);
            searchBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");

        } else if (event.getSource() == translateBtn) {
            
            translate_form.setVisible(true);
            translateBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");

        } else if (event.getSource() == gamesBtn) {
            
            games_form.setVisible(true);
            gamesBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");

        } else if (event.getSource() == contactBtn) {
            
            contact_form.setVisible(true);
            contactBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");

        }

    }
    
    /**
     * start navigation pane.
     */
    public void startNav() {
        
        home_form.setVisible(true);
        search_form.setVisible(false);
        translate_form.setVisible(false);
        games_form.setVisible(false);
        contact_form.setVisible(false);
        homeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            
    }
            
    /**
     * initialize.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        displayUsername();
        startNav();
        
    }
}
