package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import modelo.LoginRequest;
import modelo.User;
import modelo.UserLogin;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositoryUsers {
    String baseUrl = "http://44.203.132.49:3000/api/users/";
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private final Map<String, List<Cookie>> cookieStore = new HashMap<>();
    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(), cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<>();
                }
            })
            .build();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public ServiceLogin serviceLogin = retrofit.create(ServiceLogin.class);

    public Call<UserLogin>callLogin;

    public List<Cookie> getCookies() {
        return cookieStore.get("44.203.132.49");
    }
}
