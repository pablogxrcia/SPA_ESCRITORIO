package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import modelo.UserLogin;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepositoryUsers {
    String baseUrl = "http://44.203.132.49:3000/api/users/";
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public ServiceLogin serviceLogin = retrofit.create(ServiceLogin.class);

    public Call<UserLogin>callLogin;
}
