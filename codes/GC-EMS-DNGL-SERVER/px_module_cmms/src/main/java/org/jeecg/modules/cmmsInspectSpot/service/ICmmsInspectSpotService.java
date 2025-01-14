package org.jeecg.modules.cmmsInspectSpot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cmmsInspectSpot.entity.CmmsInspectSpot;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cmmsInspectSpot.entity.CmmsInspectSpotVo;

import java.util.List;

/**
 * @Description: 巡检点
 * @@author 品讯科技
 * @Date:   2024-01-17
 * @Version: V1.0
 */
public interface ICmmsInspectSpotService extends IService<CmmsInspectSpot> {

    List<CmmsInspectSpotVo> getCmmsInspectSpotList(Page<CmmsInspectSpotVo> page, CmmsInspectSpot cmmsInspectSpot);

    boolean addCmmsInspectSpot(CmmsInspectSpotVo cmmsInspectSpot);

    boolean updateCmmsInspectSpot(CmmsInspectSpotVo cmmsInspectSpot);

    CmmsInspectSpotVo getCmmsInspectSpotListById(String id);
    CmmsInspectSpotVo getCmmsInspectSpotListTreeById(String id);

    /**
     *   author: dzc
     *   version: 1.0
     *   des: 通过id查询（将巡检项变成 巡检工单-巡检项的格式）
     *   date: 2024/1/26
     */
    CmmsInspectSpotVo getCmmsInspectSpotListByIdA(String id,String inspectid,String tasktype);

    CmmsInspectSpotVo getCmmsInspectSpotListTreeByIdA(String id,String inspectid,String tasktype);

    List<CmmsInspectSpotVo> getCmmsInspectSpotListDetails(Page<CmmsInspectSpotVo> page, CmmsInspectSpot cmmsInspectSpot);

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 根据 id 查询巡检点名称
    *   date: 2024/1/29
    */
    public String getNameById(String id);

    String getCodeById(String relid);
}
