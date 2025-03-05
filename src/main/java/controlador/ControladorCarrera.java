package controlador;

import api.RepositoryUsers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.LoginRequest;
import modelo.UserLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.util.ResourceBundle;

public class ControladorCarrera implements Initializable {
    @FXML
    private ImageView imgAddCarrera;

    @FXML
    private ImageView imgCerrar;

    @FXML
    private ImageView imgLogo;

    @FXML
    private ImageView imgUsers;

    @FXML
    private ListView<?> lstCarreras;

    @FXML
    private RadioButton optCiclismo;

    @FXML
    private RadioButton optRunning;

    @FXML
    private RadioButton optTodas;

    @FXML
    private RadioButton optTrailRunning;

    @FXML
    private HBox hboxAddCarrera;

    @FXML
    private HBox hboxCerrar;

    @FXML
    private HBox hboxUsers;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        establecerIconos();

    }
    private void establecerIconos(){
        imgLogo.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/logoApp.png")));
        imgAddCarrera.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/anadir.png")));
        imgCerrar.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/cerrar-sesion.png")));
        imgUsers.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/login.png")));
    }


}
