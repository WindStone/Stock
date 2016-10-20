package stock.web.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import stock.common.util.PathUtil;

/**
 * Created by songyuanren on 2016/7/29.
 */
public class ServletListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.setProperty("logPath", PathUtil.getUserPath());
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
