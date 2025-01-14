package org.jeecg.modules.emsStatistics.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.emsStatistics.service.IEmsStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Map;

@Api(tags = "能耗管理")
@RestController
@RequestMapping("/emsStatistics")
public class EmsStatisticsController {

    @Autowired
    private IEmsStatisticsService emsStatisticsService;

    /**
     * 能耗总览
     */
    @ApiOperation("能耗总览")
    @GetMapping(value = "/energyOverview")
    public Result<Map<String, Object>> energyOverview(String spaceId) {
        return emsStatisticsService.energyOverview(spaceId);
    }

    /**
     * 需量分析
     */
    @ApiOperation("需量分析")
    @GetMapping("/demandanalysis")
    public Result<Object> demandAnalysis(String spaceId, String beginTime, String endTime) {
        return emsStatisticsService.DemandAnalysis(spaceId, beginTime, endTime);
    }

    /**
     * 节能分析
     */
    @ApiOperation("节能分析")
    @GetMapping("/savinganalysis")
    public Result<Object> savingAnalysis(String spaceId, String yearMonth) throws ParseException {
        return emsStatisticsService.savingAnalysis(spaceId, yearMonth);
    }

    /**
     * 能耗排名
     */
    @ApiOperation("能耗排名")
    @GetMapping("/ranking")
    public Result<Object> ranking(String spaceId, String beginTime, String endTime) {
        return emsStatisticsService.ranking(spaceId, beginTime, endTime);
    }

    /**
     * 分项能耗
     */
    @ApiOperation("分项能耗")
    @GetMapping("/itemizedenergy")
    public Result<Object> itemizedEnergy(String spaceId, String day) throws ParseException {
        return emsStatisticsService.itemizedEnergy(spaceId, day);
    }

    /**
     * 用能平衡分析
     */
    @ApiOperation("用能平衡分析")
    @GetMapping("/energybalanceanalysis")
    public Result<Object> energyBalanceAnalysis() {
        return emsStatisticsService.energyBalanceAnalysis();
    }

    /**
     * 峰平谷分析
     */
    @ApiOperation("峰平谷分析")
    @GetMapping("/peakvalleyanalysis")
    public Result<Object> peakValleyAnalysis() {
        return emsStatisticsService.peakValleyAnalysis();
    }

    /**
     * 能耗定额
     */
    @ApiOperation("能耗定额")
    @GetMapping("/energyauota")
    public Result<Object> energyQuota() {
        return emsStatisticsService.energyQuota();
    }

    /**
     * 能流分析
     */
    @ApiOperation("能流分析")
    @GetMapping("/energyflowAnalysis")
    public Result<Object> energyFlowAnalysis(String spaceId, String beginTime, String endTime) {
        return emsStatisticsService.energyFlowAnalysis(spaceId, beginTime, endTime);
    }

    /**
     * 查询设备详情（按日期查询电量、电量、电压、功率）
     */
    @ApiOperation("查询设备详情（按日期查询电量、电量、电压、功率）")
    @GetMapping("/searchEquipmentDetail")
    public Result<Object> searchEquipmentDetail(String equipmentid, String day, String tagtype) {
        return emsStatisticsService.searchEquipmentDetail(equipmentid, day, tagtype);
    }
}
