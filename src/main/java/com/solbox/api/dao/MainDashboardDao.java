package com.solbox.api.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by ??? on 2018-05-23.
 */
@Mapper
public interface MainDashboardDao {
    List<Map<String, Object>> getMainDashboard();
    List<String> getServiceList(String custom);
}
