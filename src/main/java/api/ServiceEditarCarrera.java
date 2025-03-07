package api;

import modelo.Carrera;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceEditarCarrera {
    @PUT("{id}")
    Call<Carrera> editarCarrera(@Header("Authorization") String token, @Path("id") String id, @Body Carrera carrera);
}