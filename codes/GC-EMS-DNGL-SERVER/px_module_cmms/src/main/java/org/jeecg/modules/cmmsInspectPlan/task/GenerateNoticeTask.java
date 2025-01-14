package org.jeecg.modules.cmmsInspectPlan.task;

import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.constant.enums.MessageTypeEnum;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.cmmsInspectPlan.entity.CmmsInspectPlan;
import org.jeecg.modules.cmmsInspectPlan.service.ICmmsInspectPlanService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 品讯科技
 * @date 2024-08
 * @package org.jeecg.modules.cmmsInspectPlan.task
 * @project EMS_server
 * @des 生成巡检工单提前通知的定时任务
 */
public class GenerateNoticeTask implements Job {

    @Autowired
    @SuppressWarnings("all")
    private ICmmsInspectPlanService inspectPlanService;
    @Autowired
    @SuppressWarnings("all")
    private ISysBaseAPI sysBaseApi;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 获取所有巡检计划
        List<CmmsInspectPlan> planList = inspectPlanService.selectAllPlanOnStatus();
        ArrayList<CmmsInspectPlan> subInspectPlanList = new ArrayList<>();
        if (planList == null || planList.size() < 1) {
            return;
        }
        Date now = DateUtils.getDate();
        for (CmmsInspectPlan plan : planList) {
            // 如果保养计划的下次通知时间为空或时间未到，则不生成消息
            if (plan.getNoticetime() == null || plan.getNoticetime().after(now) || "是".equals(plan.getIfnotice())) {
                continue;
            }
            plan.setIfnotice("是");
            subInspectPlanList.add(plan);
            MessageDTO message = new MessageDTO();
            message.setTitle("巡检工单生成提醒");
            message.setToUser(plan.getChargeruser());
            message.setToAll(false);
            message.setFromUser(plan.getCreateBy());
            message.setType(MessageTypeEnum.XT.getType());
            message.setContent("巡检计划[" + plan.getPlancode() + " " + plan.getPlanname() + "]即将生成巡检工单，请及时处理！");
            sysBaseApi.sendTemplateMessage(message);//发送系统消息
        }

        // 更新巡检计划 , 是否已通知状态
        if (subInspectPlanList != null && subInspectPlanList.size() > 0) {
            //inspectPlanService.updateNotice(subInspectPlanList);
            inspectPlanService.updateBatchById(subInspectPlanList);

            // 更新巡检计划缓存信息
            inspectPlanService.setRedisCache();
        }
    }
}
