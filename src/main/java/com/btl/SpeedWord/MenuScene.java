/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.btl.SpeedWord;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class MenuScene extends CustomScene {
    private static MenuScene menuScene;
    private boolean pause;

    public void Init() {
        pause = false;
        StackPane root = new StackPane();
        root.getChildren().add(TextureManager.getTextureManager().GetImageView("menu", 0, 0));
        root.getChildren().add(ButtonManager.getButtonManager().getButton("play", 0, 0));
        root.getChildren().add(ButtonManager.getButtonManager().getButton("score", 0, 80));
        root.getChildren().add(ButtonManager.getButtonManager().getButton("option", 0, 160));
        root.getChildren().add(ButtonManager.getButtonManager().getButton("exit", 0, 240));

        scene = new Scene(root, Engine.SCREEN_WIDTH, Engine.SCREEN_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("transparentButton.css").toExternalForm());
    }

    public static MenuScene getMenuScene() {
        if (menuScene == null) {
            menuScene = new MenuScene();
        }
        return menuScene;
    }
}