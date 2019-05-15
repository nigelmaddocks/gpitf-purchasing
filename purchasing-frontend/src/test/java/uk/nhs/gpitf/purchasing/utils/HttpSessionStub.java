package uk.nhs.gpitf.purchasing.utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

import static org.springframework.test.util.ReflectionTestUtils.setField;

public class HttpSessionStub implements HttpSession {

    public HttpSessionStub(boolean registerBreadcrumbs, boolean removeBreadCrumbs) {
        this.registerBreadcrumbs = registerBreadcrumbs;
        this.removeBreadCrumbs = removeBreadCrumbs;
    }

    public HttpSessionStub() {
    }

    boolean registerBreadcrumbs;
    boolean removeBreadCrumbs;


    @Override
    public long getCreationTime() {
        return 0;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int i) {

    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        Breadcrumbs breadCrumbsStub = new Breadcrumbs();
        if(registerBreadcrumbs) {
            setField(BreadCrumbsWrapperTest.class, "breadCrumbTitle", "cheesey beans and chips");
            return breadCrumbsStub;
        }
        if(removeBreadCrumbs) {
            setField(BreadCrumbsWrapperTest.class, "breadCrumbTitle", "");
            return breadCrumbsStub;
        }
        return new SecurityInfo();
    }

    @Override
    public Object getValue(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String[] getValueNames() {
        return new String[0];
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void putValue(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public void removeValue(String s) {

    }

    @Override
    public void invalidate() {

    }

    @Override
    public boolean isNew() {
        return false;
    }
}
