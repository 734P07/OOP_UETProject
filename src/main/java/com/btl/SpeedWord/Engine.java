/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.btl.SpeedWord;

import javafx.stage.Stage;

public class Engine {
    public static final int SCREEN_WIDTH = 1018;
    public static final int SCREEN_HEIGHT = 688;
    public static final String Title = "SpeedWord";
    private static Stage stage;
    private static Engine engine;

    public void start() {
        stage = new Stage();
        TextureManager.getTextureManager().ParseTexture("Texture.xml");

        SceneManager.getSceneManager().ChangeScene(MenuScene.getMenuScene());

        stage.setScene(SceneManager.getSceneManager().getGameScene().peek().scene);
        stage.setTitle(Title);
        stage.setResizable(false);
        stage.show();
    }

    public static Stage getStage() {
        return stage;
    }
    
    public static Engine getEngine() {
        if(engine == null) {
            engine = new Engine();
        }
        return engine;
    }
}
