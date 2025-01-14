package org.jeecg.modules.tpmMessageAlarm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.tpmMessageAlarm.entity.TpmMessageAlarm;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.tpmMessageAlarm.entity.TpmMessageAlarmVo;

import java.util.HashMap;
import java.util.List;

/**
 * @Description: tpm_message_alarm
 * @@author 品讯科技
 * @Date:   2024-04-24
 * @Version: V1.0
 */
public interface ITpmMessageAlarmService extends IService<TpmMessageAlarm> {

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 查询报警信息
    *   date: 2024/4/24
    */
    List<TpmMessageAlarmVo> getTpmMessageAlarmInfo(Page<TpmMessageAlarmVo> page, TpmMessageAlarmVo tpmMessageAlarm);

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 报警分析
    *   date: 2024/4/25
    */
    HashMap getAlarmInfo(List<String> ids, String yearmonth);
}
