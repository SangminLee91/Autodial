package com.vdc.autocall.controller;

import com.vdc.autocall.common.resolver.CommandMap;
import com.vdc.autocall.configuration.WebSessionListener;
import com.vdc.autocall.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Controller
public class AccountController {

    @Resource(name = "accountService")
    private AccountService accountService;
    @Resource(name = "webSessionListener")
    private WebSessionListener webSessionListener;

    @RequestMapping(value="/account/list")
    public ModelAndView AccountList(CommandMap commandMap) throws Exception{
        ModelAndView mv = new ModelAndView("jsonView");
        List<Map<String,Object>> dvc = accountService.AccountList(commandMap.getMap());
        mv.addObject("data", dvc);
        return mv;
    }

    @RequestMapping(value="/account/add")
    public ModelAndView AccountAdd(CommandMap commandMap, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("jsonView");
        accountService.addAccount(commandMap.getMap());
        return mv;
    }
    @RequestMapping(value="/account/del")
    public ModelAndView AccountDel(CommandMap commandMap) throws Exception{
        ModelAndView mv = new ModelAndView("jsonView");
        accountService.delAccount(commandMap.getMap());
        return mv;
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("view/login");
    }

    @RequestMapping("/main")
    public ModelAndView main() {
        return new ModelAndView("view/main");
    }

    @RequestMapping("/monitor")
    public ModelAndView monitor() {
        return new ModelAndView("view/monitor");
    }

    @RequestMapping("/stats")
    public ModelAndView stats() {
        return new ModelAndView("view/stats");
    }
    @RequestMapping("/detail")
    public ModelAndView detail() {
        return new ModelAndView("view/detail");
    }


    @RequestMapping(value = "/login/proc")
    @ResponseBody
    public Map<String, Object> LoginCheck(CommandMap commandMap, HttpServletRequest request) throws Exception {
        Map<String, Object> Rst = new HashMap<>();
        Map<String, Object> LoginRst = accountService.account_login(commandMap.getMap());

        boolean success = false;
        String msg = "";


        if (LoginRst != null && !LoginRst.isEmpty()) {

            HttpSession session = request.getSession();
            if (!webSessionListener.isLoginUser(request, LoginRst.get("user_id").toString())) {
                session.setAttribute("user_id", URLDecoder.decode(LoginRst.get("user_id").toString(), "UTF-8"));
                webSessionListener.setSession(request, URLDecoder.decode(LoginRst.get("user_id").toString(), "UTF-8"));

                success = true;
                msg = "login ok";
                Rst.put("token", session.getId());
                Rst.put("rst", LoginRst);

            } else {
                msg = "Duplicate login user";
            }
        }
        Rst.put("success", success);
        Rst.put("msg", msg);
        return Rst;
    }

    @RequestMapping(value = "/logout")
    @ResponseBody
    public Map<String, Object> LogOut(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> Rst = new HashMap<>();
        try {
            if (session.getAttribute("user_id") != null) {
                webSessionListener.removeSession(request);
            }
            Rst.put("success", true);
            Rst.put("msg", "logout");
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath + "/login");

        } catch (Exception e) {
            Rst.put("success", false);
            Rst.put("msg", e.getMessage());
        }

        return Rst;
    }
}
