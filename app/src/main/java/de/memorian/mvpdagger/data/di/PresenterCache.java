package de.memorian.mvpdagger.data.di;

import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import de.memorian.mvpdagger.presentation.base.BasePresenter;

import java.util.Map;

/**
 * Caches presenters when their view gets destroyed due to configuration changes.
 *
 * @author Tom-Philipp Seifert
 */
public final class PresenterCache {

    /**
     * Map that contains the cached presenters.
     */
    private final Map<String, BasePresenter> cachedPresenters = new ArrayMap<>();

    /**
     * Returns a {@code presenter} with the given key.
     *
     * @param activityName The key a presenter is supposed to have.
     * @return The {@code presenter} or {@code null} if none was found.
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T getPresenter(String activityName) {
        return (T) cachedPresenters.get(activityName);
    }

    /**
     * Stores the given {@code presenter}.
     *
     * @param activityName The key to store the presenter.
     * @param presenter    Presenter to store.
     */
    public void putPresenter(String activityName, BasePresenter presenter) {
        cachedPresenters.put(activityName, presenter);
    }

    /**
     * Removes the given {@code presenter} from this cache.
     *
     * @param presenterToRemove Presenter to remove.
     */
    public void removePresenter(BasePresenter presenterToRemove) {
        for (Map.Entry<String, BasePresenter> entry : cachedPresenters.entrySet()) {
            if (presenterToRemove.equals(entry.getValue())) {
                cachedPresenters.remove(entry.getKey());
                break;
            }
        }
    }
}
