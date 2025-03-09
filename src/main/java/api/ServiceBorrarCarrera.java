package api;

import modelo.Carreras;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ServiceBorrarCarrera {
    @DELETE("races/{id}")
    Call<Carreras> borrarCarrera(
            @Header("Authorization") String authToken,
            @Path("id") String id
    );
}