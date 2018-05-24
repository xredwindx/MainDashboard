package com.solbox.api.service;

import com.solbox.api.dao.MainDashboardDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by ??? on 2018-05-23.
 */
@Service
public class MainDashboardServiceImpl implements MainDashboardService {
    @Autowired private MainDashboardDao mainDashboardDao;

    @Override
    public List<Map<String, Object>> getMainDashboard() {
        return mainDashboardDao.getMainDashboard();
    }
}
