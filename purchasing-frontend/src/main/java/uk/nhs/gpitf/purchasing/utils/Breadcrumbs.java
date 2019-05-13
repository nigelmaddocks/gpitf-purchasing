package uk.nhs.gpitf.purchasing.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.serializer.support.SerializationFailedException;

import lombok.Data;

@Data
public class Breadcrumbs implements Serializable {
	private static final long serialVersionUID = "uk.nhs.gpitf.purchasing.utils.Breadcrumbs".hashCode();
	private ArrayList<Breadcrumb> breadcrumbs = new ArrayList<>();

	public static void register(String title, HttpServletRequest request) {
		register(title, request.getRequestURI(), request);
	}
	
	public static void register(String title, String url, HttpServletRequest request) {
		Breadcrumbs objBreadcrumbs = (Breadcrumbs)request.getSession().getAttribute("Breadcrumbs");
		if (objBreadcrumbs == null) {
			objBreadcrumbs = new Breadcrumbs();
		}
		
		int idxBreadcrumb = 0;
		//boolean bFound = false;
		for (Breadcrumb breadcrumb : objBreadcrumbs.getBreadcrumbs()) {
			if (url.equals(breadcrumb.getUrl()) || title.equals(breadcrumb.title)) {
				List<Breadcrumb> listToRemove = new ArrayList<>();
				for (int idxToRemove = idxBreadcrumb; idxToRemove < objBreadcrumbs.getBreadcrumbs().size(); idxToRemove++) {
					listToRemove.add(objBreadcrumbs.getBreadcrumbs().get(idxToRemove));
				}
				objBreadcrumbs.getBreadcrumbs().removeAll(listToRemove);
				//bFound = true;
				break;
			}
			idxBreadcrumb++;
		}
		
//		if (!bFound) {
			objBreadcrumbs.getBreadcrumbs().add(new Breadcrumb(title, url));
//		}
		
		request.getSession().setAttribute("Breadcrumbs", objBreadcrumbs);
	}
	
	public static void reset(HttpServletRequest request) {
		reset(null, null, request);
	}
	
	public static void reset(String title, HttpServletRequest request) {
		reset(title, request.getRequestURI(), request);
	}
	
	public static void reset(String title, String url, HttpServletRequest request) {
		Breadcrumbs objBreadcrumbs = new Breadcrumbs();
		objBreadcrumbs.setBreadcrumbs(new ArrayList<Breadcrumb>());
		if (url != null && url.trim().length() > 0) {
			objBreadcrumbs.getBreadcrumbs().add(new Breadcrumb(title, url));
		}
		request.getSession().setAttribute("Breadcrumbs", objBreadcrumbs);
	}

	public static void removeTitle(String title, HttpServletRequest request) {
		Breadcrumbs objBreadcrumbs = (Breadcrumbs)request.getSession().getAttribute("Breadcrumbs");
		for (var breadcrumb : objBreadcrumbs.breadcrumbs) {
			if (breadcrumb.title.equals(title)) {
				objBreadcrumbs.breadcrumbs.remove(breadcrumb);
				break;
			}
		}
	}
	
	public static void removeLast(HttpServletRequest request) {
		Breadcrumbs objBreadcrumbs = (Breadcrumbs)request.getSession().getAttribute("Breadcrumbs");
		if (objBreadcrumbs.breadcrumbs.size() > 0) {
			objBreadcrumbs.breadcrumbs.remove(objBreadcrumbs.breadcrumbs.size()-1);
		}
	}

	
	public static void removeAfterTitle(String title, HttpServletRequest request) {
		Breadcrumbs objBreadcrumbs = (Breadcrumbs)request.getSession().getAttribute("Breadcrumbs");
		if (objBreadcrumbs == null) {
			objBreadcrumbs = new Breadcrumbs();
		}
		
		int idxBreadcrumb = 0;
		//boolean bFound = false;
		for (Breadcrumb breadcrumb : objBreadcrumbs.getBreadcrumbs()) {
			if (title.equals(breadcrumb.title)) {
				if (idxBreadcrumb < objBreadcrumbs.getBreadcrumbs().size()-1) {
					List<Breadcrumb> listToRemove = new ArrayList<>();
					for (int idxToRemove = idxBreadcrumb+1; idxToRemove < objBreadcrumbs.getBreadcrumbs().size(); idxToRemove++) {
						listToRemove.add(objBreadcrumbs.getBreadcrumbs().get(idxToRemove));
					}
					objBreadcrumbs.getBreadcrumbs().removeAll(listToRemove);
					//bFound = true;
					break;
				}
			}
			idxBreadcrumb++;
		}
		
		request.getSession().setAttribute("Breadcrumbs", objBreadcrumbs);
	}
	
	public static List<Breadcrumb> renderable(HttpServletRequest request) {
		Breadcrumbs objBreadcrumbs = (Breadcrumbs)request.getSession().getAttribute("Breadcrumbs");
		if (objBreadcrumbs == null) {
			return new ArrayList<Breadcrumb>();
		}
		if (objBreadcrumbs.getBreadcrumbs().size() > 0) {
			return objBreadcrumbs.getBreadcrumbs().subList(0, objBreadcrumbs.getBreadcrumbs().size());
		} else {
			return new ArrayList<Breadcrumb>();
		}
	}

	@Data
	public static class Breadcrumb implements Serializable {
		private static final long serialVersionUID = 3105097486353356333L;
		private String title;
		private String url; 
		public Breadcrumb (String title, String url) {
			this.title = title;
			this.url = url;
		}		
	}
}
