package api;
import modelo.Carrera;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Header;

public interface RaceService {
    @PUT("{id}")
    Call<Carrera> updateRace(
            @Path("id") String id,
            @Body Carrera carrera,
            @Header("Cookie") String cookie
    );
}