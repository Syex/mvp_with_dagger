package de.memorian.mvpdagger.presentation.main;

import android.content.Intent;

import de.memorian.mvpdagger.data.model.ExampleModel;
import de.memorian.mvpdagger.domain.SomeUseCase;
import de.memorian.mvpdagger.presentation.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Presenter for {@link MainActivity}.
 *
 * @author Tom Seifert
 */
final class MainPresenter extends BasePresenter<MainView> {

    /**
     * Example domain use case to retrieve data.
     */
    private final SomeUseCase useCase;
    /**
     * An example model simply containing a string.
     */
    private ExampleModel exampleModel;
    /**
     * Observer that is subscribed to an Observable that emits data over a long period.
     */
    private ExampleModelObserver exampleModelObserver = null;

    @Inject
    MainPresenter(SomeUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    protected void activityCreated(Intent intent) {
        if (exampleModel == null) {
            // if model is null the activity most likely got created the very first time
            // this indicates that your presenter needs to fetch all needed data
            exampleModel = new ExampleModel();
            exampleModelObserver = new ExampleModelObserver();
            // this subscription will last for a quite a time and will survive activity recreations
            compositeDisposable.add(
                    useCase.getExampleString()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(exampleModelObserver));
        } else if (exampleModelObserver != null) {
            // activity was recreated and the observer is still retrieving data. Tell it to
            // update the view with the most current value
            exampleModelObserver.updateImmediately();
        } else {
            // activity was recreated and the observer has retrieved all data. Simple set the
            // retrieved data in the view
            getView().updateExampleValue(getExampleValue());
        }
    }

    String getExampleValue() {
        return exampleModel.getValue();
    }

    /**
     * Observer that keeps subscribed when activity gets recreated because of a configuration
     * change.
     */
    private class ExampleModelObserver extends DisposableObserver<String> {

        void updateImmediately() {
            if (getView() != null) {
                getView().updateExampleValue(getExampleValue());
            }
        }

        @Override
        public void onNext(String exampleString) {
            exampleModel.append(exampleString);
            updateImmediately();
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onComplete() {
            exampleModelObserver = null;
        }
    }
}
