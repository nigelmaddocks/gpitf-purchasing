package uk.nhs.gpitf.purchasing.utils;

import org.junit.Test;

public class BreadCrumbsWrapperTest {

    BreadCrumbsWrapper breadCrumbsWrapper = new BreadCrumbsWrapper();

     static String breadCrumbTitle = "cheesey beans";



    @Test
    public void testRemoveTitle() {

        breadCrumbsWrapper.removeTitle("cheesey beans", new HttpServletRequestStub(false, true)); //have stub set a variable here in test class too!!!

        System.err.println(breadCrumbTitle);

    }

    @Test
    public void testRegister() {

        breadCrumbsWrapper.register("and chips", new HttpServletRequestStub(true, false)); //have stub set a variable here in test class too!!!

        System.err.println(breadCrumbTitle);

    }
}
