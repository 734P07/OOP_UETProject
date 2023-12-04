package com.btl;

import com.btl.SpeedWord.Core.Engine;

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
import javafx.scene.web.WebView;
import javafx.concurrent.Task;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private Label homeDailyWord;

    @FXML
    private Label homeDayStreak;

    @FXML
    private Label homeSearchedWords;

    @FXML
    private AnchorPane home_form;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button open_speedwordBtn;

    @FXML
    private WebView searchAntonyms;

    @FXML
    private Button searchBtn;

    @FXML
    private WebView searchDefinition;

    @FXML
    private WebView searchExample;

    @FXML
    private Label searchPhoneticUK;

    @FXML
    private Label searchPhoneticUS;

    @FXML
    private Button searchSpeakerUK;

    @FXML
    private Button searchSpeakerUS;

    @FXML
    private WebView searchSynonyms;

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
    private Label username;
    
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    private double x = 0;
    private double y = 0;

    private static menuController instance;

    public static menuController getInstance() {
        if (instance == null) {
            instance = new menuController();
        }
        return instance;
    }
    
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
    
    public void homeDisplayDayStreak() {
        Task<String> task = new Task<>() {
            protected String call() throws Exception {
                String sql = "SELECT DATEDIFF(CURDATE(), lastDay), dayStreak FROM day_streak WHERE username = ?";

                connect = Database.connectDb();

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, getAccountData.username);

                result = prepare.executeQuery();

                if(result.next()) {
                    int dayDiff = result.getInt(1);
                    int dayStreak = result.getInt(2);
                    
                    if(dayDiff == 1) {
                        prepare = connect.prepareStatement("UPDATE day_streak SET lastDay = CURDATE(), "
                            + "dayStreak = dayStreak + 1 WHERE username = ?");
                        prepare.setString(1, getAccountData.username);
                        prepare.executeUpdate();
                        dayStreak++;
                    } else if(dayDiff != 0) {
                        prepare = connect.prepareStatement("UPDATE day_streak SET lastDay = CURDATE(), "
                            + "dayStreak = 1 WHERE username = ?");
                        prepare.setString(1, getAccountData.username);
                        prepare.executeUpdate();
                        dayStreak = 1;
                    }
                    
                    return Integer.toString(dayStreak);
                }
                
                prepare = connect.prepareStatement("INSERT INTO `day_streak`(`lastDay`, `dayStreak`, `username`)"
                        + " VALUES (CURDATE(),1,?)");
                prepare.setString(1, getAccountData.username);
                prepare.executeUpdate();
                
                return "1";
            }
        };    
                
        task.setOnSucceeded(e -> {
            homeDayStreak.setText(task.getValue());
        });
        
        task.setOnFailed(e -> {
             System.out.println("Day Streak Failed");
        });
        
        new Thread(task).start();
    }
    
    public void homeDisplaySearchedWords() {
        Task<String> task = new Task<>() {
            protected String call() throws Exception {
                connect = Database.connectDb();

                prepare = connect.prepareStatement("SELECT COUNT(word) FROM searched_words WHERE username = ?");
                prepare.setString(1, getAccountData.username);

                result = prepare.executeQuery();

                if(result.next()) {
                    return Integer.toString(result.getInt(1));
                }
                
                return "0";
            }
        };    
                
        task.setOnSucceeded(e -> {
            homeSearchedWords.setText(task.getValue());
        });
        
        task.setOnFailed(e -> {
             System.out.println("Get Search Words Failed");
        });
        
        new Thread(task).start();
    }
    
    public void homeDisplayDailyWord() {
        String dailyWord = RandomWordGetAPI.getSentRequest().getARandomWord();
        homeDailyWord.setText(dailyWord.substring(0, 1).toUpperCase()
            + dailyWord.substring(1));
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
    
    public void searchSearch() throws Exception {
        
        searchSpeakerUK.setVisible(true);
        searchSpeakerUS.setVisible(true);
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
        
        StringBuilder sb1 = new StringBuilder()
                .append("<html>")
                .append("<body>")
                .append("<h1>Definitions</h1>");
        StringBuilder sb2 = new StringBuilder()
                .append("<html>")
                .append("<body>")
                .append("<h1>Examples</h1>");
        StringBuilder sb3 = new StringBuilder()
                .append("<html>")
                .append("<body>")
                .append("<h1>Synonyms</h1>");
        StringBuilder sb4 = new StringBuilder()
                .append("<html>")
                .append("<body>")
                .append("<h1>Antonyms</h1>");
        
        for (WordTranscript.meaning meaning : wordTranscript.meanings) {
            sb1.append("<h2>" + meaning.partOfSpeech + "</h2>");
            for (WordTranscript.definition definition : meaning.definitions) {
                //appendTextLn(searchDefinition, translate(Language.en, Language.vi,definition.definition));
                sb1.append("<p>" + definition.definition + "</p>");
                if(definition.example != null)
                    sb2.append("<p>" + definition.example + "</p>");
            }
            for(String synonym : meaning.synonyms) {
                //appendTextLn(searchSynonyms, synonym);
                sb3.append("<p>" + synonym + "</p>");
            }
            for(String antonym : meaning.antonyms) {
                //appendTextLn(searchAntonyms, antonym);
                sb4.append("<p>" + antonym + "</p>");
            }
        }
        
        sb1.append("</body></html>");
        sb2.append("</body></html>");
        sb3.append("</body></html>");
        sb4.append("</body></html>");
        
        searchDefinition.getEngine().loadContent(sb1.toString());
        searchExample.getEngine().loadContent(sb2.toString());
        searchSynonyms.getEngine().loadContent(sb3.toString());
        searchAntonyms.getEngine().loadContent(sb4.toString());
        
        searchDefinition.getEngine().
                setUserStyleSheetLocation(getClass().getResource("searchDesign.css").toString());
        searchExample.getEngine().
                setUserStyleSheetLocation(getClass().getResource("searchDesign.css").toString());
        searchSynonyms.getEngine().
                setUserStyleSheetLocation(getClass().getResource("searchDesign.css").toString());
        searchAntonyms.getEngine().
                setUserStyleSheetLocation(getClass().getResource("searchDesign.css").toString());
        
        Task<Boolean> task = new Task<>() {
            protected Boolean call() throws Exception {
                connect = Database.connectDb();

                prepare = connect.prepareStatement("SELECT word FROM searched_words WHERE username = ? AND word = ?");
                prepare.setString(1, getAccountData.username);
                prepare.setString(2, word);

                result = prepare.executeQuery();

                if(!result.next()) {
                    prepare = connect.prepareStatement("INSERT INTO `searched_words`(`word`, `username`, `day`) "
                            + "VALUES (?,?,CURDATE())");
                    prepare.setString(1, word);
                    prepare.setString(2, getAccountData.username);
                    prepare.executeUpdate();
                }
                return true;
            }
        };
        
        new Thread(task).start();
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
            
            homeDisplaySearchedWords();
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
        
        homeDisplayDailyWord();
        homeDisplayDayStreak();
        homeDisplaySearchedWords();
        
        searchSpeakerUK.setVisible(false);
        searchSpeakerUS.setVisible(false);
    }

    /**
     * Mở game Speed Word.
     */
    public void startSpeedWord() {
        Engine.getEngine().start();
        open_speedwordBtn.setDisable(true);
        System.out.println("SpeedWord start!");
    }

    public void turnOnSpeedWordBtn() {
        open_speedwordBtn.setDisable(false);
    }
}
