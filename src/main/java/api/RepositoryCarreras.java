package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import modelo.Carreras;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepositoryCarreras {
    String baseUrl = "http://44.203.132.49:3000/api/races/";
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

    public Call<Carreras> callLeerCarreras;
    public Call<Carreras> callLeerRunning;
    public Call<Carreras> callLeerTrailRunning;
    public Call<Carreras> callLeerCycling;
}
