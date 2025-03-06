package modelo;
public class Carrera {
    String _id;
    String name;
    String sport;
    String date;
    String location;
    double distance;
    int maxParticipants;
    int unevenness;
    String tour;
    String qualifyingTime;
    String status;
    String created;

    public Carrera(String _id, String name, String sport, String date, String location, double distance, int maxParticipants, int unevenness, String tour, String qualifyingTime, String status, String created) {
        this._id = _id;
        this.name = name;
        this.sport = sport;
        this.date = date;
        this.location = location;
        this.distance = distance;
        this.maxParticipants = maxParticipants;
        this.unevenness = unevenness;
        this.tour = tour;
        this.qualifyingTime = qualifyingTime;
        this.status = status;
        this.created = created;
    }

    public Carrera(String name, String sport, String formattedDate, String location, double distance, int maxParticipants, int unevenness, String tour, String qualifyingTime) {
        this.name = name;
        this.sport = sport;
        this.date = formattedDate;
        this.location = location;
        this.distance = distance;
        this.maxParticipants = maxParticipants;
        this.unevenness = unevenness;
        this.tour = tour;
        this.qualifyingTime = qualifyingTime;
        this.status = "Pendiente";
        this.created = formattedDate;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getUnevenness() {
        return unevenness;
    }

    public void setUnevenness(int unevenness) {
        this.unevenness = unevenness;
    }

    public String getTour() {
        return tour;
    }

    public void setTour(String tour) {
        this.tour = tour;
    }

    public String getQualifyingTime() {
        return qualifyingTime;
    }

    public void setQualifyingTime(String qualifyingTime) {
        this.qualifyingTime = qualifyingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
