package api;

import modelo.Carreras;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceLeerRunning {
    @GET("sport/running")
    Call<Carreras> obtenerCarreras();
}