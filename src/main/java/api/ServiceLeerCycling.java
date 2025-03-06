package api;

import modelo.Carreras;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceLeerCycling {
    @GET("sport/cycling")
    Call<Carreras> obtenerCarreras();
}
