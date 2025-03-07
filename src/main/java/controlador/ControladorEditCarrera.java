package controlador;

import api.ServiceEditarCarrera;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ControladorEditCarrera implements Initializable {
    private String id;
    private ServiceEditarCarrera eRaceApi;

    public void setId(String id) {
        this.id = id;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}