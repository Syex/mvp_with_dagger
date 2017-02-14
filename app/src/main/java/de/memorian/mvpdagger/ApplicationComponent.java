package de.memorian.mvpdagger;

import de.memorian.mvpdagger.data.di.PresenterCache;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Tom Seifert
 */
@Component(modules = {ApplicationModule.class})
@Singleton
public interface ApplicationComponent {

    PresenterCache getPresenterCache();
}
