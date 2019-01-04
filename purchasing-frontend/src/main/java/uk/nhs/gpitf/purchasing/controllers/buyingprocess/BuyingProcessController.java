package uk.nhs.gpitf.purchasing.controllers.buyingprocess;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/buyingprocess")
public class BuyingProcessController {

  protected static final String PATH = "buying-process/";
  protected static final String PAGE_INDEX = "index";

  @GetMapping
  public String home() {
    return PATH + PAGE_INDEX;
  }

}
