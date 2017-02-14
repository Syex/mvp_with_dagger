package de.memorian.mvpdagger.presentation.base;

/**
 * Base class for all view interfaces.
 *
 * @author Tom-Philipp Seifert
 */
public interface BaseView {

    /**
     * Called by the presenter when it has aquired all models so the view can init the layout by
     * getting data from the presenter.
     */
    void initLayout();
}
