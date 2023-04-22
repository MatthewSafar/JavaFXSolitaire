package com.mycompany.mysolitaire;

import java.io.File;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * JavaFX App
 */
public class App extends Application {
    public static Scene mainMenu, settingsMenu, cardsScene;
    public static SettingsObj settings;
    public static MediaPlayer musicPlayer;

    @Override
    public void start(Stage primaryStage) {
        settings = new SettingsObj();
        musicPlayer = settings.getNextMusicTrack();
        if (musicPlayer != null) {
            musicPlayer.setMute(settings.isMute());
            musicPlayer.play();
            musicPlayer.setOnEndOfMedia(() -> {
                musicPlayer = settings.getNextMusicTrack();
                musicPlayer.setMute(settings.isMute());
                musicPlayer.play();
            });
        }
        
        initMainMenu(primaryStage);
        initSettingsMenu(primaryStage);
        initCardsScene(primaryStage);
        
        primaryStage.setTitle("mySolitaire");
        primaryStage.setScene(mainMenu);
        primaryStage.setResizable(false);
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
        var root = new Pane();
        root.setId("root");
        
        var sceneTitleGroup = new Pane();
        var cardDecorationGroup = new Group();
        CardView cards[] = new CardView[4];
        double scale = 0.8;
        double angleH = 20.0;
        double angleB = -80.0;
        for (int iter = 0; iter < 4; iter++) {
            cards[iter] = new CardView(new Card(DeckInfo.Ranks.A, DeckInfo.Suits.values()[iter]));
            cards[iter].setScale(scale);
            Rotate rotate = new Rotate();
            rotate.setPivotX(CardView.DEF_WIDTH * scale / 2);
            rotate.setPivotY(CardView.DEF_WIDTH * CardView.H_W_RATIO * scale);
            rotate.setAngle(angleH * iter + angleB);
            cards[iter].getTransforms().add(rotate);
            cardDecorationGroup.getChildren().add(cards[iter]);
        }
        sceneTitleGroup.getChildren().add(cardDecorationGroup);
        cardDecorationGroup.relocate(-80,-60);
        
        var sceneTitle = new Label("Solitaire");
        sceneTitle.setId("title");
        sceneTitleGroup.getChildren().add(sceneTitle);
        sceneTitle.relocate(0,0);
        root.getChildren().add(sceneTitleGroup);
        
        var javaEdition = new Label("JavaFX Edition!");
        javaEdition.setId("javaEdition");
        sceneTitleGroup.getChildren().add(javaEdition);
        
        var playButton = new Button("    Play    ");
        playButton.setOnAction((ActionEvent e) -> {
            primaryStage.setScene(cardsScene);
        });
        root.getChildren().add(playButton);

        var settingsButton = new Button("Settings");
        settingsButton.setOnAction((ActionEvent e) -> {
            primaryStage.setScene(settingsMenu);
        });
        root.getChildren().add(settingsButton);
        
        mainMenu = new Scene(root, settings.getWidth(), settings.getHeight());
        mainMenu.getStylesheets().add(getURI(SettingsObj.STATIC_PATH + "mainMenuStyle.css"));
        
        // layout stuff
        sceneTitleGroup.applyCss();
        javaEdition.relocate(sceneTitle.prefWidth(-1)-javaEdition.prefWidth(-1)/2 - 10,
                sceneTitle.prefHeight(-1) - 30);
        Rotate rotate = new Rotate();
        rotate.setPivotX(javaEdition.prefWidth(-1) / 2);
        rotate.setPivotY(javaEdition.prefHeight(-1) / 2);
        rotate.setAngle(-20);
        javaEdition.getTransforms().add(rotate);
        ScaleTransition scaleAnim = new ScaleTransition(Duration.millis(1300),javaEdition);
        scaleAnim.setByX(1.05);
        scaleAnim.setByY(1.05);
        scaleAnim.setAutoReverse(true);
        scaleAnim.setCycleCount(ScaleTransition.INDEFINITE);
        scaleAnim.play();

        
        double widthOffset = sceneTitle.prefWidth(-1)  / 2;
        double xpos = settings.getWidth() / 2 - widthOffset;
        sceneTitleGroup.relocate(xpos,settings.getHeight() * 0.3);
        playButton.relocate(xpos,settings.getHeight()*0.7);
        settingsButton.applyCss();
        System.out.println(settingsButton.prefWidth(-1));
        xpos = settings.getWidth() / 2 + widthOffset - settingsButton.prefWidth(-1);
        settingsButton.relocate(xpos,settings.getHeight()*0.7);
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
        leftMenu.setFillWidth(true);
        leftMenu.setSpacing(60.0);
        leftMenu.setId("leftMenu");
        root.getChildren().add(leftMenu);
        
        var backButton = new Button();
        var icon = new ImageView(getURI(SettingsObj.STATIC_PATH + "mainMenu-icon.png"));
        icon.setPreserveRatio(true);
        icon.setFitWidth(40);
        backButton.setGraphic(icon);
        backButton.setPrefSize(60,60);
        backButton.setOnAction((ActionEvent e) -> {
            primaryStage.setScene(mainMenu);
        });
        leftMenu.getChildren().add(backButton);
        
        var resetButton = new Button();
        icon = new ImageView(getURI(SettingsObj.STATIC_PATH + "reset-icon.png"));
        icon.setPreserveRatio(true);
        icon.setSmooth(true);
        icon.setFitWidth(40);
        resetButton.setGraphic(icon);
        resetButton.setPrefSize(60,60);
        resetButton.setOnAction((ActionEvent e) -> {
            var solitaireGame = new SolitairePane();
            HBox.setHgrow(solitaireGame, Priority.ALWAYS);
            root.getChildren().remove(root.getChildren().size() - 1);
            root.getChildren().add(solitaireGame);
        });
        leftMenu.getChildren().add(resetButton);
        
        var soundButton = new Button();
        if (settings.isMute()) {
            icon = new ImageView(getURI(SettingsObj.STATIC_PATH + "volume-mute-icon.png"));
        } else {
            icon = new ImageView(getURI(SettingsObj.STATIC_PATH + "volume-on-icon.png"));
        }
        icon.setPreserveRatio(true);
        icon.setFitWidth(40);
        soundButton.setGraphic(icon);
        soundButton.setPrefSize(60,60);
        soundButton.setOnAction((ActionEvent e) -> {
            settings.setMute(!settings.isMute());
            if (musicPlayer !=null) {musicPlayer.setMute(settings.isMute());}
            ImageView newIcon;
            if (settings.isMute()) {
                newIcon = new ImageView(getURI(SettingsObj.STATIC_PATH + "volume-mute-icon.png"));
            } else {
                newIcon = new ImageView(getURI(SettingsObj.STATIC_PATH + "volume-on-icon.png"));
            }
            newIcon.setPreserveRatio(true);
            newIcon.setFitWidth(40);
            soundButton.setGraphic(newIcon);
        });
        leftMenu.getChildren().add(soundButton);
        
        var solitaireGame= new SolitairePane();
        HBox.setHgrow(solitaireGame, Priority.ALWAYS);
        root.getChildren().add(solitaireGame);
        
        cardsScene = new Scene(root, settings.getWidth(), settings.getHeight());
        cardsScene.getStylesheets().add(getURI(SettingsObj.STATIC_PATH + "cardSceneStyle.css"));
    }
    
    public static String getURI(String resourcePath) {
        return new File(resourcePath).toURI().toString();
    }
}
