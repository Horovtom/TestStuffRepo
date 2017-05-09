package com.cvut.fel.horovtom.presentation;

import org.testng.annotations.Test;

/**
 * @author Tomáš Hamsa on 22.04.2017.
 */
public class MainControllerTest {
    
    @Test
    public void testMain() throws Exception {
        PresenterSingleton.setPresenter(PresenterMock.class);
        Main.main(new String[0]);
    }
}