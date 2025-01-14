package org.jeecg.modules.energyPlan.service.impl;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.energyPlan.dto.TimeRangeEnergyPlan;
import org.jeecg.modules.energyPlan.entity.EnergyPlan;
import org.jeecg.modules.energyPlan.mapper.EnergyPlanMapper;
import org.jeecg.modules.energyPlan.service.IEnergyPlanService;
import org.springframework.aop.framework.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description: 能源计划
 * @@author 品讯科技
 * @Date:   2024-01-23
 * @Version: V1.0
 */
@Service
@EnableAspectJAutoProxy(exposeProxy = true)
public class EnergyPlanServiceImpl extends ServiceImpl<EnergyPlanMapper, EnergyPlan> implements IEnergyPlanService {


    @Autowired
    private EnergyPlanMapper energyPlanMapper;

    @Override
    public Result<String> rangeAdd(TimeRangeEnergyPlan rangeEnergyPlan) {
        // 获取开始时间和结束时间
        Date beginTime = rangeEnergyPlan.getBeginTime();
        Date endTime = rangeEnergyPlan.getEndTime();
        // 检查开始日期是否大于结束日期
        if (beginTime.after(endTime)) return Result.error("开始日期大于结束日期，请检查！");

        // 计算时间范围内的天数
        long diffInMillies = endTime.getTime() - beginTime.getTime();
        int diffDay = (int) ((diffInMillies / (24 * 60 * 60 * 1000)) + 1);


        Calendar beginTimeInstance = Calendar.getInstance();
        beginTimeInstance.setTime(beginTime);
        Calendar endTimeInstance = Calendar.getInstance();
        endTimeInstance.setTime(endTime);


        ArrayList<EnergyPlan> energyPlans = new ArrayList<>();
        int i = diffDay + 1;
        int mark = 1;
        while (mark != i) {
            EnergyPlan energyPlan = new EnergyPlan();
            BeanUtils.copyProperties(rangeEnergyPlan, energyPlan);
            energyPlan.setPlandate(beginTimeInstance.getTime());
            energyPlans.add(energyPlan);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String day = simpleDateFormat.format(beginTimeInstance.getTime());
            energyPlanMapper.deleteInfoByIdAndDay(rangeEnergyPlan.getEquipmentid(), rangeEnergyPlan.getEnergytypeid(),day);

            beginTimeInstance.add(Calendar.DAY_OF_MONTH, 1);
            mark++;
        }
        //使用 AopContext.currentProxy(); 必须加注解 @EnableAspectJAutoProxy(exposeProxy = true)
        IEnergyPlanService service = (IEnergyPlanService) AopContext.currentProxy();
        service.saveBatch(energyPlans);
        return Result.OK("添加成功");
    }
}
