package org.jeecg.modules.hsmsStatistics.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.Statistics.vo.StatisticsVo;
import org.jeecg.modules.hsmsStatistics.service.IHsmsStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 环境监测数据统计汇总Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Api(tags = "环境安全 - 环境监测数据统计汇总")
@RestController
@RequestMapping("/hsms/statistics")
public class HsmsStatisticsController {
    @Autowired
    private IHsmsStatisticsService hsmsStatisticsService;


    /**
     * 查询环境信息（仪表盘使用，按设备汇总）
     */
    @ApiOperation("查询环境监测数据")
    @GetMapping("/envirinfo")
    public Result<List<StatisticsVo>> envirInfo()
    {
        return Result.ok(hsmsStatisticsService.selectEnvirInfo());
    }
}
