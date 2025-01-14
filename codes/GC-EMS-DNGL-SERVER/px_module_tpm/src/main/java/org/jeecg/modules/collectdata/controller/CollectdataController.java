package org.jeecg.modules.collectdata.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.collectdata.dao.CollectdataRepository;
import org.jeecg.modules.collectdata.entity.Collectdata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.jeecg.common.api.vo.Result;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试mongodb
 */
@Api(tags="测试mongodb")
@RestController
@RequestMapping("/collectdata/collectdata")
public class CollectdataController {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CollectdataRepository repository;



    @ApiOperation(value="测试mongodb-插入", notes="测试mongodb-插入")
    @GetMapping("/insert")
    public Result<?> insert(){
        Map<String,Object> map = new HashMap<>();
        map.put("id","4");
        map.put("version","1.0");
        map.put("equipmentcode","9010100000009");
        map.put("method","thing.event.property.post");
        Map<String,Object> map1 = new HashMap<>();
        map1.put("time",1645791383383L);
        map1.put("ALARM", 0);
        map1.put("ENV_TEMP", 35);
        map1.put("EVA_OUT_TEMP", 56);
        map1.put("EVA_IN_TEMP", 10.6);
        map1.put("COOL_OUT_TEMP", 20);
        map1.put("COOL_IN_TEMP", 12.5);
        map.put("properties", map1);
        mongoTemplate.insert(map, "tpmcollectdata");

        return Result.OK("存入成功");
    }

    @ApiOperation(value="测试mongodb-依据设备编号查询", notes="测试mongodb-依据设备编号查询")
    @GetMapping("/querybycode")
    public Result<?> query(@RequestParam(name="equipmentcode") String equipmentcode) {
//        List<Collectdata> list = repository.findByEquipmentcode(equipmentcode);
        List<Collectdata> list = repository.findByEquipmentcode(equipmentcode);

        return Result.OK(list);
    }

    @ApiOperation(value="测试mongodb-依据设备编号、日期查询", notes="测试mongodb-依据设备编号、日期查询")
    @GetMapping("/querybycodeanddate")
    public Result<?> querybycodeanddate(@RequestParam(name="equipmentcode") String equipmentcode,
                                        @RequestParam(name="begindate") String begindate,
                                        @RequestParam(name="enddate") String enddate) {
        Date bdate = new Date();
        Date edate = new Date();
        try {
            // 开始日期
            if (!"".equals(begindate)) {
                bdate = DateUtils.parseDate(begindate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            }
            else {
                bdate = DateUtils.parseDate(DateUtils.formatDate(bdate, "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            }
            // 结束日期
            if (!"".equals(enddate)) {
                edate = DateUtils.parseDate(enddate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            }
            else {
                edate = DateUtils.parseDate(DateUtils.formatDate(edate, "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Collectdata> list = repository.findByEquipmentcodeAndLogtimeBetween(equipmentcode, bdate, edate);

        return Result.OK(list);
    }

    @ApiOperation(value="测试mongodb-依据日期查询", notes="测试mongodb-依据日期查询")
    @GetMapping("/querybydate")
    public Result<?> querybydate(@RequestParam(name="begindate") String begindate,
                                 @RequestParam(name="enddate") String enddate) {
        Date bdate = new Date();
        Date edate = new Date();
        try {
            // 开始日期
            if (!"".equals(begindate)) {
                bdate = DateUtils.parseDate(begindate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            }
            else {
                bdate = DateUtils.parseDate(DateUtils.formatDate(bdate, "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            }
            // 结束日期
            if (!"".equals(enddate)) {
                edate = DateUtils.parseDate(enddate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            }
            else {
                edate = DateUtils.parseDate(DateUtils.formatDate(edate, "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Collectdata> list = repository.findByLogtimeBetween(bdate, edate);

        return Result.OK(list);
    }
}
