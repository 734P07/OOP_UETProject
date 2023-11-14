/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.btl.SpeedWord;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ButtonManager {
    private static ButtonManager buttonManager;
    private Map<String, Button> buttonMap;
    private Map<String, Boolean> isPressButton;

    public ButtonManager() {
        buttonMap = new HashMap<>();
        isPressButton = new HashMap<>();
        createButton();
    }

    public void createButton() {
        // Play button
        Button buttonPlay = new Button();
        buttonPlay.setGraphic(TextureManager.getTextureManager().GetImageView("play_n"));
        buttonPlay.getStyleClass().add("transparent-button");
        buttonPlay.setOnMouseEntered(event -> buttonPlay.setGraphic(TextureManager.getTextureManager().GetImageView("play_h")));
        buttonPlay.setOnMouseExited(event -> buttonPlay.setGraphic(TextureManager.getTextureManager().GetImageView("play_n")));
        buttonPlay.setOnMouseClicked(event -> {
            buttonPlay.setGraphic(TextureManager.getTextureManager().GetImageView("play_p"));
            SceneManager.getSceneManager().ChangeScene(PlayScene.getPlayScene());
            SceneManager.getSceneManager().Load();
        });
        buttonMap.put("play", buttonPlay);

        // Score button
        Button buttonScore = new Button();
        buttonScore.setGraphic(TextureManager.getTextureManager().GetImageView("score_n"));
        buttonScore.getStyleClass().add("transparent-button");
        buttonScore.setOnMouseEntered(event -> buttonScore.setGraphic(TextureManager.getTextureManager().GetImageView("score_h")));
        buttonScore.setOnMouseExited(event -> buttonScore.setGraphic(TextureManager.getTextureManager().GetImageView("score_n")));
        buttonScore.setOnMouseClicked(event -> buttonScore.setGraphic(TextureManager.getTextureManager().GetImageView("score_p")));
        buttonMap.put("score", buttonScore);

        // Option button
        Button buttonOption = new Button();
        buttonOption.setGraphic(TextureManager.getTextureManager().GetImageView("option_n"));
        buttonOption.getStyleClass().add("transparent-button");
        buttonOption.setOnMouseEntered(event -> buttonOption.setGraphic(TextureManager.getTextureManager().GetImageView("option_h")));
        buttonOption.setOnMouseExited(event -> buttonOption.setGraphic(TextureManager.getTextureManager().GetImageView("option_n")));
        buttonOption.setOnMouseClicked(event -> buttonOption.setGraphic(TextureManager.getTextureManager().GetImageView("option_p")));
        buttonMap.put("option", buttonOption);

        // Exit button
        Button buttonExit = new Button();
        buttonExit.setGraphic(TextureManager.getTextureManager().GetImageView("exit_n"));
        buttonExit.getStyleClass().add("transparent-button");
        buttonExit.setOnMouseEntered(event -> buttonExit.setGraphic(TextureManager.getTextureManager().GetImageView("exit_h")));
        buttonExit.setOnMouseExited(event -> buttonExit.setGraphic(TextureManager.getTextureManager().GetImageView("exit_n")));
        buttonExit.setOnMouseClicked(event -> buttonExit.setGraphic(TextureManager.getTextureManager().GetImageView("exit_p")));
        buttonMap.put("exit", buttonExit);

        // Home button
        Button buttonHome = new Button();
        buttonHome.setGraphic(TextureManager.getTextureManager().GetImageView("home_n"));
        buttonHome.getStyleClass().add("transparent-button");
        buttonHome.setOnMouseEntered(event -> buttonHome.setGraphic(TextureManager.getTextureManager().GetImageView("home_h")));
        buttonHome.setOnMouseExited(event -> buttonHome.setGraphic(TextureManager.getTextureManager().GetImageView("home_n")));
        buttonHome.setOnMouseClicked(event -> {
            buttonHome.setGraphic(TextureManager.getTextureManager().GetImageView("home_p"));
            SceneManager.getSceneManager().ChangeScene(MenuScene.getMenuScene());
            SceneManager.getSceneManager().Load();
        });
        buttonMap.put("home", buttonHome);

        // Restart button
        Button buttonRestart = new Button();
        buttonRestart.setGraphic(TextureManager.getTextureManager().GetImageView("restart_n"));
        buttonRestart.getStyleClass().add("transparent-button");
        buttonRestart.setOnMouseEntered(event -> buttonRestart.setGraphic(TextureManager.getTextureManager().GetImageView("restart_h")));
        buttonRestart.setOnMouseExited(event -> buttonRestart.setGraphic(TextureManager.getTextureManager().GetImageView("restart_n")));
        buttonRestart.setOnMouseClicked(event -> {
            buttonRestart.setGraphic(TextureManager.getTextureManager().GetImageView("restart_p"));
            SceneManager.getSceneManager().ChangeScene(PlayScene.getPlayScene());
            SceneManager.getSceneManager().Load();
        });
        buttonMap.put("restart", buttonRestart);

        // 10 button
        for (int i = 1; i <= 10; i++) {
            Integer id = i;
            isPressButton.put(id.toString(), false);
            Button tmp = new Button();
            tmp.getStyleClass().add("button");
            tmp.setOnMouseClicked(event -> {
                tmp.getStyleClass().add("button-click");
                TurnPlay.getTurnPlay().buttonPressed(tmp);
            });
            buttonMap.put(id.toString(), tmp);
        }

    }

    public Button getButton(String id) {
        return buttonMap.get(id);
    }
    public Button getButton(String id, int x, int y) {
        Button button = buttonMap.get(id);
        button.setTranslateX(x);
        button.setTranslateY(y);
        return button;
    }

    public Button getButton(String id, int x, int y, int width, int height) {
        Button button = buttonMap.get(id);
        button.setTranslateX(x);
        button.setTranslateY(y);
        button.setPrefSize(width, height);
        return button;
    }

    public void setButtonText(String id, String text) {
        buttonMap.get(id).setText(text);
    }

    public static ButtonManager getButtonManager() {
        if (buttonManager == null) {
            buttonManager = new ButtonManager();
        }
        return buttonManager;
    }
}