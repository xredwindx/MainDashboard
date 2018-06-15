package com.solbox.api.service;

import com.solbox.api.dao.MainDashboardDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
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
            Map<String, String> influxData = this.getInfluxData(custom);
            map.put("gf_status", influxData.get("status"));
            map.put("gf_svc_name", influxData.get("servicename"));
            map.put("gf_svr_id", influxData.get("svr_id"));
        }

        return list;
    }

    @Override
    public List<Map<String, Object>> getErrMsg() {
        RestTemplate rest = new RestTemplate();
        List<Map<String, Object>> result = new ArrayList<>();

        String query = "SELECT level, message FROM "+influxDBName+".delivery WHERE time > now()-3m AND level > 0 ORDER BY time DESC LIMIT 15";
        String url = influxUrl+"/query?db="+influxDBName+"&u="+influxUser+"&p="+influxPwd+"&q="+query;

        try {
            Map<String, Object> jsonMap = rest.getForObject(url, Map.class);
            List<Map<String, Object>> results = (List<Map<String, Object>>)jsonMap.get("results");
            List<Map<String, Object>> series = (List<Map<String, Object>>)results.get(0).get("series");
            if(series != null) {
                List<List<Object>> values = (List<List<Object>>)series.get(0).get("values");

                for(List<Object> item : values) {
                    Map<String, Object> data = new HashMap<>();

                    data.put("time", item.get(0));
                    data.put("status", item.get(1));
                    data.put("msg", item.get(2));

                    result.add(data);
                }
            }
        } catch(Exception e) {
            log.error(e.getMessage(), e);
        }

        return result;
    }

    /**
     * influx http api
     * @param custom
     * @return
     */
    public Map<String, String> getInfluxData(String custom) {
        RestTemplate rest = new RestTemplate();
        List<String> svcList = mainDashboardDao.getServiceList(custom);
        Map<String, String> result = new HashMap<>();

        if(svcList == null || svcList.size() == 0) {
            return result;
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
        querySvc += ")";

        String query = "SELECT svr_id, servicename, max(level) As status FROM "+influxDBName+".delivery WHERE time > now()-2m AND "+querySvc;
        String url = influxUrl+"/query?db="+influxDBName+"&u="+influxUser+"&p="+influxPwd+"&q="+query;

        try {
            Map<String, Object> jsonMap = rest.getForObject(url, Map.class);
            List<Map<String, Object>> results = (List<Map<String, Object>>)jsonMap.get("results");
            List<Map<String, Object>> series = (List<Map<String, Object>>)results.get(0).get("series");
            if(series != null) {
                List<Object> values = (List<Object>)series.get(0).get("values");
                List<Object> item = (List<Object>)values.get(0);

                result.put("svr_id", item.get(1).toString());
                result.put("servicename", item.get(2).toString());
                result.put("status", item.get(3).toString());
            }
        } catch(Exception e) {
            log.error(e.getMessage(), e);
        }

        // json parsing
       return result;
    }


}
