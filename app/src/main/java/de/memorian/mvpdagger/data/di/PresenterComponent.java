package de.memorian.mvpdagger.data.di;

import de.memorian.mvpdagger.presentation.base.BasePresenter;


/**
 * Base Dagger component to provide a {@link BasePresenter} .
 *
 * @author Tom Seifert
 */
public interface PresenterComponent<T extends BasePresenter> {

    T getPresenter();
}
