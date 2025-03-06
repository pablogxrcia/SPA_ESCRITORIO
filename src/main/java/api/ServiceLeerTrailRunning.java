package api;

import modelo.Carreras;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceLeerTrailRunning {
    @GET("sport/trailRunning")
    Call<Carreras> obtenerCarreras();
}
