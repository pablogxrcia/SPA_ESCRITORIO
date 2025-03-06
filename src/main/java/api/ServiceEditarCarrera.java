package api;

import modelo.Carrera;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceEditarCarrera {
    @PUT("races/{id}")
    Call<Carrera> editarCarrera(@Path("id") String id, @Body Carrera carrera);
}