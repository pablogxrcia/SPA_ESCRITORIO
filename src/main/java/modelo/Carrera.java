package modelo;
public class Carrera {
    private String name;
    private String location;
    private String date;
    private String tipo; // Tipo de carrera: Running, Trail Running, Ciclismo

    public Carrera(String name, String location, String date, String tipo) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.tipo = tipo;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getTipo() {
        return tipo;
    }
}
