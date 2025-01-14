package org.jeecg.modules.tpmMessageAlarm.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.jeecg.modules.tpmMessageAlarm.entity.TpmMessageAlarm;
import org.jeecg.modules.tpmMessageAlarm.entity.TpmMessageAlarmStatistics;
import org.jeecg.modules.tpmMessageAlarm.entity.TpmMessageAlarmVo;
import org.jeecg.modules.tpmMessageAlarm.mapper.TpmMessageAlarmMapper;
import org.jeecg.modules.tpmMessageAlarm.service.ITpmMessageAlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: tpm_message_alarm
 * @@author 品讯科技
 * @Date:   2024-04-24
 * @Version: V1.0
 */
@Service
public class TpmMessageAlarmServiceImpl extends ServiceImpl<TpmMessageAlarmMapper, TpmMessageAlarm> implements ITpmMessageAlarmService {

    @Autowired
    @SuppressWarnings("all")
    private TpmMessageAlarmMapper alarmMapper;

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 查询报警信息
    *   date: 2024/4/24
    */
    @Override
    public List<TpmMessageAlarmVo> getTpmMessageAlarmInfo(Page<TpmMessageAlarmVo> page, TpmMessageAlarmVo tpmMessageAlarm) {
        return alarmMapper.getTpmMessageAlarmInfo(page,tpmMessageAlarm);
    }

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 报警分析
    *   date: 2024/4/25
    */
    @Override
    public HashMap getAlarmInfo(List<String> ids, String yearmonth) {
        HashMap<String, List<TpmMessageAlarmStatistics>> resultMap = new HashMap<>();

        Long year;
        Long month;
        if (!"".equals(yearmonth) && yearmonth != null) {
            year = Long.parseLong(yearmonth.substring(0, 4));
            month = Long.parseLong(yearmonth.substring(5, 7));
        }
        else {
            Date standardDate = new Date();
            Calendar calendar = Calendar.getInstance();
            // 设置当前的时间戳
            calendar.setTime(standardDate);
            // 获得当前日期所属月
            month = (long) (calendar.get(calendar.MONTH) + 1);
            // 获得当前日期所属年
            year = (long) calendar.get(Calendar.YEAR);

        }

        TpmMessageAlarmStatistics alarmStatistics = new TpmMessageAlarmStatistics();
        if (ObjectUtil.isNotNull(ids)){
            alarmStatistics.setIds(ids);
        }
        alarmStatistics.setYear(year);
        alarmStatistics.setMonth(month);

        /** 报警统计 */
        List<TpmMessageAlarmStatistics> list1 = alarmMapper.selectAlarmStatistics(alarmStatistics);
        resultMap.put("alarmStatistics",list1);

        /** 信息统计(报警等级) */
        List<TpmMessageAlarmStatistics> list2 = alarmMapper.selectLevelStatistics(alarmStatistics);
        resultMap.put("levelStatistics",list2);

        /** 报警排名(报警类型) */
        List<TpmMessageAlarmStatistics> list3 = alarmMapper.selectAlarmRank(alarmStatistics);
        resultMap.put("alarmRank",list3);

        /** 近两年报警对比图(报警类型) */
        List<TpmMessageAlarmStatistics> list4 = alarmMapper.selectAlarmTwoYear(alarmStatistics);
        resultMap.put("alarmTwoYear",list4);

        /** 报警类型占比 */
        List<TpmMessageAlarmStatistics> list5 = alarmMapper.selectTypeRatio(alarmStatistics);
        resultMap.put("typeRatio",list5);

        /** 报警趋势散点图 */
        List<TpmMessageAlarmStatistics> list6 = alarmMapper.selectAlarmScatter(alarmStatistics);
        resultMap.put("alarmScatter",list6);

        /** 报警类型信息 */
        List<TpmMessageAlarmStatistics> list7 = alarmMapper.selectAlarmType(alarmStatistics);
        resultMap.put("alarmType",list7);

        return resultMap;
    }
}
