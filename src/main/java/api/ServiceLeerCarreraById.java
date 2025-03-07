package api;

import modelo.Carrera;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServiceLeerCarreraById {
    @GET("{id}")
    Call<Carrera> obtenerCarreraPorId(@Path("id") String idCarrera);
}
