package de.memorian.mvpdagger.domain;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Android clean architecture repository to read from local database.
 *
 * @author Tom Seifert
 */
final class LocalRepository {

    @Inject
    LocalRepository() {

    }

    Observable<String> getExampleText() {
        return Observable
                .just("This", " text", " is", " emitted", " every", " three", " seconds")
                .concatMap(new Function<String, ObservableSource<? extends String>>() {
                    @Override
                    public ObservableSource<? extends String> apply(String s) throws Exception {
                        return Observable.just(s).delay(3, TimeUnit.SECONDS);
                    }
                });
    }
}
