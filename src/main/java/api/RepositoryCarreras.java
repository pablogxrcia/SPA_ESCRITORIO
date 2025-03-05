package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import modelo.Carreras;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepositoryCarreras {
    String baseUrl = "http://192.168.50.143:3000/api/races/";
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public ServiceLeerCarreras serviceLeerCarreras = retrofit.create(ServiceLeerCarreras.class);

    public Call<Carreras> callLeerCarreras;
}
