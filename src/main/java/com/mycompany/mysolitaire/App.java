package com.mycompany.mysolitaire;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {
    public static Scene mainMenu, settingsMenu, cardsScene;
    public static SettingsObj settings;

    @Override
    public void start(Stage primaryStage) {
        settings = new SettingsObj();
        
        initMainMenu(primaryStage);
        initSettingsMenu(primaryStage);
        initCardsScene(primaryStage);
        
        primaryStage.setTitle("mySolitaire");
        primaryStage.setScene(mainMenu);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
    
    // initializes the main menu
    // requires primary stage as input to hook up buttons to other scenes
    // TODO: stylize so that it looks nice, with an actual logo instead of
    // just basic ass text. Maybe include play history eventually
    public static void initMainMenu(Stage primaryStage) {
        // main menu
        var root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        
        var sceneTitle = new Text("My JavaFX Solitaire");
        root.add(sceneTitle, 0, 0, 2,1);
        var playButton = new Button("Play");
        playButton.setOnAction((ActionEvent e) -> {
            primaryStage.setScene(cardsScene);
        });
        root.add(playButton, 0, 1);
        var settingsButton = new Button("Settings");
        settingsButton.setOnAction((ActionEvent e) -> {
            primaryStage.setScene(settingsMenu);
        });
        root.add(settingsButton, 1, 1);
        mainMenu = new Scene(root, settings.getWidth(), settings.getHeight());
    }
    
    // Initializes the settings scene, with buttons and stuff
    // requires primary stage as input to hook up buttons to other scenes
    // TODO: Allow changes to card appearance, music selection, music volume
    // changes to table appearance, type of solitaire, etc.
    public static void initSettingsMenu(Stage primaryStage) {
        // main menu
        var root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        
        var sceneTitle = new Text("Settings");
        root.add(sceneTitle, 0, 0);
        var backButton = new Button("Main Menu");
        backButton.setOnAction((ActionEvent e) -> {
            primaryStage.setScene(mainMenu);
        });
        root.add(backButton, 0, 1);
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();
        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        root.add(label, 0, 2);
        settingsMenu = new Scene(root, settings.getWidth(), settings.getHeight());
        
    }
    
    // init cards scene. This one will probably be the hardest, we'll see how it
    // goes
    // requires primary stage as input to hook up buttons to other scenes
    public static void initCardsScene(Stage primaryStage) {
        // main menu
        var root = new HBox();
        
        var leftMenu = new VBox();
        leftMenu.setAlignment(Pos.CENTER);
        root.getChildren().add(leftMenu);
        
        var sceneTitle = new Text("Cards, eventually");
        leftMenu.getChildren().add(sceneTitle);
        var backButton = new Button("Main Menu");
        backButton.setOnAction((ActionEvent e) -> {
            primaryStage.setScene(mainMenu);
        });
        leftMenu.getChildren().add(backButton);
        
        leftMenu.setStyle("-fx-background-color: darkgray; -fx-text-fill: white;");
        
        var solitaireGame= new SolitairePane();
        HBox.setHgrow(solitaireGame, Priority.ALWAYS);
        root.getChildren().add(solitaireGame);
        
        cardsScene = new Scene(root, settings.getWidth(), settings.getHeight());
    }

}
