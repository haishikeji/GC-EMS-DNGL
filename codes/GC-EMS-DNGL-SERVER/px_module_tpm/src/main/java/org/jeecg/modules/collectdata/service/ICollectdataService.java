package org.jeecg.modules.collectdata.service;

import org.jeecg.modules.collectdata.entity.Collectdata;

import java.util.List;

public interface ICollectdataService {

    List<Collectdata> getLatestDataPerGroup();
}
