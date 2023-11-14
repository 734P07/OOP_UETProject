/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.btl.SpeedWord;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class PlayScene extends CustomScene {
    private static PlayScene playScene;
    private Timeline timeline;
    private Text scoreText;
    private Integer score;

    public void Init() {
        // Load font
        Font customFont = Font.loadFont(getClass().getResourceAsStream("FontPoint.ttf"), 90);

        StackPane root = new StackPane();
        root.getChildren().add(TextureManager.getTextureManager().GetImageView("play"));
        root.getChildren().add(ButtonManager.getButtonManager().getButton("1", -200, -130, 250, 80));
        root.getChildren().add(ButtonManager.getButtonManager().getButton("2", -200, -30, 250, 80));
        root.getChildren().add(ButtonManager.getButtonManager().getButton("3", -200, 70, 250, 80));
        root.getChildren().add(ButtonManager.getButtonManager().getButton("4", -200, 170, 250, 80));
        root.getChildren().add(ButtonManager.getButtonManager().getButton("5", -200, 270, 250, 80));
        root.getChildren().add(ButtonManager.getButtonManager().getButton("6", 200, -130, 250, 80));
        root.getChildren().add(ButtonManager.getButtonManager().getButton("7", 200, -30, 250, 80));
        root.getChildren().add(ButtonManager.getButtonManager().getButton("8", 200, 70, 250, 80));
        root.getChildren().add(ButtonManager.getButtonManager().getButton("9", 200, 170, 250, 80));
        root.getChildren().add(ButtonManager.getButtonManager().getButton("10", 200, 270, 250, 80));

        // Score init
        {
            score = 0;
            scoreText = new Text(score.toString());
            scoreText.setFont(customFont);
            scoreText.setTranslateX(0);
            scoreText.setTranslateY(-250);
            root.getChildren().add(scoreText);
        }

        // Timer Bar
        {
            ProgressBar progressBar = new ProgressBar(1); // Giá trị ban đầu là 1.0 (toàn bộ thanh)
            progressBar.getStyleClass().add("progress-bar"); // Thêm lớp CSS vào ProgressBar
            progressBar.setMinWidth(Engine.SCREEN_WIDTH);
            progressBar.setMinHeight(10);
            progressBar.setTranslateX(0);
            progressBar.setTranslateY(-Engine.SCREEN_HEIGHT / 2 + 5);

            timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, e -> {
                        double progress = progressBar.getProgress();
                        if (progress > 0) {
                            progressBar.setProgress(Math.max(0, progress - 0.00016)); // Giảm giá trị progress mỗi 0.01 giây
                        } else {
                            StackPane stk = (StackPane) scene.getRoot();
                            stk.getChildren().add(gameOver());
                            timeline.stop();
                        }
                    }),
                    new KeyFrame(Duration.seconds(0.01)) // Cập nhật mỗi 0.01 giây
            );

            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
            root.getChildren().add(progressBar);
        }

        // Logic
        TurnPlay.getTurnPlay().start();

        scene = new Scene(root, Engine.SCREEN_WIDTH, Engine.SCREEN_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("Button.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("TimerBar.css").toExternalForm());
    }

    public StackPane gameOver() {
        for (Integer i = 1; i <= 10; i++) {
            ButtonManager.getButtonManager().getButton(i.toString()).setDisable(true);
        }

        StackPane root = new StackPane();
        root.getChildren().add(TextureManager.getTextureManager().GetImageView("over"));
        root.getChildren().add(ButtonManager.getButtonManager().getButton("home", -100, 50));
        root.getChildren().add(ButtonManager.getButtonManager().getButton("restart", 100, 50));
        return root;
    }

    public Text getScoreText() {
        return scoreText;
    }

    public void setScoreText(Text scoreText) {
        this.scoreText = scoreText;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public static PlayScene getPlayScene() {
        if (playScene == null) {
            playScene = new PlayScene();
        }
        return playScene;
    }
}
