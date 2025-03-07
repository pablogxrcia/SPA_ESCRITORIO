package controlador;

import api.ServiceEditarCarrera;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
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

    }

    @FXML
    void handleCancel(ActionEvent event) {

    }

    @FXML
    void handleCreateRace(ActionEvent event) {

    }

    private String id;
    private ServiceEditarCarrera eRaceApi;

    public void setId(String id) {
        this.id = id;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}