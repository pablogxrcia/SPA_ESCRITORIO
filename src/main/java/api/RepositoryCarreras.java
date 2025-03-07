package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.cdimascio.dotenv.Dotenv;
import modelo.Carrera;
import modelo.Carreras;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepositoryCarreras {

    Dotenv dotenv = Dotenv.load();
    String ip = dotenv.get("IP");
    String port = dotenv.get("PORT");

    String baseUrl = "http://"+ip+port+"/api/races/";
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public ServiceLeerCarreras serviceLeerCarreras = retrofit.create(ServiceLeerCarreras.class);
    public ServiceLeerRunning serviceLeerRunning = retrofit.create(ServiceLeerRunning.class);
    public ServiceLeerTrailRunning serviceLeerTrailRunning = retrofit.create(ServiceLeerTrailRunning.class);
    public ServiceLeerCycling serviceLeerCycling = retrofit.create(ServiceLeerCycling.class);
    public ServiceLeerCarreraById serviceLeerCarreraById = retrofit.create(ServiceLeerCarreraById.class);
    public ServiceEditarCarrera serviceEditarCarrera = retrofit.create(ServiceEditarCarrera.class);

    public Call<Carreras> callLeerCarreras;
    public Call<Carreras> callLeerRunning;
    public Call<Carreras> callLeerTrailRunning;
    public Call<Carreras> callLeerCycling;
    public Call<Carrera> callLeerCarreraById;

    public Call<Carrera> callEditarCarrera;
}
