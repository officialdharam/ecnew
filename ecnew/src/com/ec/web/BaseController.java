package com.ec.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ec.exception.AuthorizationException;
import com.ec.exception.LoginException;

@Controller
public class BaseController {
    
    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void authorizationException(Throwable ex, HttpServletRequest request) {
        return;
    }
    
    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String loginError(Throwable ex, HttpServletRequest request) {
	request.setAttribute("loginError", "true");
        return "home";
    }
    
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String genericError(Throwable e) {
        return "error";
    }

}
