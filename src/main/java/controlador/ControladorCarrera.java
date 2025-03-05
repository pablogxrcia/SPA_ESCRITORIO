package controlador;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Carrera;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControladorCarrera implements Initializable {
    @FXML
    private ImageView imgAddCarrera, imgCerrar, imgLogo, imgUsers;

    @FXML
    private ListView<Carrera> lstCarreras;

    @FXML
    private RadioButton optCiclismo, optRunning, optTodas, optTrailRunning;

    @FXML
    private HBox hboxAddCarrera, hboxCerrar, hboxUsers;

    private ToggleGroup toggleGroup;
    private ObservableList<Carrera> carreras;
    private ObservableList<Carrera> carrerasFiltradas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        establecerIconos();
        configurarToggleGroup();
        configurarEventosMouse();
        //configurarEventos();

        // Crear las carreras
        carreras = FXCollections.observableArrayList(
                new Carrera("Maratón de Primavera", "Parque Central", "2025-05-15", "Running"),
                new Carrera("Vuelta Ciclista", "Montaña Alta", "2025-06-20", "Ciclismo")
        );

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
                    Button btnEliminar = new Button();
                    Button btnDescargar = new Button();
                    Button btnSubir = new Button();

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
    }

    private void eliminarCarrera(Carrera carrera) {
        System.out.println("Eliminando carrera: " + carrera.getName());
        // Aquí puedes agregar la lógica para eliminar la carrera
        carreras.remove(carrera);
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

    /*private void configurarEventos() {
        hboxAddCarrera.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("¿Estás seguro que quieres salir?");
            alert.setContentText("Serás redirigido a la pantalla de inicio de sesión.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                cambiarVentana("login.fxml", event);
            }
        });

        hboxUsers.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("¿Estás seguro que quieres salir?");
            alert.setContentText("Serás redirigido a la pantalla de inicio de sesión.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                cambiarVentana("login.fxml", event);
            }
        });

        hboxCerrar.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("¿Estás seguro que quieres salir?");
            alert.setContentText("Serás redirigido a la pantalla de inicio de sesión.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                cambiarVentana("login.fxml", event);
            }
        });
    }*/

    private void actualizarFiltro() {
        String filtroSeleccionado;

        if (optTodas.isSelected()) {
            filtroSeleccionado = "Todas";
        } else if (optRunning.isSelected()) {
            filtroSeleccionado = "Running";
        } else if (optTrailRunning.isSelected()) {
            filtroSeleccionado = "Trail Running";
        } else if (optCiclismo.isSelected()) {
            filtroSeleccionado = "Ciclismo";
        } else {
            filtroSeleccionado = "Todas";  // Valor por defecto
        }

        final String filtroFinal = filtroSeleccionado;  // Declaramos como final

        // Filtrar las carreras según la opción seleccionada
        if (filtroFinal.equals("Todas")) {
            carrerasFiltradas.setAll(carreras); // Mostrar todas las carreras
        } else {
            carrerasFiltradas.setAll(
                    carreras.filtered(carrera -> carrera.getTipo().equals(filtroFinal))  // Usamos la variable final
            );
        }
    }


    private void configurarEfectoHover(HBox hbox) {
        hbox.setOnMouseEntered(event -> hbox.setStyle("-fx-background-color: #D3D3D3; -fx-cursor: hand;"));
        hbox.setOnMouseExited(event -> hbox.setStyle("-fx-background-color: transparent;"));
    }

    /*private void cambiarVentana(String fxml, MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("/vista/" + fxml));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
