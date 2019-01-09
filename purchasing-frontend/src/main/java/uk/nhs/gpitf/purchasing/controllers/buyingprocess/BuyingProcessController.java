package uk.nhs.gpitf.purchasing.controllers.buyingprocess;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;

@Controller
public class BuyingProcessController {

	protected static final String PATH = "buying-process/";
	protected static final String PAGE_INDEX = "index";
	protected static final String PAGE_SEARCH_SOLUTIONS_MENU = "searchSolutionMenu";

	@GetMapping("/buyingprocess")
	public String home(HttpServletRequest request) {
		Breadcrumbs.register("Buying Process", request);
		return PATH + PAGE_INDEX;
	}

	@GetMapping("/buyingprocess/searchSolutionMenu")
	public String searchSolutionsMenu(HttpServletRequest request) {
		Breadcrumbs.register("Search menu", request);
		return PATH + PAGE_SEARCH_SOLUTIONS_MENU;
	}

}
