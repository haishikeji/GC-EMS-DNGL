package org.jeecg.modules.collectdata.service.impl;

import org.jeecg.modules.collectdata.entity.Collectdata;
import org.jeecg.modules.collectdata.service.ICollectdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectdataServiceImpl implements ICollectdataService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 获取每个设备的最新一条信息
     * @return
     */
    @Override
    public List<Collectdata> getLatestDataPerGroup() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("equipmentcode").last("logtime").as("logtime"),
                Aggregation.sort(Sort.Direction.DESC, "logtime")
        );

        AggregationResults<Collectdata> aggregatedPage = mongoTemplate.aggregate(aggregation, "equipmentcode", Collectdata.class);
        return aggregatedPage.getMappedResults();
    }
}
