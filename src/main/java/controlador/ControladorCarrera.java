package controlador;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControladorCarrera implements Initializable {
    @FXML
    private ImageView imgAddCarrera, imgCerrar, imgLogo, imgUsers;

    @FXML
    private ListView<?> lstCarreras;

    @FXML
    private RadioButton optCiclismo, optRunning, optTodas, optTrailRunning;

    @FXML
    private HBox hboxAddCarrera, hboxCerrar, hboxUsers;

    private ToggleGroup toggleGroup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        establecerIconos();
        configurarToggleGroup();
        configurarEventosMouse();
    }

    private void establecerIconos() {
        imgLogo.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/logoApp.png")));
        imgAddCarrera.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/anadir.png")));
        imgCerrar.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/cerrar-sesion.png")));
        imgUsers.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/login.png")));
    }

    private void configurarToggleGroup() {
        toggleGroup = new ToggleGroup();
        optTodas.setToggleGroup(toggleGroup);
        optRunning.setToggleGroup(toggleGroup);
        optTrailRunning.setToggleGroup(toggleGroup);
        optCiclismo.setToggleGroup(toggleGroup);
        optTodas.setSelected(true); // Selecciona una opción por defecto
    }

    private void configurarEventosMouse() {
        configurarEfectoHover(hboxUsers);
        configurarEfectoHover(hboxAddCarrera);
        configurarEfectoHover(hboxCerrar);
    }
    private void configurarEventos() {
        // Evento para hboxUsers (Abrir AdminUsers.fxml)
        //hboxUsers.setOnMouseClicked(event -> cambiarVentana("AdminUsers.fxml",event));
        hboxAddCarrera.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("¿Estás seguro que quieres salir?");
            alert.setContentText("Serás redirigido a la pantalla de inicio de sesión.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                cambiarVentana("login.fxml",event);
            }
        });

        // Evento para hboxAddCarrera (Abrir VentanaAddCarrera.fxml)
        //hboxAddCarrera.setOnMouseClicked(event -> cambiarVentana("VentanaAddCarrera.fxml",event));
        hboxUsers.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("¿Estás seguro que quieres salir?");
            alert.setContentText("Serás redirigido a la pantalla de inicio de sesión.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                cambiarVentana("login.fxml",event);
            }
        });

        // Evento para hboxCerrar (Confirmar salida y volver a login)
        hboxCerrar.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("¿Estás seguro que quieres salir?");
            alert.setContentText("Serás redirigido a la pantalla de inicio de sesión.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                cambiarVentana("login.fxml",event);
            }
        });
    }
    private void configurarEfectoHover(HBox hbox) {
        hbox.setOnMouseEntered(event -> hbox.setStyle("-fx-background-color: #D3D3D3; -fx-cursor: hand;"));
        hbox.setOnMouseExited(event -> hbox.setStyle("-fx-background-color: transparent;"));
    }

    private void cambiarVentana(String fxml, MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/" + fxml));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
