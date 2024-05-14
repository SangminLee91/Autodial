package com.vdc.autocall.controller;


import com.vdc.autocall.common.resolver.CommandMap;
import com.vdc.autocall.common.util.DateUtil;
import com.vdc.autocall.common.util.ExcelView;
import com.vdc.autocall.service.DeviceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DeviceController {

    @Resource(name = "deviceService")
    private DeviceService deviceService;


    @RequestMapping(value="/list")
    public ModelAndView DeviceList(CommandMap commandMap, HttpServletRequest request) throws Exception{
        ModelAndView mv = new ModelAndView("jsonView");
        String userId = (String) request.getSession().getAttribute("user_id");
        commandMap.put("user_id", userId);
        List<Map<String,Object>> map = deviceService.DeviceList(commandMap.getMap());
        mv.addObject("data", map);
        return mv;
    }

    @RequestMapping(value="/add")
    public ModelAndView DeviceAdd(CommandMap commandMap, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("jsonView");
        String userId = (String) session.getAttribute("user_id");
        commandMap.put("user_id", userId);
        deviceService.addDevice(commandMap.getMap());
        return mv;
    }

    @RequestMapping(value="/edit")
    public ModelAndView DeviceEdit(CommandMap commandMap, HttpSession session) throws Exception{
        ModelAndView mv = new ModelAndView("jsonView");
        deviceService.editDevice(commandMap.getMap());
        return mv;
    }

    @RequestMapping(value="/del")
    public ModelAndView DeviceDel(CommandMap commandMap) throws Exception{
        ModelAndView mv = new ModelAndView("jsonView");
        deviceService.delDevice(commandMap.getMap());
        return mv;
    }


    @RequestMapping(value="/statslist")
    public ModelAndView StatsList(CommandMap commandMap) throws Exception{
        ModelAndView mv = new ModelAndView("jsonView");
        List<Map<String,Object>> dvc = deviceService.StatsList(commandMap.getMap());
        mv.addObject("data", dvc);
        return mv;
    }

    @RequestMapping(value="/detaillist")
    public ModelAndView DetailList(CommandMap commandMap) throws Exception{
        ModelAndView mv = new ModelAndView("jsonView");
        List<Map<String,Object>> dvc = deviceService.DetailList(commandMap.getMap());
        mv.addObject("data", dvc);
        return mv;
    }

    @RequestMapping(value="/statslist/exceldown")
    public void stats_excel(CommandMap commandMap, HttpServletResponse response, HttpServletRequest request) throws Exception{
        Map<String, Object> excelData = new HashMap<>();

        List<Map<String,Object>> data = deviceService.getstats_excel(commandMap.getMap());
        excelData.put("data", data);
        excelData.put("start_dt", commandMap.getMap().get("start_dt"));
        excelData.put("end_dt", commandMap.getMap().get("end_dt"));

        ExcelView excel = new ExcelView();
        String filename = "통계_"+ DateUtil.getToday("yyyyMMddhhmmss");
        String templateFile = "stats.xlsx";
        excel.download(request, response, excelData, filename, templateFile);
    }

    @RequestMapping(value="/detaillist/exceldown")
    public void detail_excel(CommandMap commandMap, HttpServletResponse response, HttpServletRequest request) throws Exception{
        Map<String, Object> excelData = new HashMap<>();

        List<Map<String,Object>> data = deviceService.getdetail_excel(commandMap.getMap());
        excelData.put("data", data);
        excelData.put("start_dt", commandMap.getMap().get("start_dt"));
        excelData.put("end_dt", commandMap.getMap().get("end_dt"));

        ExcelView excel = new ExcelView();
        String filename = "상세이력_"+ DateUtil.getToday("yyyyMMddhhmmss");
        String templateFile = "detail.xlsx";
        excel.download(request, response, excelData, filename, templateFile);
    }
}

