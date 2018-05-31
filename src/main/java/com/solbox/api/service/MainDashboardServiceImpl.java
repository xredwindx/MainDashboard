package com.solbox.api.service;

import com.solbox.api.dao.MainDashboardDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by ??? on 2018-05-23.
 */
@Service
public class MainDashboardServiceImpl implements MainDashboardService {
    public static Logger log = LoggerFactory.getLogger(MainDashboardServiceImpl.class);
    @Autowired private MainDashboardDao mainDashboardDao;

    @Value("${maindashboard.influx.api.url}")
    private String influxUrl;
    @Value("${maindashboard.influx.api.user}")
    private String influxUser;
    @Value("${maindashboard.influx.api.pwd}")
    private String influxPwd;
    @Value("${maindashboard.influx.api.db}")
    private String influxDBName;

    @Override
    public List<Map<String, Object>> getMainDashboard() {
        List<Map<String, Object>> list = mainDashboardDao.getMainDashboard();

        for(Map<String, Object> map : list) {
            String custom = (String) map.get("custom");
            String error = this.getInfluxData(custom);
            map.put("gf_status", error);
        }

        return list;
    }

    /**
     * influx http api
     * @param custom
     * @return
     */
    public String getInfluxData(String custom) {
        RestTemplate rest = new RestTemplate();
        List<String> svcList = mainDashboardDao.getServiceList(custom);
        String result = "";

        if(svcList == null || svcList.size() == 0) {
            return "";
        }

        int index = 0;
        String querySvc = "(";
        for(String svc : svcList) {
            if(index == 0) {
                querySvc += "servicename='"+svc+"'";
            } else {
                querySvc += " OR servicename='"+svc+"'";
            }
            index++;
        }
//        querySvc += "servicename='ajnews'";
        querySvc += ")";
        String query = "SELECT sum(error) As status FROM "+influxDBName+".delivery WHERE time > now()-2m AND "+querySvc;
        String url = influxUrl+"/query?db="+influxDBName+"&u="+influxUser+"&p="+influxPwd+"&q="+query;

        try {
            Map<String, Object> jsonMap = rest.getForObject(url, Map.class);
            List<Map<String, Object>> results = (List<Map<String, Object>>)jsonMap.get("results");
            List<Map<String, Object>> series = (List<Map<String, Object>>)results.get(0).get("series");
            if(series != null) {
                List<Object> values = (List<Object>)series.get(0).get("values");
                result = ((List<Object>)values.get(0)).get(1)+"";
            }
        } catch(Exception e) {
            log.error(e.getMessage(), e);
        }

        // json parsing
       return result;
    }
}
