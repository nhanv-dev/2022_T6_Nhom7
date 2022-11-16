package com.controller.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.Commodity;
import com.service.ICommodityService;
import com.service.implement.CommodityService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UnexpiredDataAPI", value = "/api/unexpired-data")
public class UnexpiredDataAPI extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ICommodityService commodityService = new CommodityService();
        List<Commodity> unexpiredData = commodityService.findUnexpiredData();
        String json = GSON.toJson(unexpiredData);
        System.out.println(json);
        response.setHeader("Content-Type", "application/json");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        response.getWriter().println(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}