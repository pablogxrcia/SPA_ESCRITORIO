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
import okhttp3.Cookie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controlador implements Initializable {
    RepositoryUsers repository;
    private String authToken;

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

    private void abrirVenanaAdministrador() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("vista/adminCarreras.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la ventana de administración
            ControladorCarrera controladorCarrera = loader.getController();
            controladorCarrera.setAuthToken(authToken); // Pasar el token al controlador

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Administración de Carreras");
            stage.show();

            // Cerrar la ventana de login
            Stage s = (Stage) this.txtLogin.getScene().getWindow();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void encolaLogin() {
        repository.callLogin.enqueue(new Callback<UserLogin>() {
            @Override
            public void onFailure(Call<UserLogin> call, Throwable t) {
                System.out.println("Network Error :: " + t.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful()) {
                        if (response.body().getUser().getRole().equals("admin")) {
                            // Obtener cookies
                            List<Cookie> cookies = repository.getCookies();
                            for (Cookie cookie : cookies) {
                                if (cookie.name().equals("token")) { // Suponiendo que el token se llama "token"
                                    authToken = cookie.value(); // Guardar el token
                                    System.out.println("Token obtenido: " + authToken);
                                }
                            }
                            abrirVenanaAdministrador();
                        } else {
                            mostrarAlerta("Error", "El usuario no tiene permisos de administrador");
                        }
                    } else {
                        mostrarAlerta("Error", "Usuario o contraseña incorrectos");
                    }
                });
            }
        });
    }
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
