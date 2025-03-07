package controlador;

import api.ServiceCrearCarrera;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelo.Carrera;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ControladorAddCarrera implements Initializable {
    @FXML
    private TextField nameField;
    @FXML private ComboBox<String> sportComboBox;
    @FXML private DatePicker datePicker;
    @FXML private TextField locationField;
    @FXML private TextField distanceField;
    @FXML private TextField maxParticipantsField;
    @FXML private TextField unevennessField;
    @FXML private TextArea tourField;
    @FXML private TextField qualifyingTimeField;
    @FXML private Button createRaceButton;

    private ServiceCrearCarrera raceApi;
    private String authToken; // Campo para almacenar el token

    private ObservableList<Carrera> carreras;

    private ListView<Carrera> lstCarreras;

    // Método para establecer el token
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        System.out.println("Token recibido en ControladorAddCarrera: " + authToken); // Verificar que el token se recibe
    }
    public void setObservable(ObservableList<Carrera> carreras, ListView<Carrera> lstCarreras) {
        this.carreras = carreras;
        this.lstCarreras = lstCarreras;
    }

    @FXML
    void añadirArchivoGpx(ActionEvent event) {
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
    private void handleCreateRace(ActionEvent event) {
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
            Call<Carrera> call = raceApi.createRace("Bearer " + authToken, race); // Suponiendo que el token es de tipo Bearer
            call.enqueue(new Callback<Carrera>() {
                @Override
                public void onResponse(Call<Carrera> call, Response<Carrera> response) {
                    Platform.runLater(() -> {
                        if (response.isSuccessful()) {
                            carreras.add(race);
                            lstCarreras.setItems(FXCollections.observableArrayList(carreras));
                            showAlert("Éxito", "Carrera creada exitosamente.");
                            // Cierra la ventana actual después de mostrar la alerta
                            Stage stage = (Stage) nameField.getScene().getWindow();
                            stage.close();
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

    @FXML
    private void handleCancel(ActionEvent event) {
        // Cierra la ventana actual
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sportComboBox.setItems(FXCollections.observableArrayList("running", "trailRunning", "cycling"));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.50.143:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        raceApi = retrofit.create(ServiceCrearCarrera.class);
    }
}