package com.rainlf.account.web;

import com.rainlf.account.service.AccountService;
import com.rainlf.account.service.AccountServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class CaptchaImageServlet extends HttpServlet {

    private ApplicationContext context;

    @Override
    public void init() throws ServletException {
        super.init();
        context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter("key");

        log.info("Key: {}", key);
        if (key == null || key.length() == 0) {
            resp.sendError(400, "No captcha key found");
        } else {
            AccountService accountService = (AccountService) context.getBean("accountService");
            try {
                resp.setContentType("image/png");
                OutputStream out = resp.getOutputStream();
                out.write(accountService.generateCaptchaImage(key));
                out.close();
            } catch (AccountServiceException e) {
                resp.sendError(404, e.getMessage());
            }
        }
    }
}
