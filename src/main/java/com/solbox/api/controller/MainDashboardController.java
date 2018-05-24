package com.solbox.api.controller;

import com.solbox.api.service.MainDashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by ??? on 2018-05-23.
 */
@RestController
public class MainDashboardController {
    private Logger log = LoggerFactory.getLogger(MainDashboardController.class);
    @Autowired private MainDashboardService mainDashboardService;

    @RequestMapping(value = "/api/maindashboard", method = RequestMethod.POST)
    public ResponseEntity<?> getMainDashboard(@RequestBody Map<String, Object> param) {
        HttpStatus apiStatus = HttpStatus.OK;
        List<Map<String, Object>> mainDashboardList = null;

        try {
            mainDashboardList = mainDashboardService.getMainDashboard();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            apiStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Object>(mainDashboardList, apiStatus);
    }

}
