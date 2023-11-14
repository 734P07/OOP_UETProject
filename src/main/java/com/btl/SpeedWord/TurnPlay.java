/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.btl.SpeedWord;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.*;

public class TurnPlay {
    private static TurnPlay turnPlay;
    private Map<String, String> wordMap;
    private Button firstButton;
    private Button secondButton;
    private int score;
    private boolean flag;

    public void start() {
        score = 0;
        flag = false;

        wordMap = new HashMap<>();
        wordMap.put("hello", "xin chào");
        wordMap.put("english", "tiếng anh");
        wordMap.put("sword", "thanh kiếm");
        wordMap.put("bird", "chim");
        wordMap.put("mouse", "chuột");

        List<String> id = new ArrayList<>();
        for (Integer i = 6; i <= 10; i++) {
            id.add(i.toString());
        }
        Collections.shuffle(id);
        Integer tmp = 1;
        for(Map.Entry<String, String> entry : wordMap.entrySet()) {
            ButtonManager.getButtonManager().setButtonText(tmp.toString(), entry.getKey());
            ButtonManager.getButtonManager().setButtonText(id.get(tmp - 1), entry.getValue());
            tmp += 1;
        }

        // Reset 10 button
        for (Integer i = 1; i <= 10; i++) {
            Button button = ButtonManager.getButtonManager().getButton(i.toString());
            button.getStyleClass().remove("button-disabled");
            button.getStyleClass().remove("button-true");
            button.getStyleClass().remove("button-click");
            button.setDisable(false);
        }
    }

    public void scoreText() {
        if(checkWord()) {
            score++;
            PlayScene.getPlayScene().setScore(PlayScene.getPlayScene().getScore() + 1);
            Text text = PlayScene.getPlayScene().getScoreText();
            text.setText(PlayScene.getPlayScene().getScore().toString());
        }
        if (score == 5) {
            start();
        }
    }

    public boolean checkWord() {
        String word1 = firstButton.getText();
        String word2 = secondButton.getText();
        Button button1 = firstButton;
        Button button2 = secondButton;
        if ((wordMap.containsKey(word1) && wordMap.get(word1) != null && wordMap.get(word1).equals(word2))
                || (wordMap.containsKey(word2) && wordMap.get(word2) != null && wordMap.get(word2).equals(word1))) {
            button1.getStyleClass().add("button-true");
            button2.getStyleClass().add("button-true");
            button1.setDisable(true);
            button2.setDisable(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
                button1.getStyleClass().add("button-disabled");
                button2.getStyleClass().add("button-disabled");
            }));
            timeline.setCycleCount(1); // Chỉ thực hiện một lần
            timeline.play();
            flag = false;
            return true;
        } else {
            button1.getStyleClass().add("button-false");
            button2.getStyleClass().add("button-false");
            button1.setDisable(true);
            button2.setDisable(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
                button1.getStyleClass().remove("button-false");
                button2.getStyleClass().remove("button-false");
                button1.getStyleClass().remove("button-click");
                button2.getStyleClass().remove("button-click");
                button1.setDisable(false);
                button2.setDisable(false);
            }));
            timeline.setCycleCount(1); // Chỉ thực hiện một lần
            timeline.play();
        }
        flag = false;
        return false;
    }

    public void buttonPressed(Button button) {
        if (!flag) {
            firstButton = button;
            flag = true;
        } else {
            secondButton = button;
            scoreText();
        }
    }

    public static TurnPlay getTurnPlay() {
        if (turnPlay == null) {
            turnPlay = new TurnPlay();
        }
        return turnPlay;
    }
}
