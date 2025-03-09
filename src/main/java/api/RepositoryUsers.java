package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.cdimascio.dotenv.Dotenv;
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
    Dotenv dotenv = Dotenv.load();
    String ip = dotenv.get("IP");
    String port = dotenv.get("PORT");

    String baseUrl = "http://"+ip+port+"/api/users/";
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private final Map<String, List<Cookie>> cookieStore = new HashMap<>();
    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    System.out.println("Cookies recibidas para " + url.host() + ": " + cookies); // Depuración
                    cookieStore.put(url.host(), cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<>(); // Devuelve una lista vacía si no hay cookies
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
        return cookieStore.get(ip);
    }
}
