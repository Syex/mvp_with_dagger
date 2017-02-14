package de.memorian.mvpdagger;

import android.app.Application;

/**
 * @author Tom Seifert
 */
public final class App extends Application {

    /**
     * Main Dagger component.
     */
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
