package controlador;

import api.RepositoryCarreras;
import api.ServiceBorrarCarrera;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Carrera;
import modelo.Carreras;
import modelo.UserLogin;
import okhttp3.Cookie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControladorCarrera implements Initializable {
    Dotenv dotenv = Dotenv.load();
    String ip = dotenv.get("IP");
    String port = dotenv.get("PORT");

    RepositoryCarreras repository = new RepositoryCarreras();

    @FXML
    private ImageView imgAddCarrera, imgCerrar, imgLogo, imgUsers;

    @FXML
    private ListView<Carrera> lstCarreras;

    @FXML
    public RadioButton optCiclismo, optRunning, optTodas, optTrailRunning;

    @FXML
    private HBox hboxAddCarrera, hboxCerrar, hboxUsers;

    private ToggleGroup toggleGroup;
    private ObservableList<Carrera> carreras = FXCollections.observableArrayList();
    private ObservableList<Carrera> carrerasFiltradas;
    private String authToken; // Campo para almacenar el token
    private ServiceBorrarCarrera serviceBorrarCarrera;

    // Método para establecer el token
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        System.out.println("Token recibido en ControladorAddCarrera: " + authToken); // Verificar que el token se recibe
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        establecerIconos();
        configurarToggleGroup();
        configurarEventosMouse();
        configurarEventos();

        // Crear las carreras
        this.repository.callLeerCarreras = this.repository.serviceLeerCarreras.obtenerCarreras();
        encolaLeerCarreras();

        // Inicializamos carrerasFiltradas
        carrerasFiltradas = FXCollections.observableArrayList(carreras);

        // Configurar el ListView con el modelo de carrera
        lstCarreras.setItems(carrerasFiltradas);

        // Personalizar cómo se muestra cada elemento
        lstCarreras.setCellFactory(listView -> new ListCell<Carrera>() {
            @Override
            protected void updateItem(Carrera carrera, boolean empty) {
                super.updateItem(carrera, empty);
                if (empty || carrera == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Crear el contenedor principal
                    HBox hbox = new HBox();
                    hbox.setSpacing(10);
                    hbox.setAlignment(Pos.CENTER_LEFT);

                    // Crear el contenedor para el texto (información de la carrera)
                    HBox hboxTexto = new HBox();
                    Label label = new Label(carrera.getName() + " - " + carrera.getLocation() + " - " + carrera.getDate());
                    hboxTexto.getChildren().add(label);
                    HBox.setHgrow(hboxTexto, javafx.scene.layout.Priority.ALWAYS);

                    // Crear el contenedor para los botones
                    HBox hboxBotones = new HBox();
                    hboxBotones.setSpacing(5); // Espaciado entre los botones
                    hboxBotones.setAlignment(Pos.CENTER_RIGHT); // Alineación a la derecha

                    // Crear los botones con imágenes
                    Button btnEditar = new Button();
                    btnEditar.setTooltip(new Tooltip("Editar"));

                    Button btnEliminar = new Button();
                    btnEliminar.setTooltip(new Tooltip("Eliminar"));

                    Button btnDescargar = new Button();
                    btnDescargar.setTooltip(new Tooltip("Descargar"));

                    Button btnSubir = new Button();
                    btnSubir.setTooltip(new Tooltip("Subir"));

                    // Asignar las imágenes a los botones
                    ImageView imgEditar = new ImageView(new Image(getClass().getResourceAsStream("/images/editar.png")));
                    ImageView imgEliminar = new ImageView(new Image(getClass().getResourceAsStream("/images/eliminar.png")));
                    ImageView imgDescargar = new ImageView(new Image(getClass().getResourceAsStream("/images/descargar.png")));
                    ImageView imgSubir = new ImageView(new Image(getClass().getResourceAsStream("/images/subir.png")));

                    // Establecer el tamaño pequeño de las imágenes
                    imgEditar.setFitWidth(20); // Ajusta el tamaño
                    imgEditar.setFitHeight(20); // Ajusta el tamaño
                    imgEliminar.setFitWidth(20); // Ajusta el tamaño
                    imgEliminar.setFitHeight(20); // Ajusta el tamaño
                    imgDescargar.setFitWidth(20); // Ajusta el tamaño
                    imgDescargar.setFitHeight(20); // Ajusta el tamaño
                    imgSubir.setFitWidth(20); // Ajusta el tamaño
                    imgSubir.setFitHeight(20); // Ajusta el tamaño

                    // Asignar las imágenes a los botones
                    btnEditar.setGraphic(imgEditar);
                    btnEliminar.setGraphic(imgEliminar);
                    btnDescargar.setGraphic(imgDescargar);
                    btnSubir.setGraphic(imgSubir);

                    // Añadir eventos a los botones
                    btnEditar.setOnAction(event -> editarCarrera(carrera));
                    btnEliminar.setOnAction(event -> eliminarCarrera(carrera));
                    btnDescargar.setOnAction(event -> descargarCarrera(carrera));

                    // Añadir los botones al contenedor de botones
                    hboxBotones.getChildren().addAll(btnEditar, btnEliminar, btnDescargar, btnSubir);

                    // Añadir tanto el contenedor de texto como el de botones al HBox principal
                    hbox.getChildren().addAll(hboxTexto, hboxBotones);

                    setGraphic(hbox); // Establecer la celda con los componentes
                }
            }
        });
    }

    private void establecerIconos() {
        imgLogo.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/logoApp.png")));
        imgAddCarrera.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/anadir.png")));
        imgCerrar.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/cerrar-sesion.png")));
        imgUsers.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/login.png")));
    }

    private void editarCarrera(Carrera carrera) {
        System.out.println("Editando carrera: " + carrera.getName());
        // Aquí puedes agregar la lógica para editar la carrera
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("vista/editCarrera.fxml"));
            Parent root = loader.load();

            System.out.println("ID: "+carrera.get_id());
            ControladorEditCarrera cac= loader.getController();
            System.out.println("ID: "+carrera.get_id());
            if(cac!=null){
                System.out.println("ID: "+carrera.get_id());
                cac.setId(carrera.get_id());
                cac.setAuthToken(authToken);
                cac.setControladorCarrera(this);
                cac.inicializarControles();
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Editar carrera");
            stage.show();
        } catch (IOException e) {
            System.out.println("no se ha podido cargar la ventana");
        }

    }


    private void eliminarCarrera(Carrera carrera) {
        // Mostrar una alerta de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Estás seguro de que quieres eliminar la carrera?");
        alert.setContentText("Carrera: " + carrera.getName());

        // Obtener la respuesta del usuario
        Optional<ButtonType> result = alert.showAndWait();

        // Si el usuario confirma, proceder con la eliminación
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Configurar Retrofit para la petición DELETE
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://" + ip + port + "/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            serviceBorrarCarrera = retrofit.create(ServiceBorrarCarrera.class);

            // Realizar la petición DELETE a la API
            serviceBorrarCarrera.borrarCarrera("Bearer " + authToken, carrera.get_id()).enqueue(new Callback<Carreras>() {
                @Override
                public void onResponse(Call<Carreras> call, Response<Carreras> response) {
                    if (response.isSuccessful()) {
                        // Mostrar mensaje de éxito
                        Platform.runLater(() -> {
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Carrera eliminada");
                            successAlert.setHeaderText(null);
                            successAlert.setContentText("Carrera eliminada correctamente: " + carrera.getName());
                            successAlert.showAndWait();

                            // Actualizar la lista de carreras después de la eliminación
                            actualizarListaCarreras();
                        });
                    } else {
                        // Manejar errores de la API
                        Platform.runLater(() -> {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Error al eliminar la carrera");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Código de error: " + response.code() + "\nMensaje: " + response.message());
                            errorAlert.showAndWait();
                        });
                    }
                }
                private void actualizarListaCarreras() {
                    // Llamar al método que carga las carreras desde la API
                    repository.callLeerCarreras = repository.serviceLeerCarreras.obtenerCarreras();
                    encolaLeerCarreras();
                }

                @Override
                public void onFailure(Call<Carreras> call, Throwable t) {
                    // Manejar errores de red
                    Platform.runLater(() -> {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error de red");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Error al conectar con el servidor: " + t.getMessage());
                        errorAlert.showAndWait();
                    });
                }
            });
        } else {
            // Si el usuario cancela, no hacer nada
            System.out.println("Eliminación cancelada por el usuario.");
        }
    }

    private void descargarCarrera(Carrera carrera) {
        System.out.println("Descargando carrera: " + carrera.getName());
        // Aquí puedes agregar la lógica para descargar la carrera
    }

    private void configurarToggleGroup() {
        toggleGroup = new ToggleGroup();
        optTodas.setToggleGroup(toggleGroup);
        optRunning.setToggleGroup(toggleGroup);
        optTrailRunning.setToggleGroup(toggleGroup);
        optCiclismo.setToggleGroup(toggleGroup);
        optTodas.setSelected(true); // Selecciona una opción por defecto

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> actualizarFiltro());
    }

    private void configurarEventosMouse() {
        configurarEfectoHover(hboxUsers);
        configurarEfectoHover(hboxAddCarrera);
        configurarEfectoHover(hboxCerrar);
    }

    private void configurarEventos() {
        hboxAddCarrera.setOnMouseClicked(event -> {
            abrirVentanaAnadirCarrera();
        });

        hboxUsers.setOnMouseClicked(event -> {
            cambiarVentana("adminUsers.fxml", event, "Administracion Usuarios");  // Llamamos a cambiarVentana con el archivo users.fxml
        });

        hboxCerrar.setOnMouseClicked(event -> {
            cambiarVentana("administracion.fxml", event,"Log In Administrador");
        });
    }

    private void actualizarFiltro() {
        String filtroSeleccionado;

        if (optTodas.isSelected()) {
            filtroSeleccionado = "Todas";
        } else if (optRunning.isSelected()) {
            filtroSeleccionado = "running";
        } else if (optTrailRunning.isSelected()) {
            filtroSeleccionado = "trailRunning";
        } else if (optCiclismo.isSelected()) {
            filtroSeleccionado = "cycling";
        } else {
            filtroSeleccionado = "Todas";  // Valor por defecto
        }

        final String filtroFinal = filtroSeleccionado;  // Declaramos como final

        // Filtrar las carreras según la opción seleccionada
        switch (filtroSeleccionado) {
            case "running" -> {
                repository.callLeerRunning = repository.serviceLeerRunning.obtenerCarreras();
                encolaLeerRunning();
            }
            case "trailRunning" -> {
                repository.callLeerTrailRunning = repository.serviceLeerTrailRunning.obtenerCarreras();
                encolaLeerTrailRunning();
            }
            case "cycling" -> {
                repository.callLeerCycling = repository.serviceLeerCycling.obtenerCarreras();
                encolaLeerCycling();
            }
            case "Todas" -> {
                repository.callLeerCarreras = repository.serviceLeerCarreras.obtenerCarreras();
                encolaLeerCarreras();
            }
        }
    }


    private void configurarEfectoHover(HBox hbox) {
        hbox.setOnMouseEntered(event -> hbox.setStyle("-fx-background-color: #D3D3D3; -fx-cursor: hand;"));
        hbox.setOnMouseExited(event -> hbox.setStyle("-fx-background-color: transparent;"));
    }

    private void cambiarVentana(String fxml, MouseEvent event, String titulo) {
        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/" + fxml));
            Scene scene = new Scene(loader.load());

            // Obtener la ventana actual
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(titulo);
            // Cambiar la escena de la ventana actual
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();  // Manejo de errores si no se puede cargar el FXML
        }
    }
    private void abrirVentanaAnadirCarrera() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("vista/addCarrera.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la ventana de añadir carrera
            ControladorAddCarrera controladorAddCarrera = loader.getController();
            controladorAddCarrera.setAuthToken(authToken); // Pasar el token al controlador
            controladorAddCarrera.setObservable(carreras, lstCarreras); // Pasar la lista de carreras al controlador

            // Crear una nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Añadir Carrera");

            // Hacer que la ventana sea modal
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal
            stage.setResizable(false); // Opcional: Hacer que la ventana no sea redimensionable

            // Mostrar la ventana y esperar hasta que se cierre
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void encolaLeerCarreras() {
        repository.callLeerCarreras.enqueue(new Callback<Carreras>() {
            /**
             * Para errores del tipo: Network Error :: timeout
             */
            @Override
            public void onFailure(Call<Carreras> call, Throwable t) {
                System.out.println("Network Error :: " + t.getLocalizedMessage());
            }

            /**
             * La respuesta del servidor
             */
            @Override
            public void onResponse(Call<Carreras> call, Response<Carreras> response) {
                Platform.runLater(() -> {
                    System.out.println("Respuesta LECTURA " + response);
                    System.out.println("Respuesta LECTURA (Estado HTTP): " + response.message());
                    System.out.println("Respuesta LECTURA: " + response.body());

                    if (response.isSuccessful()) {
                        // Print and check if we get a list of races
                        System.out.println(response.body().getRaces());

                        if (response.body().getRaces() != null) {
                            // Clear the list and add new races
                            carreras.clear();
                            carreras.addAll(response.body().getRaces());

                            // Update the filtered list (this triggers ListView update)
                            carrerasFiltradas.setAll(carreras);  // Ensure the filtered list is updated as well
                        } else {
                            System.out.println("No races available in the response body.");
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

    public void encolaLeerRunning() {
        repository.callLeerRunning.enqueue(new Callback<Carreras>() {
            /**
             * Para errores del tipo: Network Error :: timeout
             */
            @Override
            public void onFailure(Call<Carreras> call, Throwable t) {
                System.out.println("Network Error :: " + t.getLocalizedMessage());
            }

            /**
             * La respuesta del servidor
             */
            @Override
            public void onResponse(Call<Carreras> call, Response<Carreras> response) {
                Platform.runLater(() -> {
                    System.out.println("Respuesta LECTURA " + response);
                    System.out.println("Respuesta LECTURA (Estado HTTP): " + response.message());
                    System.out.println("Respuesta LECTURA: " + response.body());

                    if (response.isSuccessful()) {
                        // Print and check if we get a list of races
                        System.out.println(response.body().getRaces());

                        if (response.body().getRaces() != null) {
                            // Clear the list and add new races
                            carreras.clear();
                            carreras.addAll(response.body().getRaces());

                            // Update the filtered list (this triggers ListView update)
                            carrerasFiltradas.setAll(carreras);  // Ensure the filtered list is updated as well
                        } else {
                            System.out.println("No races available in the response body.");
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

    public void encolaLeerTrailRunning() {
        repository.callLeerTrailRunning.enqueue(new Callback<Carreras>() {
            /**
             * Para errores del tipo: Network Error :: timeout
             */
            @Override
            public void onFailure(Call<Carreras> call, Throwable t) {
                System.out.println("Network Error :: " + t.getLocalizedMessage());
            }

            /**
             * La respuesta del servidor
             */
            @Override
            public void onResponse(Call<Carreras> call, Response<Carreras> response) {
                Platform.runLater(() -> {
                    System.out.println("Respuesta LECTURA " + response);
                    System.out.println("Respuesta LECTURA (Estado HTTP): " + response.message());
                    System.out.println("Respuesta LECTURA: " + response.body());

                    if (response.isSuccessful()) {
                        // Print and check if we get a list of races
                        System.out.println(response.body().getRaces());

                        if (response.body().getRaces() != null) {
                            // Clear the list and add new races
                            carreras.clear();
                            carreras.addAll(response.body().getRaces());

                            // Update the filtered list (this triggers ListView update)
                            carrerasFiltradas.setAll(carreras);  // Ensure the filtered list is updated as well
                        } else {
                            System.out.println("No races available in the response body.");
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

    public void encolaLeerCycling() {
        repository.callLeerCycling.enqueue(new Callback<Carreras>() {
            /**
             * Para errores del tipo: Network Error :: timeout
             */
            @Override
            public void onFailure(Call<Carreras> call, Throwable t) {
                System.out.println("Network Error :: " + t.getLocalizedMessage());
            }

            /**
             * La respuesta del servidor
             */
            @Override
            public void onResponse(Call<Carreras> call, Response<Carreras> response) {
                Platform.runLater(() -> {
                    System.out.println("Respuesta LECTURA " + response);
                    System.out.println("Respuesta LECTURA (Estado HTTP): " + response.message());
                    System.out.println("Respuesta LECTURA: " + response.body());

                    if (response.isSuccessful()) {
                        // Print and check if we get a list of races
                        System.out.println(response.body().getRaces());

                        if (response.body().getRaces() != null) {
                            // Clear the list and add new races
                            carreras.clear();
                            carreras.addAll(response.body().getRaces());

                            // Update the filtered list (this triggers ListView update)
                            carrerasFiltradas.setAll(carreras);  // Ensure the filtered list is updated as well
                        } else {
                            System.out.println("No races available in the response body.");
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
