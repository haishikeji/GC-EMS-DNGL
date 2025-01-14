package org.jeecg.modules.cmmsUpkeepPlan.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.cmmsUpkeepItem.entity.CmmsUpkeepItem;
import org.jeecg.modules.cmmsUpkeepItem.entity.CmmsUpkeepItemDto;
import org.jeecg.modules.cmmsUpkeepPlan.entity.CmmsUpkeepPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cmmsUpkeepPlan.entity.CmmsUpkeepPlanDto;

/**
 * @Description: 保养计划
 * @@author 品讯科技
 * @Date:   2024-01-10
 * @Version: V1.0
 */
public interface CmmsUpkeepPlanMapper extends BaseMapper<CmmsUpkeepPlan> {

    /**
     * 分页列表查询
     * 根据设备id查找设备名称并进行两表字段拼接
     *
     */
    public List<CmmsUpkeepPlanDto> getAllCmmsUpkeepPlan(Page<CmmsUpkeepPlanDto> page, CmmsUpkeepPlan cmmsUpkeepPlan);

    /**
     *
     * 根据id查找保养计划
     *
     */
    public CmmsUpkeepPlanDto getCmmsUpkeepPlanContentById(@Param("id") String id);

    /**
     *
     * 根据status查找启用的保养计划
     *
     */
    public List<CmmsUpkeepPlan> getCmmsUpkeepPlanByStatus(@Param("status") String status);

    /**
     *
     * 更新保养计划表
     *
     */
    public boolean updateCmmsUpkeepPlanContentById(@Param("cmmsUpkeepPlanDto") CmmsUpkeepPlanDto cmmsUpkeepPlanDto);

    /**
     * 通过id更新保养计划状态
     *
     */
    public boolean updateCmmsUpkeepPlanStatusById(@Param("cmmsUpkeepPlan") CmmsUpkeepPlan cmmsUpkeepPlan);

    /**
     * 通过设备id将该设备的其它计划都设为不启用
     * 一个设备只能启用一个计划
     *
     */
    public boolean updateCmmsUpkeepPlanStatusByEqid(@Param("cmmsUpkeepPlan") CmmsUpkeepPlan cmmsUpkeepPlan);

    /**
     * 根据保养计划批量更新下次生成工单时间及通知信息
     *
     */
    public boolean updateCmmsUpkeepCreateNoticeByIds(@Param("cmmsUpkeepPlanList") List<CmmsUpkeepPlan> cmmsUpkeepPlanList);

    /**
     * 根据保养计划批量更新保养计划是否提前通知提醒ifnotice
     *
     */
    public boolean updateCmmsUpkeepPlanIfnoticeByIds(@Param("cmmsUpkeepPlanList") List<CmmsUpkeepPlan> cmmsUpkeepPlanList);

}
