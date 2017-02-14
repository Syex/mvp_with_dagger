package de.memorian.mvpdagger.presentation.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import de.memorian.mvpdagger.App;
import de.memorian.mvpdagger.ApplicationComponent;
import de.memorian.mvpdagger.data.di.PresenterCache;
import de.memorian.mvpdagger.data.di.PresenterComponent;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Base class for all {@link AppCompatActivity activities}.
 *
 * @author Tom Seifert
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity
        implements BaseView {

    /**
     * Contains cached presenters.
     */
    @Inject
    protected PresenterCache presenterCache;
    /**
     * A base {@link dagger.Component component} that provides a new {@link BasePresenter} instance
     * if needed.
     */
    protected PresenterComponent<T> presenterComponent;
    /**
     * The presenter for this {@code activity}.
     */
    protected T presenter;

    protected ApplicationComponent getApplicationComponent() {
        return ((App) getApplication()).getApplicationComponent();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutFile());
        inject();
        ButterKnife.bind(this);
        restoreOrCreatePresenter();
        presenter.activityCreated(getIntent());
    }

    @SuppressWarnings("unchecked")
    private void restoreOrCreatePresenter() {
        // try to get a cached presenter
        presenter = presenterCache.getPresenter(getClass().getName());
        if (presenter == null) {
            // no cached one found, create a new one
            presenter = presenterComponent.getPresenter();
            presenterCache.putPresenter(getClass().getName(), presenter);
        }
        presenter.bindView(this);
    }

    /**
     * @return The layout resource file for this activity,
     */
    @LayoutRes
    protected abstract int getLayoutFile();

    /**
     * Inject all components for this class.
     */
    protected abstract void inject();

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onRestart() {
        super.onRestart();
        // put presenter back to cache and re-bind view
        presenterCache.putPresenter(getClass().getName(), presenter);
        presenter.bindView(this);
        presenter.onRestart();
    }

    @Override
    protected void onStop() {
        if (!isChangingConfigurations()) {
            // activity is stopped normally, remove the cached presenter so it's not cached
            // even if activity gets killed
            presenterCache.removePresenter(presenter);
        }
        // onStop will clear view reference
        presenter.onStop(isChangingConfigurations());
        super.onStop();
    }
}
