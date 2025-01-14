package org.jeecg.modules.energyPlan.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.energyPlan.dto.TimeRangeEnergyPlan;
import org.jeecg.modules.energyPlan.entity.EnergyPlan;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 能源计划
 * @@author 品讯科技
 * @Date:   2024-01-23
 * @Version: V1.0
 */
public interface IEnergyPlanService extends IService<EnergyPlan> {

    Result<String> rangeAdd(TimeRangeEnergyPlan rangeEnergyPlan);
}
