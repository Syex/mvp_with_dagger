package de.memorian.mvpdagger.presentation.main;

import de.memorian.mvpdagger.ApplicationComponent;
import de.memorian.mvpdagger.data.di.ActivityScope;
import de.memorian.mvpdagger.data.di.PresenterComponent;

import dagger.Component;

/**
 * @author Tom Seifert
 */
@Component(dependencies = {ApplicationComponent.class},
        modules = {MainModule.class})
@ActivityScope
interface MainComponent extends PresenterComponent<MainPresenter> {

    void inject(MainActivity mainActivity);

}
