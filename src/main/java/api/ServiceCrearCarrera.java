package api;

import modelo.Carrera;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ServiceCrearCarrera {
    @POST("races")
    Call<Carrera> createRace(@Header("Authorization") String token, @Body Carrera race);
}