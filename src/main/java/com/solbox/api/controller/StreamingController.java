package com.solbox.api.controller;

import com.solbox.api.service.StreamingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ??? on 2017-12-06.
 */
@RestController
public class StreamingController {
    private static Logger log = LoggerFactory.getLogger(StreamingController.class);
    @Autowired private StreamingService streamingService;

    @RequestMapping(value = "/api/streaming", method = RequestMethod.POST)
    public ResponseEntity<?> getStreamingList(@RequestBody Map<String, Object> param) {
        HttpStatus apiStatus = HttpStatus.OK;
        List<Map<String, Object>> streamingList = null;

        try {
            streamingList = streamingService.getStreamingList(param);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            apiStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(streamingList, apiStatus);
    }

    @RequestMapping(value = "/api/streaming/history", method = RequestMethod.POST)
    public ResponseEntity<?> getStreamingHistory(@RequestBody Map<String, Object> param) {
        HttpStatus apiStatus = HttpStatus.OK;
        List<Map<String, Object>> streamingHistoryList = null;
        int totalCount = 0;

        try {
            streamingHistoryList = streamingService.getStreamingHistoryList(param);
            totalCount = streamingService.historyTotalCount(param);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            apiStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        Map<String, Object> returnValue = new HashMap<>();
        returnValue.put("streamingHistoryList", streamingHistoryList);
        returnValue.put("totalCount", totalCount);

        return new ResponseEntity<>(returnValue, apiStatus);
    }
}
