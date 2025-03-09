package controlador;

import api.RepositoryCarreras;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelo.Carrera;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ControladorEditCarrera implements Initializable {
    @FXML
    private Button createRaceButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField distanceField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField maxParticipantsField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField qualifyingTimeField;

    @FXML
    private ComboBox<String> sportComboBox;

    @FXML
    private TextArea tourField;

    @FXML
    private TextField unevennessField;

    @FXML
    private Label lblIdCarrera;

    @FXML
    void anadirArchivoGpx(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos GPX (*.gpx)", "*.gpx");
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            System.out.println("Archivo seleccionado: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        // Cierra la ventana actual
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    // ACTUALIZAR CARRERA
    @FXML
    void handleCreateRace(ActionEvent event) {
        try {
            String name = nameField.getText();
            String sport = sportComboBox.getValue();
            LocalDate date = datePicker.getValue();
            String location = locationField.getText();
            double distance = Double.parseDouble(distanceField.getText());
            int maxParticipants = Integer.parseInt(maxParticipantsField.getText());
            int unevenness = Integer.parseInt(unevennessField.getText());
            String tour = tourField.getText();
            String qualifyingTime = qualifyingTimeField.getText();

            // Validación de campos requeridos
            if (name.isEmpty() || sport == null || date == null || location.isEmpty() || tour.isEmpty() || qualifyingTime.isEmpty()) {
                showAlert("Error", "Todos los campos son requeridos");
                return;
            }

            // Validación de valores numéricos
            if (distance <= 0 || maxParticipants <= 0 || unevenness <= 0) {
                showAlert("Error", "Los valores numéricos deben ser positivos");
                return;
            }

            // Validación de formato de tiempo de calificación
            if (!isValidTimeFormat(qualifyingTime)) {
                showAlert("Error", "Formato de tiempo de calificación inválido. Use HH:mm:ss");
                return;
            }

            // Validación de deporte
            if (!isValidSport(sport)) {
                showAlert("Error", "Tipo de deporte inválido. Opciones válidas: running, trailRunning, cycling");
                return;
            }

            // Formatear la fecha sin la hora ni la zona horaria
            String formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE); // Formato: "2025-05-15"

            Carrera race = new Carrera(name, sport, formattedDate, location, distance, maxParticipants, unevenness, tour, qualifyingTime);

            // Depuración: Mostrar los datos que se enviarán al servidor
            System.out.println("Datos enviados al servidor:");
            System.out.println("Id: " + id);
            System.out.println("Nombre: " + name);
            System.out.println("Deporte: " + sport);
            System.out.println("Fecha: " + formattedDate);
            System.out.println("Ubicación: " + location);
            System.out.println("Distancia: " + distance);
            System.out.println("Máximo de participantes: " + maxParticipants);
            System.out.println("Desnivel: " + unevenness);
            System.out.println("Recorrido: " + tour);
            System.out.println("Tiempo de calificación: " + qualifyingTime);

            // Incluir el token en la solicitud
            repository.callEditarCarrera = repository.serviceEditarCarrera.editarCarrera("Bearer " + authToken,id, race);
            repository.callEditarCarrera.enqueue(new Callback<Carrera>() {
                @Override
                public void onResponse(Call<Carrera> call, Response<Carrera> response) {
                    Platform.runLater(() -> {
                        if (response.isSuccessful()) {
                            showAlert("Éxito", "Carrera editada exitosamente.");
                            // Cierra la ventana actual después de mostrar la alerta
                            Stage stage = (Stage) nameField.getScene().getWindow();
                            stage.close();
                            if (cc.optTodas.isSelected()) {
                                cc.encolaLeerCarreras();
                            }if (cc.optRunning.isSelected()) {{
                                cc.encolaLeerRunning();}
                            }if (cc.optTrailRunning.isSelected()) {
                                cc.encolaLeerTrailRunning();
                            }else {
                                cc.encolaLeerCycling();
                            }
                        } else {
                            showAlert("Error", "Error al crear la carrera. Código: " + response.code());
                        }
                    });
                }

                @Override
                public void onFailure(Call<Carrera> call, Throwable t) {
                    Platform.runLater(() -> showAlert("Error", "Fallo en la conexión: " + t.getMessage()));
                }
            });
        } catch (NumberFormatException e) {
            showAlert("Error", "Los valores numéricos deben ser positivos y estar en el formato correcto.");
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean isValidTimeFormat(String time) {
        return time.matches("^([0-1]?\\d|2[0-3]):([0-5]?\\d):([0-5]?\\d)$");
    }

    private boolean isValidSport(String sport) {
        return sport.equals("running") || sport.equals("trailRunning") || sport.equals("cycling");
    }

    private ControladorCarrera cc;
    private String authToken;
    private String id;
    private Carrera carrera;
    private RepositoryCarreras repository = new RepositoryCarreras();

    public void setId(String id) {
        System.out.println("ID: "+id);
        this.id = id;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        System.out.println("Token recibido en ControladorAddCarrera: " + authToken); // Verificar que el token se recibe
    }

    public void inicializarControles() {
        repository.callLeerCarreraById = repository.serviceLeerCarreraById.obtenerCarreraPorId(id);
        repository.callLeerCarreraById.enqueue(new Callback<Carrera>() {

            @Override
            public void onResponse(Call<Carrera> call, Response<Carrera> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful()) {
                        carrera = response.body();

                        lblIdCarrera.setText("ID CARRERA: "+id);
                        nameField.setText(carrera.getName());
                        sportComboBox.setValue(carrera.getSport());
                        String[] date = carrera.getDate().split("-");
                        date[2] = date[2].substring(0,2);
                        datePicker.setValue(LocalDate.of(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2])));
                        locationField.setText(carrera.getLocation());
                        tourField.setText(carrera.getTour());
                        distanceField.setText(String.valueOf(carrera.getDistance()));
                        maxParticipantsField.setText(String.valueOf(carrera.getMaxParticipants()));
                        unevennessField.setText(String.valueOf(carrera.getUnevenness()));
                        qualifyingTimeField.setText(carrera.getQualifyingTime());
                    } else {
                        Alert alertaLeer = new Alert(Alert.AlertType.ERROR);
                        alertaLeer.setTitle("Error");
                        alertaLeer.setHeaderText("No existe la carrera");
                        alertaLeer.setContentText("Código error: " + response.code());
                        alertaLeer.showAndWait();
                    }
                });
            }

            @Override
            public void onFailure(Call<Carrera> call, Throwable t) {
                Alert alertaLeer = new Alert(Alert.AlertType.ERROR);
                alertaLeer.setTitle("Error");
                alertaLeer.setHeaderText("No existe la carrera");
                alertaLeer.setContentText("Error: " + t.getMessage());
                alertaLeer.showAndWait();
            }
        });
    }

    public void setControladorCarrera(ControladorCarrera controladorCarrera) {
        cc = controladorCarrera;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Has entrado a editar carrera");
        sportComboBox.setItems(FXCollections.observableArrayList("running", "trailRunning", "cycling"));
    }

}