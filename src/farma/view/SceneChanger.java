package farma.view;


import farma.model.Stoc;
import farma.model.Intrari;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class SceneChanger {
    /**
     * Primeste o noua scena prin titlu si numele fxml-ului
     */
    public void changeScenes(ActionEvent event, String viewName, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        // primeste scena din event
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Primeste o noua scena prin titlu si numele fxml-ului
     * primeste un obiect pentru a fi transferat catre alta scena
     */
    public void changeScenes(ActionEvent event,
                             String viewName,
                             String title,
                             Object transferObj,
                             ControllerClass controllerClass) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        // controller class va implementa o metoda cu ajutorul careia va prelua obiectul transferat intre scene
        // accesam controllerul ci preincarcam obiectul
        controllerClass = loader.getController();
        controllerClass.preloadObject(transferObj);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Primeste o noua scena prin titlu si numele fxml-ului
     * primeste un obiect pentru a fi transferat catre alta scena (caz editare)
     * primeste un ObservableList pentru pastrare (caz editare)
     */
    public void changeScenesIntrari(ActionEvent event,
                             String viewName,
                             String title,
                             Object transferObj,
                             ObservableList<Intrari> transferList,
                             ControllerClass controllerClass,
                             ControllerListTransfer controllerListTransfer) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        controllerListTransfer = loader.getController();
        controllerListTransfer.preloadList(transferList);

        controllerClass = loader.getController();
        controllerClass.preloadObject(transferObj);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Primeste o noua scena prin titlu si numele fxml-ului
     * primeste un ObservableList pentru pastrare (caz de adaugare)
     */
    public void changeScenesIntrari(ActionEvent event,
                             String viewName,
                             String title,
                             ObservableList<Intrari> transferList,
                             ControllerListTransfer controllerListTransfer) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        controllerListTransfer = loader.getController();
        controllerListTransfer.preloadList(transferList);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Primeste o noua scena prin titlu si numele fxml-ului
     * primeste un ObservableList pentru pastrare (caz de adaugare)
     */
    public void changeScenesStoc(ActionEvent event,
                                    String viewName,
                                    String title,
                                    ObservableList<Stoc> stocTransfer,
                                    ControllerStocTransfer controllerStocTransfer) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        controllerStocTransfer = loader.getController();
        controllerStocTransfer.preloadList(stocTransfer);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
