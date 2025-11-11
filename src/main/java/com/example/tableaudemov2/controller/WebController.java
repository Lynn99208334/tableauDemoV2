package com.example.tableaudemov2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")            // 首頁
    public String index() { return "index"; }      // 對應 templates/index.html

    @GetMapping("/charts")      // 圖表頁
    public String charts() { return "charts"; }    // 對應 templates/charts.html

    @GetMapping("/tables")      // 表格頁
    public String tables() { return "tables"; }    // 對應 templates/tables.html

    @GetMapping("/ccAnalysis")
    public String ccAnalysis() { return "ccAnalysis"; }    // 對應 templates/tables.html

//    // http://localhost:8080/dashboard
//    @GetMapping("/dashboard")
//    public String dashboard() {
//        return "dashboard"; // 對應 templates/dashboard.html
//    }
}