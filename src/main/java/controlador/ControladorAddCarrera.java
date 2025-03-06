package controlador;

import api.ServiceCrearCarrera;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modelo.Carrera;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

            if (name.isEmpty() || sport == null || date == null || location.isEmpty() || tour.isEmpty() || qualifyingTime.isEmpty()) {
                showAlert("Error", "Todos los campos son requeridos");
                return;
            }

            String formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE) + "T09:00:00Z";

            Carrera race = new Carrera(name, sport, formattedDate, location, distance, maxParticipants, unevenness, tour, qualifyingTime);

            raceApi.createRace(race).enqueue(new Callback<Carrera>() {
                @Override
                public void onResponse(Call<Carrera> call, Response<Carrera> response) {
                    Platform.runLater(() -> {
                        if (response.isSuccessful()) {
                            showAlert("Éxito", "Carrera creada exitosamente.");
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("vista/adminCarreras.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Administracion de Carreras");
            stage.show();

            //Cierra la ventana login
            Stage s = (Stage) this.nameField.getScene().getWindow();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sportComboBox.setItems(FXCollections.observableArrayList("running", "trailRunning", "cycling"));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://44.203.132.49:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        raceApi = retrofit.create(ServiceCrearCarrera.class);
    }
}
