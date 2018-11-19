package cordeiro.lucas.rxandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import cordeiro.lucas.rxandroid.model.User;
import cordeiro.lucas.rxandroid.retrofit.DataService;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AndroidRx";
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRetrofit();
        leituraObservableRetrofit();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private <T> Observer<T> definirObserver() {
        return new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(T t) {
                Log.d(TAG, "onNext: " + t.toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };
    }

    private void setRetrofit() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(DataService.URL_USERS)
                .build();

    }

    private void leituraCallRetrofit(){
        DataService dataService = retrofit.create(DataService.class);
        Call<List<User>> call = dataService.recuperarUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    for(User user : response.body()){
                        Log.d(TAG, "onResponse: "+user.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    private void leituraObservableRetrofit(){
        DataService dataService = retrofit.create(DataService.class);
        Observable<List<User>> observable = dataService.recuperarUsersRX();
        observable
                .subscribeOn(Schedulers.newThread())//Onde será executado
                .observeOn(AndroidSchedulers.mainThread())//Onde será executado o observer
                .subscribe(definirObserver());
    }

    private void leituraUmaPessoa(){
        User user = new User(0,"Lucas", "lucas@email.com");
        subscribeObserver(user);
    }

    private void leiturarList(){
        List<User> users = new ArrayList<>();
        for(int i=0; i<10;i++){
            users.add(new User(i,"Pessoa"+i,"pessoa"+i+"email.com"));
        }
        subscribeObserver(users);
    }

    private void leituraArray(){
        User[] users = new User[10];
        for(int i=0; i<10;i++){
            users[i]= new User(i,"Pessoa"+i, "pessoa"+i+"@email.com");
        }
        subscribeObserverArray(users);
    }


    private <T> void subscribeObserver(T valor){
        Observable.just(valor)
                .filter(new Predicate<T>() {//Filtrar os valores
                    @Override
                    public boolean test(T t) throws Exception {
                        return true;
                    }
                })
                .distinct()//Mesma função do SQL, selecionar valores únicos
                .map(new Function<T, T>() {//Mapeamento dos objetos
                    @Override
                    public T apply(T t) throws Exception {
                        Log.d(TAG, "Map: "+t.toString());
                        return t;
                    }
                })
                .subscribe(definirObserver());
    }

    private void subscribeObserverArray(User[] valor){
        Observable.fromArray(valor).subscribe(definirObserver());
    }
}
