package com.solbox.api.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by ??? on 2017-12-06.
 */
@Mapper
public interface StreamingDao {
    List<Map<String, Object>> getStreamingList(Map<String, Object> param);
    List<Map<String, Object>> getStreamingHistoryList(Map<String, Object> param);
    int historyTotalCount(Map<String, Object> param);
}
