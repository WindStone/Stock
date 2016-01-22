/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author yuanren.syr
 * @version $Id: HomeController.java, v 0.1 2016/1/11 22:27 yuanren.syr Exp $
 */
@Controller
public class HomeController {
    @RequestMapping(value = { "/", "/index.html" }, method = { RequestMethod.GET })
    public String showHomePage(Map<String, Object> model) {
        return "index";
    }
}
