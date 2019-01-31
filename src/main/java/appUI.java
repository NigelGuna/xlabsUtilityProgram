import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;

import javax.swing.*;
import java.io.*;

public class appUI extends Application {

    public static MenuBar functionBar = new MenuBar();
    public static GridPane pane = new GridPane();
    Scene scene = new Scene(pane, Main.width, Main.height);

    @Override
    public void start(Stage primaryStage) throws Exception {

        //GRID PANE CREATTION AND CUSTOMIZATION

        pane.setHgap(10);
        pane.setVgap(10);
//        pane.setBackground(new Background(new BackgroundFill(Color.rgb(0,0,0), CornerRadii.EMPTY, Insets.EMPTY)));

        //MISC DECLERATIONS
        SeparatorMenuItem separator = new SeparatorMenuItem();
        Slider slider = new Slider(0, 100, 50);
        ColorTagIdentifier colorTagIdentifier = new ColorTagIdentifier();

        //MENU BAR

        //CSS CHECKER
        Menu function1 = new Menu("Menu");
        functionBar.getMenus().add(function1);
        ImageView cssImage = (new ImageView("file:C:\\PROJECTS LOL\\cssScriptInterface\\src\\resources\\css.png"));
        cssImage.setFitHeight(20);
        cssImage.setFitWidth(20);
        function1.setGraphic(cssImage);
        //MENU FUNCTIONS
        MenuItem function1Item1 = new MenuItem("THE CHECK!");
        function1.getItems().add(function1Item1);
        function1.getItems().add(separator);
        //VELOCITY SLIDER - its pointless but super cool
        CustomMenuItem function1Item2 = new CustomMenuItem();
        function1Item2.setContent(slider);
        function1Item2.setHideOnClick(false);
        function1.getItems().add(function1Item2);
        //VELOCITY BUTTON
        Button velocityButton = new Button("Set Velocity");
        CustomMenuItem function1Item2Button = new CustomMenuItem();
        function1Item2Button.setContent(velocityButton);
        function1Item2Button.setOnAction(action ->{
            System.out.println("Velocityyyyyyyyyyyyyyyyyyyy");
        });
        function1.getItems().add(function1Item2Button);

        //MOON LANDER MENU OPTION
        MenuItem function1Item3 = new MenuItem("Moon Lander");
        function1Item3.setOnAction(Action ->{
            try {
                moonLanderScene.beginMoonLander(primaryStage);
            }
            catch (Exception e){
                System.out.println(e);
            }
            });
        function1.getItems().add(function1Item3);

        //TYPE RACER MENU OPTION
        MenuItem function1Item4 = new MenuItem("Type Racer");
        function1Item4.setOnAction(Action->{
            try{
                TypeRacerScene.beginTypeRacer(primaryStage);
            }
            catch(Exception e){
                System.out.println(e);
            }
        });
        function1.getItems().add(function1Item4);

        //EXIT BUTTON
        MenuItem function1Exit = new MenuItem("Exit");
        function1Exit.setOnAction(Action ->{
            System.exit(0);
        });
        function1.getItems().add(function1Exit);

        //ADD MENU BAR TO PANE
        pane.add(functionBar, 0,0, 1,1);

        //FILE PATH LABEL
        Label instruction = new Label("Enter file path to execute CSS check:");
//        instruction.setTextFill(Color.web("#ffffff"));
        pane.add(instruction, 1, 1, 1, 2);

        //FILE PATH TEXT FIELD
        TextField filePath = new TextField();
        filePath.setText("Enter file path here");
        pane.add(filePath, 1, 3, 1, 2);

        //OUTPUT SECTION
        TextArea output = new TextArea();
        output.setEditable(false);
        pane.add(output, 1, 7, 5, 9);

        //CSS FILE EXISTENCE CHECKBOX
        CheckBox cssFileExistenceCheck = new CheckBox("Check for incorrect CSS file extension");
        cssFileExistenceCheck.setSelected(false);
        pane.add(cssFileExistenceCheck, 2, 5, 1, 2);

        //RUN BUTTON
        Button run = new Button("Run");
        run.setOnAction(action -> {
            Main.filePathString = filePath.getText();
            colorTagIdentifier.searchFile(new File(Main.filePathString));
            String finalOutput = colorTagIdentifier.consoleOutput;

            if(cssFileExistenceCheck.isSelected()){
                colorTagIdentifier.checkCssFiles(new File(Main.filePathString));
                finalOutput = finalOutput.concat(colorTagIdentifier.consoleOutput);
            }

            finalOutput = finalOutput.replaceAll("(\r?\n){2,}", "$1");
            output.setText(finalOutput);
        });
        pane.add(run, 1,5, 1, 2);

        //EXIT BUTTON
        Button exit = new Button("Close");
        exit.setOnAction(action ->{
            System.exit(0);
        });
        pane.add(exit, 1, 16, 1, 2);


        //DECLARE MENU CHECK FUNCTIONALITY
        function1Item1.setOnAction(Action->{
            primaryStage.setScene(scene);
            recalibrateMainStage(primaryStage);
            primaryStage.show();
        });

        //FINAL STEPS
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void recalibrateMainStage(Stage primaryStage){
        pane.add(functionBar, 0,0, 1,1);
        primaryStage.setResizable(false);
        primaryStage.setTitle("CSS Check");
    }

    public static void main(String args[]){
        launch(args);
    }
}

