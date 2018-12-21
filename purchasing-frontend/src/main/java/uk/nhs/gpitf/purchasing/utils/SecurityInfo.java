package uk.nhs.gpitf.purchasing.utils;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import lombok.Data;

@Data
public class SecurityInfo implements Serializable {
	boolean isAuthenticated;
	boolean isKnown;
	String email;
	String forename;
	String surname;
	long organisationId;
	String organisationName;
	long[] roles;
	String rolesDescription;
	
	public static boolean isAuthenticated(HttpServletRequest request) {
		SecurityInfo secinfo = (SecurityInfo) request.getSession().getAttribute("SecurityInfo");
		if (secinfo == null) {
			return false;
		} else {
			return secinfo.isAuthenticated;
		}
	}
}
