package org.jeecg.modules.cmmsInspectPlan.task;

import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.cmmsInspectPlan.entity.CmmsInspectPlan;
import org.jeecg.modules.cmmsInspectPlan.service.ICmmsInspectPlanService;
import org.jeecg.modules.util.PlanToOrderUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author 品讯科技
 * @date 2024-08
 * @package org.jeecg.modules.cmmsInspectPlan.task
 * @project EMS_server
 * @des 生成巡检工单的定时任务
 */
public class GenerateCmmsInspectTask implements Job {

    @Autowired
    @SuppressWarnings("all")
    private ICmmsInspectPlanService inspectPlanService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        PlanToOrderUtils planToOrderUtils = new PlanToOrderUtils();
        // 获取所有巡检计划
        List<CmmsInspectPlan> planList = inspectPlanService.selectAllPlanOnStatus();
        if (planList == null || planList.size() < 1) {
            return;
        }
        Date now = DateUtils.getDate();
        for (CmmsInspectPlan plan : planList) {
            // 如果巡检计划下次执行时间为空或时间未到，则不生成巡检工单
            if (plan.getNexttime() == null || plan.getNexttime().after(now)) {
                continue;
            }
            inspectPlanService.generateInspect(plan.getId());
        }

    }
}
