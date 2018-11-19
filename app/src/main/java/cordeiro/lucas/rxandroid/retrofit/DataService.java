package cordeiro.lucas.rxandroid.retrofit;

import java.util.List;


import cordeiro.lucas.rxandroid.model.User;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DataService {
    public final static String URL_USERS = "https://jsonplaceholder.typicode.com/";

    @GET("users/")
    Call<List<User>> recuperarUsers();

    @GET("users/")
    Observable<List<User>> recuperarUsersRX();
}
