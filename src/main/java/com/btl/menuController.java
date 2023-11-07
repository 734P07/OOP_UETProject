package com.btl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

public class menuController implements Initializable {
    @FXML
    private Button exitBtn;

    @FXML
    private AnchorPane home_form;

    @FXML
    private AnchorPane main_form;

    @FXML
    private AnchorPane search_form;

    @FXML
    private Button signoutBtn;

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
    
    private static final String CLIENT_ID = "FREE_TRIAL_ACCOUNT";
    private static final String CLIENT_SECRET = "PUBLIC_SECRET";
    private static final String ENDPOINT = "http://api.whatsmate.net/v1/translation/translate";
    
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
     */
    public void translateTranslate() throws Exception {
        String fromLang;
        String toLang;
        
        if(translate_beforeLanguage.getText().equals("English")){
            fromLang = "en";
            toLang = "vi";
        } else {
            fromLang = "vi";
            toLang = "en";
        }
        String text = translate_beforeTranslate.getText();

        translate(fromLang, toLang, text);
    }
    
    public void translate(String fromLang, String toLang, String text) throws Exception {
        String jsonPayload = new StringBuilder()
            .append("{")
            .append("\"fromLang\":\"")
            .append(fromLang)
            .append("\",")
            .append("\"toLang\":\"")
            .append(toLang)
            .append("\",")
            .append("\"text\":\"")
            .append(text)
            .append("\"")
            .append("}")
            .toString();

        URL url = new URL(ENDPOINT);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("X-WM-CLIENT-ID", CLIENT_ID);
        conn.setRequestProperty("X-WM-CLIENT-SECRET", CLIENT_SECRET);
        conn.setRequestProperty("Content-Type", "application/json");

        OutputStream os = conn.getOutputStream();
        os.write(jsonPayload.getBytes());
        os.flush();
        os.close();

        int statusCode = conn.getResponseCode();
        //System.out.println("Status Code: " + statusCode);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(
            (statusCode == 200) ? conn.getInputStream() : conn.getErrorStream()
        ));
        String output;
        while ((output = br.readLine()) != null) {
            this.translate_afterTranslate.setText(output);
        }
        conn.disconnect();
    }
    
    /**
     * initialize.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        displayUsername();
    }
}
