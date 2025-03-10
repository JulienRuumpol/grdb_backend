package com.jr.grdb_backend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController()
@RequestMapping("/redis")
public class RedisSessionTestController {

    private final String HOME_VIEW_COUNT = "HOME_VIEW_COUNT";

    @GetMapping("/")
    public String home(Principal principal, HttpSession session){
        incrementCount(session, HOME_VIEW_COUNT);
        return "hello " + principal.getName();
    }

    @GetMapping("/count")
    public String count (HttpSession session){
        return "HOME_VIEW_COUNT = " + session.getAttribute(HOME_VIEW_COUNT);
    }

    private void incrementCount(HttpSession session, String attr) {
      var homeViewCount =  session.getAttribute(attr) == null ? 0 : (Integer) session.getAttribute(attr);
      session.setAttribute(attr, homeViewCount += 1);
    }
}
