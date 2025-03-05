package controlador;

import api.RepositoryUsers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.LoginRequest;
import modelo.UserLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controlador implements Initializable {
    RepositoryUsers repository;

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
        repository.callLogin = repository.serviceLogin.getUser(new LoginRequest(txtLogin.getText(), txtPassword.getText()));
        encolaLogin();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        establecerIconos();
        repository = new RepositoryUsers();
    }
    private void establecerIconos(){
        imgAdmin.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/login.png")));
        imgPassword.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/password.png")));
        imgAdminPrincipal.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/administradorPrincipal.png")));
    }

    public void abrirVenanaAdministrador(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("vista/adminCarreras.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Administracion de Carreras");
            stage.show();

            //Cierra la ventana login
            Stage s = (Stage) this.txtLogin.getScene().getWindow();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void encolaLogin(){
        repository.callLogin.enqueue(new Callback<UserLogin>() {
            /**
             * Para errores del tipo: Network Error :: timeout
             */
            @Override
            public void onFailure(Call<UserLogin> call, Throwable t) {
                System.out.println("Network Error :: " + t.getLocalizedMessage());
            }

            /**
             * La respuesta del servidor
             */
            @Override
            public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                Platform.runLater(() -> {
                    System.out.println("Respuesta LECTURA " + response);
                    System.out.println("Respuesta LECTURA (Estado HTTP): " + response.message());
                    System.out.println("Respuesta LECTURA: " + response.body());
                    if (response.isSuccessful()) {
                        if (response.body().getUser().getRole().equals("admin")) {
                            abrirVenanaAdministrador();
                        }else {
                            Alert alertaLeer = new Alert(Alert.AlertType.WARNING);
                            alertaLeer.setTitle("Error");
                            alertaLeer.setHeaderText("El usuario no tiene permisos de administrador");
                            alertaLeer.setContentText("Código: " + response.code());
                            alertaLeer.showAndWait();
                        }
                    } else {
                        Alert alertaLeer = new Alert(Alert.AlertType.ERROR);
                        alertaLeer.setTitle("Datos incorrectos");
                        alertaLeer.setHeaderText("Usuario o contraseña incorrectos");
                        alertaLeer.setContentText("Código error: " + response.code());
                        alertaLeer.showAndWait();
                    }
                });
            }
        });
    }
}
