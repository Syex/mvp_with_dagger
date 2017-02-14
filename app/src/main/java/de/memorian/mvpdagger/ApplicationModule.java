package de.memorian.mvpdagger;

import de.memorian.mvpdagger.data.di.PresenterCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Tom Seifert
 */
@Module
final class ApplicationModule {

    @Provides
    @Singleton
    PresenterCache providePresenterProvider() {
        return new PresenterCache();
    }
}
