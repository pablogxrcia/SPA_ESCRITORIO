package api;

import modelo.Carreras;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface ServiceLeerCarreras {
    @GET(".")
    Call<Carreras> obtenerCarreras();
}