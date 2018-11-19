package cordeiro.lucas.rxandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import cordeiro.lucas.rxandroid.model.Pessoa;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AndroidRx";
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        leituraUmaPessoa();
        leiturarList();
        leituraArray();
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

    private void leituraUmaPessoa(){
        Pessoa pessoa = new Pessoa("Lucas");
        Observable<Pessoa> observable = Observable.just(pessoa);
        Observer<Pessoa> observer = definirObserver();
        observable.subscribe(observer);
    }

    private void leiturarList(){
        List<Pessoa> pessoas = new ArrayList<>();
        for(int i=0; i<10;i++){
            pessoas.add(new Pessoa("Pessoa"+i));
        }
        Observable<List<Pessoa>> observable = Observable.just(pessoas);
        Observer<List<Pessoa>> observer = definirObserver();
        observable.subscribe(observer);
    }


    private void leituraArray(){
        Pessoa[] pessoas = new Pessoa[10];
        for(int i=0; i<10;i++){
            pessoas[i]= new Pessoa("Pessoa"+i);
        }
        Observable<Pessoa> observable = Observable.fromArray(pessoas);
        Observer<Pessoa> observer = definirObserver();
        observable.subscribe(observer);
    }
}
