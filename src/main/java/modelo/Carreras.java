package modelo;

import java.util.List;

public class Carreras {
    List<Carrera> races;

    public Carreras(List<Carrera> races) {
        this.races = races;
    }

    public List<Carrera> getRaces() {
        return races;
    }

    public void setRaces(List<Carrera> races) {
        this.races = races;
    }
}
