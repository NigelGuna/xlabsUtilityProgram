import javafx.concurrent.Worker;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;

/**
 * Created by ngunawardena on 1/29/2019.
 */
public class TypeRacerScene {

    public static String typeRacerUrl = null;
    public static String slackToken = "enter-slack-token-here";
    public static String channel = "enter-slack-group-id-here";

    public static void beginTypeRacer(Stage primaryStage) throws Exception {

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

// Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException e) {
        }
//// Now you can access an https URL without having the certificate in the truststore
//        try {
//            URL url = new URL("https://hostname/index.html");
//        } catch (MalformedURLException e) {
//        }

        GridPane typeRacerPane = new GridPane();
        typeRacerPane.add(AppUI.functionBar, 0,0, 5,1);

        WebView typeRacerWebView = new WebView();
        typeRacerWebView.getEngine().load("http://play.typeracer.com/");
        typeRacerPane.add(typeRacerWebView, 0, 2, 10, 10);
        typeRacerWebView.getEngine().getLoadWorker().stateProperty().addListener(Event->{
            if(typeRacerWebView.getEngine().getLoadWorker().getState()== Worker.State.SUCCEEDED){
                System.out.println(typeRacerWebView.getEngine().getLoadWorker().getState());
                System.out.println("Starting thread");
                KeyPressing keyPressing = new KeyPressing();
                Thread keyPressThread = new Thread(keyPressing);
                keyPressThread.start();
            }
        });

        Scene typeRacerScene = new Scene(typeRacerPane, Main.width, Main.height);
        primaryStage.setTitle("Type Racer");
        primaryStage.setResizable(true);
        primaryStage.setScene(typeRacerScene);
    }
}
