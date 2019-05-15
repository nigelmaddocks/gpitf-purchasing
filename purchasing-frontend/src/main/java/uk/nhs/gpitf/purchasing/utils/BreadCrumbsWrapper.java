package uk.nhs.gpitf.purchasing.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class BreadCrumbsWrapper implements IBreadCrumbsWrapper {

    @Override
    public void removeTitle(String title, HttpServletRequest request) {
        Breadcrumbs.removeTitle(title, request);
    }

    @Override
    public void register(String title, HttpServletRequest request) {
        Breadcrumbs.register(title, request.getRequestURI(), request);
    }

}