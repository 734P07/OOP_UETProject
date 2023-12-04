/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.btl.SpeedWord.Scenes;

import com.btl.SpeedWord.Sound.SoundManager;
import com.btl.SpeedWord.Widgets.ButtonManager;
import com.btl.SpeedWord.Core.Engine;
import com.btl.SpeedWord.Graphics.TextureManager;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;

public class MenuScene extends CustomScene {
    private static MenuScene s_Instance;
    private Scene scene;
    private boolean pause;

    public void Init() {
        pause = false;
        StackPane root = new StackPane();
        root.getChildren().add(TextureManager.getTextureManager().GetImageView("menu", 0, 0));
        root.getChildren().add(ButtonManager.getInstance().getButton("play", 0, 0));
        root.getChildren().add(ButtonManager.getInstance().getButton("score", 0, 80));
        root.getChildren().add(ButtonManager.getInstance().getButton("option", 0, 160));
        root.getChildren().add(ButtonManager.getInstance().getButton("exit", 0, 240));

        scene = new Scene(root, Engine.SCREEN_WIDTH, Engine.SCREEN_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("transparentButton.css").toExternalForm());
        Option();

        SoundManager.getInstance().playSound("menu");
    }

    @Override
    public void Exit() {
        SoundManager.getInstance().stopSound("menu");
    }

    @Override
    public void Pause() {
        toggleAllUIObjects(scene.getRoot(), true);
    }

    @Override
    public void Resume() {
        toggleAllUIObjects(scene.getRoot(), false);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    public static MenuScene getInstance() {
        if (s_Instance == null) {
            s_Instance = new MenuScene();
        }
        return s_Instance;
    }

    private void Option() {
        Slider musicSlider = new Slider();
        musicSlider.setMin(0);
        musicSlider.setMax(1);
        musicSlider.setValue(0.5);
        SoundManager.getInstance().getSound("menu").volumeProperty().bind(musicSlider.valueProperty());
        SoundManager.getInstance().getSound("play").volumeProperty().bind(musicSlider.valueProperty());
        musicSlider.setTranslateX(20);
        musicSlider.setTranslateY(-15);
        musicSlider.setMaxSize(300, 50);

        Slider SFXSlider = new Slider();
        SFXSlider.setMin(0);
        SFXSlider.setMax(1);
        SFXSlider.setValue(0.5);
        SoundManager.getInstance().getSound("click").volumeProperty().bind(SFXSlider.valueProperty());
        SFXSlider.setTranslateX(20);
        SFXSlider.setTranslateY(90);
        SFXSlider.setPrefWidth(300);
        SFXSlider.setMaxSize(300, 50);

        StackPane stackPane = new StackPane(TextureManager.getTextureManager().GetImageView("option"), ButtonManager.getInstance().getButton("x", 230, -140), musicSlider, SFXSlider);
        stackPane.setVisible(false);
        stackPane.setId("option");
        ((StackPane) scene.getRoot()).getChildren().add(stackPane);
    }

    public void OpenOption() {
        scene.lookup("#option").setVisible(true);
    }

    public void ExitOption() {
        scene.lookup("#option").setVisible(false);
    }

    public void ExitGame() {
        Engine.getStage().close();
    }
}