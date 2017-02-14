package de.memorian.mvpdagger.domain;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author Tom Seifert
 */
public final class SomeUseCase {

    private final LocalRepository localRepository;

    @Inject
    public SomeUseCase(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    public Observable<String> getExampleString() {
        return localRepository.getExampleText();
    }
}
