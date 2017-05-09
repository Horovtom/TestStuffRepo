package com.cvut.fel.horovtom.presentation;

import javax.annotation.Nonnull;

/**
 * Provides instance of presenter that should be used in UI. This class is necessary for using the test presenter.
 *
 * @author Tomáš Hamsa on 22.04.2017.
 */
class PresenterSingleton {
    /**
     * Presenter that should be used in UI
     */
    private static Presenter presenter;
    
    /**
     * Gets {@link Presenter} that should be used in UI
     *
     * @return some intance of {@link Presenter} that was previously initialized in the programme
     */
    static Presenter getPresenter() {
        return presenter;
    }
    
    /**
     * Initializes instance of {@link Presenter}. Specific class of {@link Presenter} is determined by the argument and the class of the Presenter will be
     * the same as class provided. The presenter is set just one time so if this method is called when the Presenter was already intitialized this method
     * won't do anything
     *
     * @param presenterImpl
     *         Class of Presenter to be initialized
     */
    static void setPresenter(@Nonnull Class<? extends Presenter> presenterImpl) throws IllegalAccessException, InstantiationException {
        if (presenter != null) {
            return;
        }
        presenter = presenterImpl.newInstance();
    }
}
