package com.btl;

import com.btl.SpeedWord.Engine;

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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

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
    private TextArea searchAntonyms;

    @FXML
    private Button searchBtn;

    @FXML
    private TextArea searchDefinition;

    @FXML
    private TextArea searchExample;

    @FXML
    private Label searchPhoneticUK;

    @FXML
    private Label searchPhoneticUS;

    @FXML
    private Button searchSpeakerUK;

    @FXML
    private Button searchSpeakerUS;

    @FXML
    private TextArea searchSynonyms;

    @FXML
    private Label searchWord;

    @FXML
    private AnchorPane search_form;

    @FXML
    private TextField search_searchBar;

    @FXML
    private Button search_searchBtn;

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
    private Button open_speedwordBtn;

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
     * send a GET request of dictionary api.
     * @param word a word that we need its information.
     * @return information of the word.
     */
    public WordTranscript sendGetDictionaryRequest(String word){
        
        ArrayList<WordTranscript> wordTranscripts = null;
        
        try {
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://api.dictionaryapi.dev/api/v2/entries/en/" + word))
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
            
            System.out.println(getResponse.body());
            
            Type wordsType = new TypeToken<ArrayList<WordTranscript>>(){}.getType();
            Gson gson = new Gson();
            
            wordTranscripts = gson.fromJson(getResponse.body(), wordsType);
            
        } catch(Exception e) {}
        
        return wordTranscripts.get(0);
        
    }
    
    public void appendTextLn(TextArea textArea, String string) {
        if(string != null) {
            textArea.appendText(string + "\n");
        }
    }
    
    public void searchSearch() throws Exception {
        
        String word = search_searchBar.getText();
        
        WordTranscript wordTranscript = sendGetDictionaryRequest(word);
        
        searchWord.setText(wordTranscript.word.substring(0, 1).toUpperCase()
            + wordTranscript.word.substring(1));
        
        for(int i = 0; i < 6; i++) {
            
            if(wordTranscript.phonetics.get(i).text != null) {
                searchPhoneticUK.setText(wordTranscript.phonetics.get(i).text);
                try {
                    searchPhoneticUS.setText(wordTranscript.phonetics.get(i + 1).text);
                } catch (Exception e) {
                    searchPhoneticUS.setText(wordTranscript.phonetics.get(i).text);
                }
                break;
            }
        }
        
        searchDefinition.clear();
        searchExample.clear();
        searchSynonyms.clear();
        searchAntonyms.clear();
        
        for (WordTranscript.meaning meaning : wordTranscript.meanings) {
            appendTextLn(searchDefinition, meaning.partOfSpeech);
            for (WordTranscript.definition definition : meaning.definitions) {
                appendTextLn(searchDefinition, translate(Language.en, Language.vi,definition.definition));
                appendTextLn(searchExample, definition.example);
            }
            for(String synonym : meaning.synonyms) {
                appendTextLn(searchSynonyms, synonym);
            }
            for(String antonym : meaning.antonyms) {
                appendTextLn(searchAntonyms, antonym);
            }
        }
        
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
            for(String sentence : line.split("\\.")) {
                translate_afterTranslate.appendText(translate(fromLang, toLang, sentence));
                if(!sentence.equals(""))
                    translate_afterTranslate.appendText(". ");
            }
            translate_afterTranslate.appendText("\n");
        }
    }
    
    /**
     * translate feature.
     * @param fromLang translate from this language
     * @param toLang to this language
     * @param text string we need to translate
     * @return 
     * @throws Exception
     */
    public String translate(Language fromLang, Language toLang, String text) throws Exception {
        text = text.replaceAll("\\!\\s+", "!");
        text = text.replaceAll("\\?\\s+", "?");
        text = text.replaceAll("\\;\\s+", ";");
        text = text.replaceAll("\"", " ");
        
        SimpleGoogleTranslate translate = new SimpleGoogleTranslate();        
        String result = translate.doTranslate(fromLang, toLang, text);

        result = result.substring(result.lastIndexOf("\"") + 1);
        //System.out.println(result);
        if(!result.equals("N/A")) {
            return result;
        }
        return "";
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

    /**
     * Mở game Speed Word.
     */
    public void startSpeedWord() {
        Engine.getEngine().start();
        System.out.println("SpeedWord start!");
    }
}
