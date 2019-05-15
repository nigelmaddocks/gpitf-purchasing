package uk.nhs.gpitf.purchasing.utils;

import javax.servlet.http.HttpServletRequest;

public interface IBreadCrumbsWrapper {
    void removeTitle(String title, HttpServletRequest request);
    void register(String title, HttpServletRequest request);
}
