package controlador;

import api.RaceService;
import api.RepositoryCarreras;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import modelo.Carrera;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
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

    private String id;
    private Carrera carrera;
    private String authToken;

    private RepositoryCarreras repositoryCarreras = new RepositoryCarreras();

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
        cargarDatosCarrera();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializar el ComboBox con los deportes válidos
        sportComboBox.getItems().addAll("running", "trailRunning", "cycling");
    }

    private void cargarDatosCarrera() {
        if (carrera != null) {
            nameField.setText(carrera.getName());
            locationField.setText(carrera.getLocation());
            sportComboBox.setValue(carrera.getSport());
            distanceField.setText(String.valueOf(carrera.getDistance()));
            maxParticipantsField.setText(String.valueOf(carrera.getMaxParticipants()));
            unevennessField.setText(String.valueOf(carrera.getUnevenness()));
            qualifyingTimeField.setText(carrera.getQualifyingTime());
            tourField.setText(carrera.getTour());
            datePicker.setValue(parseDate(carrera.getDate()));
            lblIdCarrera.setText(carrera.get_id());
        }
    }

    @FXML
    void anadirArchivoGpx(ActionEvent event) {
        // Lógica para añadir archivo GPX
    }

    @FXML
    void handleCancel(ActionEvent event) {
        // Cerrar la ventana de edición
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleCreateRace(ActionEvent event) {
        try {
            // Validar permisos (asumiendo que tienes un sistema de autenticación)
            if (!esAdmin()) {
                mostrarAlerta("No tienes permisos para actualizar carreras", AlertType.ERROR);
                return;
            }

            // Validar ID
            if (id == null || id.isEmpty()) {
                mostrarAlerta("ID de carrera inválido", AlertType.ERROR);
                return;
            }

            // Crear un objeto Carrera con los campos actualizados
            Carrera updatedCarrera = new Carrera(
                    id,
                    nameField.getText(),
                    sportComboBox.getValue(),
                    parseDate(datePicker.getValue()),
                    locationField.getText(),
                    parseDouble(distanceField.getText()),
                    parseInt(maxParticipantsField.getText()),
                    parseInt(unevennessField.getText()),
                    tourField.getText(),
                    qualifyingTimeField.getText(),
                    carrera.getStatus(),
                    carrera.getCreated()
            );

            // Validar deporte
            if (updatedCarrera.getSport() != null && !isValidSport(updatedCarrera.getSport())) {
                mostrarAlerta("Tipo de deporte inválido. Opciones válidas: running, trailRunning, cycling", AlertType.ERROR);
                return;
            }

            // Validar valores numéricos
            if (updatedCarrera.getDistance() <= 0) {
                mostrarAlerta("El campo distance debe ser un número positivo", AlertType.ERROR);
                return;
            }
            if (updatedCarrera.getMaxParticipants() <= 0) {
                mostrarAlerta("El campo maxParticipants debe ser un número positivo", AlertType.ERROR);
                return;
            }
            if (updatedCarrera.getUnevenness() <= 0) {
                mostrarAlerta("El campo unevenness debe ser un número positivo", AlertType.ERROR);
                return;
            }

            // Validar fecha
            if (updatedCarrera.getDate() == null || !isValidDate(parseDate(updatedCarrera.getDate()))) {
                mostrarAlerta("Fecha inválida", AlertType.ERROR);
                return;
            }

            // Validar tiempo de calificación
            if (updatedCarrera.getQualifyingTime() != null && !isValidTimeFormat(updatedCarrera.getQualifyingTime())) {
                mostrarAlerta("Formato de tiempo de calificación inválido. Use HH:mm:ss", AlertType.ERROR);
                return;
            }

            // Llamar al servicio para actualizar la carrera
            repositoryCarreras.callEditarCarrera = repositoryCarreras.serviceEditar.updateRace(id,updatedCarrera,"token=" + authToken);

            repositoryCarreras.callEditarCarrera.enqueue(new Callback<Carrera>() {
                @Override
                public void onResponse(Call<Carrera> call, Response<Carrera> response) {
                    if (response.isSuccessful()) {
                        mostrarAlerta("Carrera actualizada exitosamente", AlertType.INFORMATION);
                        // Cerrar la ventana de edición
                        Stage stage = (Stage) nameField.getScene().getWindow();
                        stage.close();
                    } else {
                        // Manejar errores de la API
                        String errorMessage = "Error al actualizar la carrera";
                        if (response.errorBody() != null) {
                            try {
                                errorMessage = response.errorBody().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        mostrarAlerta(errorMessage, AlertType.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Carrera> call, Throwable t) {
                    mostrarAlerta("Error de red: " + t.getMessage(), AlertType.ERROR);
                }
            });

        } catch (Exception e) {
            mostrarAlerta("Error al actualizar la carrera: " + e.getMessage(), AlertType.ERROR);
        }
    }

    private boolean esAdmin() {
        // Lógica para verificar si el usuario es admin
        return true; // Cambiar por la lógica real
    }

    private boolean isValidSport(String sport) {
        List<String> validSports = Arrays.asList("running", "trailRunning", "cycling");
        return validSports.contains(sport);
    }

    private boolean isValidDate(LocalDate date) {
        return date != null && !date.isBefore(LocalDate.now());
    }

    private boolean isValidTimeFormat(String time) {
        return time.matches("^([0-1]?\\d|2[0-3]):([0-5]?\\d):([0-5]?\\d)$");
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return -1; // Valor inválido
        }
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1; // Valor inválido
        }
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private String parseDate(LocalDate date) {
        if (date == null) return null;
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private void mostrarAlerta(String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Mensaje");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}