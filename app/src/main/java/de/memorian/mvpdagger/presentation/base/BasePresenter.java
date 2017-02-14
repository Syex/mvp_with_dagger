package de.memorian.mvpdagger.presentation.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Tom Seifert
 */
public abstract class BasePresenter<T extends BaseView> {

    /**
     * Contains all active subscriptions.
     */
    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();
    /**
     * The view reference for this presenter.
     */
    private WeakReference<T> viewReference;

    /**
     * Binds the given view to this presenter.
     *
     * @param view View to bind.
     */
    void bindView(T view) {
        if (viewReference != null && viewReference.get() == view) {
            return;
        }
        this.viewReference = new WeakReference<>(view);
    }

    /**
     * @return The view attached to this presenter, or {@code null} if it was unbound.
     */
    protected T getView() {
        if (viewReference == null) {
            return null;
        }
        return viewReference.get();
    }

    /**
     * Called as last when {@link Activity#onCreate(Bundle)} is called.
     *
     * @param intent The {@code Intent} to eventually read arguments from.
     */
    protected abstract void activityCreated(Intent intent);

    /**
     * Called when {@link Activity#onStart()} is called.
     */
    protected void onStart() {
    }

    /**
     * Called when {@link Activity#onRestart()} is called.
     */
    protected void onRestart() {
    }

    /**
     * Called when {@link Activity#onStop()} is called.
     */
    protected void onStop(boolean changingConfigurations) {
        if (!changingConfigurations) {
            // clear all subscriptions if activity is stopped normally
            compositeDisposable.dispose();
        }
        viewReference = null;
    }
}
