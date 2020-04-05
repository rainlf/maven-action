package com.rainlf.account.web;

import com.rainlf.account.service.AccountService;
import com.rainlf.account.service.AccountServiceException;
import com.rainlf.account.service.SignUpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class SignUpServlet extends HttpServlet {

    private ApplicationContext context;

    @Override
    public void init() throws ServletException {
        super.init();
        context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String email = req.getParameter("email");
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirm_password");
        String captchaKey = req.getParameter("captcha_key");
        String captchaValue = req.getParameter("captcha_value");

        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(email) || StringUtils.isEmpty(name) || StringUtils.isEmpty(password) || StringUtils.isEmpty(confirmPassword) || StringUtils.isEmpty(captchaKey) || StringUtils.isEmpty(captchaValue)) {
            resp.sendError(400, "Parameter incomplete");
        }

        AccountService accountService = (AccountService) context.getBean("accountService");
        SignUpRequest request = new SignUpRequest();
        request.setId(id);
        request.setEmail(email);
        request.setName(name);
        request.setPassword(password);
        request.setConfirmPassword(confirmPassword);
        request.setCaptchaKey(captchaKey);
        request.setCaptchaValue(captchaValue);
        request.setActivateServiceUrl(getServletContext().getRealPath("/") + "activate");
        log.info("SignUpRequest: {}", request);

        try {
            accountService.signUp(request);
            resp.getWriter().print("Account is created, please check your mail box for activation link.");
        } catch (AccountServiceException e) {
            resp.sendError(400, e.getMessage());
        }
    }
}
