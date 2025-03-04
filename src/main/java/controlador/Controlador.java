package controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class Controlador implements Initializable {
    @FXML
    private ImageView imgAdmin;

    @FXML
    private ImageView imgAdminPrincipal;

    @FXML
    private ImageView imgPassword;

    @FXML
    private TextField txtLogin;

    @FXML
    private TextField txtPassword;

    @FXML
    void abridPanelAdministracion(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        establecerIconos();
    }
    private void establecerIconos(){
        imgAdmin.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/login.png")));
        imgPassword.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/password.png")));
        imgAdminPrincipal.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/administradorPrincipal.png")));
    }
}
