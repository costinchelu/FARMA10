package farma;

import farma.util.LogUser;
import farma.util.UserLogged;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class Main extends Application {

    Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.setOnCloseRequest(e -> {
            e.consume();
            try {
                closeProgram();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

//        Parent root = FXMLLoader.load(getClass().getResource("view/MainScene.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("view/Login.fxml"));

        Scene scene = new Scene(root);

        Image icon = new Image(String.valueOf(getClass().getResource("pill_bottle_480px.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Autentificare FARMA 10");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void closeProgram() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmare iesire");
        alert.setHeaderText("Confirmare pentru iesirea din program");
        alert.setContentText("Sunteti sigur ca doriti sa iesiti?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // Log out user
            LogUser.logUser(UserLogged.readUser(), "-", "OUT");

            // inchidere program
            window.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
