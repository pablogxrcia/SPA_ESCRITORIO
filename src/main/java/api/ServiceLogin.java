package api;

import modelo.LoginRequest;
import modelo.UserLogin;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ServiceLogin {
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @POST("login")
    Call<UserLogin> getUser(@Body LoginRequest loginRequest);
}
