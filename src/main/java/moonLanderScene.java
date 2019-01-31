import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.*;

import javax.swing.*;
import java.io.*;


/**
 * Created by ngunawardena on 1/29/2019.
 */
public class moonLanderScene{

    public static void beginMoonLander(Stage primaryStage) throws Exception {
        GridPane moonLanderPane = new GridPane();
        moonLanderPane.add(appUI.functionBar, 0,0, 5,1);


        WebView moonLanderWebView = new WebView();
//        VBox moonLanderPane = new VBox (chessWebView);
        moonLanderWebView.getEngine().load("http://moonlander.seb.ly/");
        moonLanderPane.add(moonLanderWebView, 0, 2, 10, 10);

        Scene moonLanderScene = new Scene(moonLanderPane, Main.width, Main.height);
        primaryStage.setTitle("MoonLander");
        primaryStage.setResizable(true);
        primaryStage.setScene(moonLanderScene);

    }

}
