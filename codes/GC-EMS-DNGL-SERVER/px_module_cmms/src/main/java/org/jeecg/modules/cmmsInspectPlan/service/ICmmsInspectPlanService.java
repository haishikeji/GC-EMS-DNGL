package org.jeecg.modules.cmmsInspectPlan.service;

import org.jeecg.modules.cmmsInspectPlan.entity.CmmsInspectPlan;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cmmsInspectPlan.entity.CmmsInspectPlanVo;

import java.util.List;

/**
 * @Description: 巡检计划
 * @@author 品讯科技
 * @Date:   2024-01-23
 * @Version: V1.0
 */
public interface ICmmsInspectPlanService extends IService<CmmsInspectPlan> {

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 添加巡检计划
    *   date: 2024/1/23
    */
    boolean addCmmsInspectPlan(CmmsInspectPlanVo cmmsInspectPlan);

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 修改巡检计划
    *   date: 2024/1/23
    */
    boolean updateCmmsInspectPlan(CmmsInspectPlanVo cmmsInspectPlan);

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 删除巡检计划
    *   date: 2024/1/23
    */
    boolean deletePlanById(String id);

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 批量删除巡检计划
    *   date: 2024/1/23
    */
    boolean deleteBatchByIds(String ids);

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 修改状态
    *   date: 2024/1/23
    */
    boolean updateCmmsInspectPlanStatus(CmmsInspectPlanVo cmmsInspectPlan);

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 根据id查询
    *   date: 2024/1/24
    */
    CmmsInspectPlanVo getCmmsInspectPlanById(String id);

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 生成巡检工单
    *   date: 2024/1/25
    */
    boolean generateInspect(String id);

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 获取所有的巡检计划
    *   date: 2024/1/26
    */
    List<CmmsInspectPlan> selectAllPlanOnStatus();

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 修改巡检计划 是否已通知状态
    *   date: 2024/1/29
    */
    public int updateNotice(List<CmmsInspectPlan> inspectPlanList);

    /**
     *   author: dzc
     *   version: 1.0
     *   des: 添加缓存
     *   date: 2024/1/29
     */
    public void setRedisCache();

    CmmsInspectPlanVo getCmmsInspectPlanTreeById(String id);

}
