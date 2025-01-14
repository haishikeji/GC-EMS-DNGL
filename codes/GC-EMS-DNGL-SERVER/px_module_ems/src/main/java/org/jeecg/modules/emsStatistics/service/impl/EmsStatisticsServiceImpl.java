package org.jeecg.modules.emsStatistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.baseSpace.entity.Space;
import org.jeecg.modules.baseSpace.entity.SpaceDto;
import org.jeecg.modules.baseSpace.mapper.SpaceMapper;
import org.jeecg.modules.baseSpace.service.ISpaceService;
import org.jeecg.modules.dataCurrent.entity.DataCurrent;
import org.jeecg.modules.dataCurrent.mapper.DataCurrentMapper;
import org.jeecg.modules.dataElectricity.entity.DataElectricity;
import org.jeecg.modules.dataElectricity.mapper.DataElectricityMapper;
import org.jeecg.modules.dataPower.entity.DataPower;
import org.jeecg.modules.dataPower.mapper.DataPowerMapper;
import org.jeecg.modules.dataVoltage.entity.DataVoltage;
import org.jeecg.modules.dataVoltage.mapper.DataVoltageMapper;
import org.jeecg.modules.elecfeeBase.entity.ElecfeeBase;
import org.jeecg.modules.elecfeeBase.mapper.ElecfeeBaseMapper;
import org.jeecg.modules.emsStatistics.entity.EmsStatistics;
import org.jeecg.modules.emsStatistics.mapper.EmsStatisticsMapper;
import org.jeecg.modules.emsStatistics.service.IEmsStatisticsService;
import org.jeecg.modules.energyItem.entity.EnergyItem;
import org.jeecg.modules.energyItem.entity.EnergyItemDto;
import org.jeecg.modules.energyItem.mapper.EnergyItemMapper;
import org.jeecg.modules.equipmentStatus.entity.EquipmentStatus;
import org.jeecg.modules.equipmentStatus.mapper.EquipmentStatusMapper;
import org.jeecg.modules.tpmEquipment.entity.TpmEquipment;
import org.jeecg.modules.tpmEquipment.mapper.TpmEquipmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmsStatisticsServiceImpl implements IEmsStatisticsService {

    @Autowired
    private EmsStatisticsMapper emsStatisticsMapper;

    @Autowired
    private ISpaceService spaceService;

    @Autowired
    private TpmEquipmentMapper equipmentMapper;

    @Autowired
    private DataElectricityMapper electricityMapper;

    @Autowired
    private EnergyItemMapper energyItemMapper;

    @Autowired
    private ElecfeeBaseMapper elecfeeBaseMapper;

    @Autowired
    private DataCurrentMapper dataCurrentMapper;

    @Autowired
    private DataVoltageMapper dataVoltageMapper;

    @Autowired
    private DataPowerMapper dataPowerMapper;

    @Autowired
    private EquipmentStatusMapper equipmentStatusMapper;

    @Autowired
    private SpaceMapper spaceMapper;
    /**
     * 能耗总览
     *
     * @param spaceId 区域ID
     * @return 分析统计
     */
    @Override
    public Result<Map<String, Object>> energyOverview(String spaceId) {
        HashMap<String, Object> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        // 设置当前的时间戳
        calendar.setTime(new Date());
        // 设置星期一为一周开始的第一天
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期所属周
        LocalDate localDate = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        int week = localDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

        // 获得当前日期所属月
        int month = calendar.get(Calendar.MONTH) + 1;
        // 获得当前日期所属年
        int year = calendar.get(Calendar.YEAR);

        // 上个月
        Calendar calendarLast = Calendar.getInstance();
        calendarLast.setTime(new Date());
        calendarLast.add(Calendar.MONTH, -1);
        // 获得上个月
        int monthLast = calendarLast.get(Calendar.MONTH) + 1;
        // 获得上个月所属年
        int yearLast = calendarLast.get(Calendar.YEAR);

        // 正常查询参数（只包括年、月）
        EmsStatistics statisticsParams = new EmsStatistics();
        statisticsParams.setEquipmentid(spaceId);
        statisticsParams.setYear(year);
        statisticsParams.setMonth(month);

        //区域基本信息
        Space space = spaceService.getById(spaceId);
        if (Objects.isNull(space)) space = new Space();
        map.put("energySpace", space);

        // 本月用电top5
        LambdaQueryWrapper<TpmEquipment> eq = new LambdaQueryWrapper<TpmEquipment>()
                .eq(TpmEquipment::getSpaceid, spaceId)
                .select(TpmEquipment::getId);
        List<TpmEquipment> equipments = equipmentMapper.selectList(eq);
        List<String> equipmentids = new ArrayList<>();
        if (equipments != null && !equipments.isEmpty()) {
            equipmentids = equipments.stream().map(TpmEquipment::getId).collect(Collectors.toList());
        }
        // 设置区域下的所有的设备信息
        statisticsParams.setIds(equipmentids);
        // 如果选择的是整个厂区的，需要去掉配电室几个
        if ("AREA0002".equals(space.getSerialnum())) {
            equipmentids.remove("301156882513921");
            equipmentids.remove("301156930748417");
            equipmentids.remove("301156947525633");
            equipmentids.remove("301156962205697");
        }

        List<EmsStatistics> topStatistics = new ArrayList<>();
        if (equipmentids.size() > 0) {
            topStatistics = emsStatisticsMapper.selectMonthElectricity(statisticsParams);
        }
        // top5上个月的用电量
        if (topStatistics != null && topStatistics.size() > 0) {
            for (EmsStatistics topStatistic : topStatistics) {
                EmsStatistics top5LastParams = new EmsStatistics();
                top5LastParams.setYear(yearLast);
                top5LastParams.setMonth(monthLast);
                top5LastParams.setEquipmentid(topStatistic.getEquipmentid());
                EmsStatistics lastValue = emsStatisticsMapper.selectMonthEquipElectricity(top5LastParams);
                if (lastValue != null) {
                    topStatistic.setHowManyValue2(lastValue.getHowManyValue2());
                }
            }
            map.put("top5Statistics", topStatistics);
        } else {
            map.put("top5Statistics", new ArrayList<>());
        }

        // 用电需量
        List<EmsStatistics> demandStatistics = emsStatisticsMapper.selectMonthDemand(statisticsParams);
        if (demandStatistics == null || demandStatistics.isEmpty()) {
            demandStatistics = new ArrayList<>();
            EmsStatistics demand = new EmsStatistics();

            demand.setType("需量峰值");
            demand.setHowManyValue(0);
            demandStatistics.add(demand);

            demand.setType("即时需量");
            demand.setHowManyValue(0);
            demandStatistics.add(demand);

            demand.setType("目标需量");
            demand.setHowManyValue(0);
            demandStatistics.add(demand);
        }

        if (demandStatistics.size() < 3) {

            List<EmsStatistics> demands1 = demandStatistics.stream().filter(sta -> "需量峰值".equals(sta.getType())).collect(Collectors.toList());
            if (demands1.size() < 1) {
                EmsStatistics demand = new EmsStatistics();
                demand.setType("需量峰值");
                demand.setHowManyValue(0);
                demandStatistics.add(demand);
            }

            List<EmsStatistics> demands2 = demandStatistics.stream().filter(sta -> "即时需量".equals(sta.getType())).collect(Collectors.toList());
            if (demands2.size() < 1) {
                EmsStatistics demand = new EmsStatistics();
                demand.setType("即时需量");
                demand.setHowManyValue(0);
                demandStatistics.add(demand);
            }

            List<EmsStatistics> demands3 = demandStatistics.stream().filter(sta -> "目标需量".equals(sta.getType())).collect(Collectors.toList());
            if (demands3.size() < 1) {
                EmsStatistics demand = new EmsStatistics();
                demand.setType("目标需量");
                demand.setHowManyValue(0);
                demandStatistics.add(demand);
            }
        }
        map.put("demandStatistics", demandStatistics);

        // 环境信息
        List<EmsStatistics> environmentStatistics = new ArrayList<>();
        // 如果选择的是整个厂区的，则只需要获取总控的二氧化碳信息即可
        if ("AREA0002".equals(space.getSerialnum())) {
            EmsStatistics statisticsParams1 = new EmsStatistics();
            List<String> equip1 = new ArrayList<>();
            equip1.add("301156882513921");
            statisticsParams1.setIds(equip1);
            statisticsParams1.setYear(year);
            statisticsParams1.setMonth(month);
            environmentStatistics = emsStatisticsMapper.selectEnvironment(statisticsParams1);
        }
        else if (equipmentids.size() > 0) {
            environmentStatistics = emsStatisticsMapper.selectEnvironment(statisticsParams);
        }
        map.put("environmentStatistics", environmentStatistics);

        // 能源消耗总标煤
        List<EmsStatistics> kgceStatistics = new ArrayList<>();

        // 如果选择的是整个厂区、配电室的，则只需要获取配电室的几个表的信息即可
        if ("AREA0002".equals(space.getSerialnum()) || "AREA0015".equals(space.getSerialnum())) {
            kgceStatistics = emsStatisticsMapper.selectKgceMonthTotal(statisticsParams);
        }
        else if (equipmentids.size() > 0) {
            kgceStatistics = emsStatisticsMapper.selectKgceMonth(statisticsParams);
        }
        map.put("kgceStatistics", kgceStatistics);

        // 总用电趋势图
        String yearMonth = DateUtils.parseDateToStr("yyyy-MM", new Date());
        String thisdate = DateUtils.getLastDay(yearMonth.substring(0,4), yearMonth.substring(5));
        int lastDay = Integer.parseInt(thisdate.substring(8));

        List<String> daysplitStr = new ArrayList<>(); // 天数拆分
        List<Float> dongli = new ArrayList<>();
        List<Float> zhaoming = new ArrayList<>();
        List<Float> kongtiao = new ArrayList<>();
        List<Float> qita = new ArrayList<>();


        for(int i = 1; i < lastDay; i++) {
            String day = i < 10 ? "0" + i : String.valueOf(i);
            daysplitStr.add(day);
            dongli.add(0f);
            zhaoming.add(0f);
            kongtiao.add(0f);
            qita.add(0f);
            List<EmsStatistics> dayStatis = null;
            // 如果选择的是整个厂区、配电室的，则只需要获取配电室的几个表的信息即可
            if ("AREA0002".equals(space.getSerialnum()) || "AREA0015".equals(space.getSerialnum())) {
                dayStatis = emsStatisticsMapper.selectItemByEquipidAndDayTotal(equipmentids,yearMonth + "-" + day);
            }
            else if (equipmentids.size() > 0) {
                dayStatis = emsStatisticsMapper.selectItemByEquipidAndDay(equipmentids,yearMonth + "-" + day);
            }
            if (dayStatis != null && dayStatis.size() > 0) {
                for (EmsStatistics daysta : dayStatis) {
                    switch (daysta.getEquipmentname()) {
                        case "动力用电":
                            dongli.set(i - 1, daysta.getHowManyValue());
                            break;
                        case "照明用电":
                            zhaoming.set(i - 1, daysta.getHowManyValue());
                            break;
                        case "空调用电":
                            kongtiao.set(i - 1, daysta.getHowManyValue());
                            break;
                        case "其它用电":
                            qita.set(i - 1, daysta.getHowManyValue());
                            break;
                    }
                }
            }
        }
        map.put("daysplit", daysplitStr);
        map.put("dongli", dongli);
        map.put("zhaoming", zhaoming);
        map.put("kongtiao", kongtiao);
        map.put("qita", qita);
        return Result.ok(map);
    }

    /**
     * 需量分析
     *
     * @param spaceId   区域ID
     * @param beginTime 开始年月
     * @param endTime   结束年月
     * @return 分析统计
     */
    @Override
    public Result<Object> DemandAnalysis(String spaceId, String beginTime, String endTime) {
        Map<String,Object> map = new HashMap<>();
        LambdaQueryWrapper<TpmEquipment> eq = new LambdaQueryWrapper<TpmEquipment>()
                .eq(TpmEquipment::getSpaceid, spaceId)
                .select(TpmEquipment::getId);
        List<TpmEquipment> equipments = equipmentMapper.selectList(eq);
        List<String> equipmentids = new ArrayList<>();
        if (equipments != null && !equipments.isEmpty()) {
            equipmentids = equipments.stream().map(TpmEquipment::getId).collect(Collectors.toList());
        }
        List<String> months = DateUtils.MonthSplit(beginTime, endTime);
        List<String> monthSplit = new ArrayList<>();
        List<Double> mubiao = new ArrayList<>();
        List<Float> shiji = new ArrayList<>();

        // 循环遍历年月
        for (String month : months) {
            monthSplit.add(month.substring(5));

            Float demand = 0f;
            if (equipmentids.size() > 0) {
                demand = emsStatisticsMapper.SelectMaxDmand(equipmentids, month);
            }
            shiji.add(demand == null ? 0f : demand);

           ElecfeeBase elecfeeBase = elecfeeBaseMapper.selectDemandByYearmonth(month);
            mubiao.add(elecfeeBase == null ? 0f : elecfeeBase.getKva());
        }
        map.put("monthsplit", monthSplit);
        map.put("mubiao", mubiao);
        map.put("shiji", shiji);
        return Result.OK(map);
    }

    /**
     * 节能分析
     *
     * @param spaceId   区域ID
     * @param yearMonth 年月
     */
    @Override
    public Result<Object> savingAnalysis(String spaceId, String yearMonth) throws ParseException {
        Map<String,Object> map = new HashMap<>();
        LambdaQueryWrapper<TpmEquipment> eq = new LambdaQueryWrapper<TpmEquipment>()
                .eq(TpmEquipment::getSpaceid, spaceId)
                .select(TpmEquipment::getId);
        List<TpmEquipment> equipments = equipmentMapper.selectList(eq);
        List<String> equipmentids = new ArrayList<>();
        if (equipments != null && !equipments.isEmpty()) {
            equipmentids = equipments.stream().map(TpmEquipment::getId).collect(Collectors.toList());
        }
        System.out.println(equipmentids);
        // 区域基本信息
        SpaceDto energySpace = spaceMapper.selectSpaceById(spaceId);
        // 当前日期
        Date currentdate = DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-dd", new Date()), "yyyy-MM-dd");

        // 获取年月的最后一天
        String thisdate = DateUtils.getLastDay(yearMonth.substring(0,4), yearMonth.substring(5));
        Integer lastday = Integer.parseInt(thisdate.substring(8));

        // 循环获取相关信息，进行数据分析、节能分析趋势
        List<Integer> monthDate = new ArrayList<>();
        List<Float> jienengData = new ArrayList<>();
        List<Float> chaobiaoData = new ArrayList<>();
        List<Float> jihuaData = new ArrayList<>();
        List<Float> leijiData = new ArrayList<>();

        float shijiAll = 0f;
        float jihuaAll = 0f;
        float leijiAll = 0f;

        Integer jienengAmount = 0;

        for (int i = 1; i <= lastday; i++) {
            monthDate.add(i);
            String day = i < 10 ? yearMonth + "-0" + i : yearMonth + "-" + i;
            List<EmsStatistics> yongnengList = new ArrayList<>();
            // 如果选择的是整个厂区、配电室的，则只需要获取配电室的几个表的信息即可
            if ("AREA0002".equals(energySpace.getSerialnum()) || "AREA0015".equals(energySpace.getSerialnum())) {
                EmsStatistics statisticsParams1 = new EmsStatistics();
                List<String> equip1 = new ArrayList<>();
                equip1.add("301156882513921");
                yongnengList = emsStatisticsMapper.selectEnergyByDay(equip1, day);
            }
            else if (equipmentids != null && equipmentids.size() > 0) {
                // 去掉配电室的几个表
                equipmentids.remove("301156882513921");
                equipmentids.remove("301156930748417");
                equipmentids.remove("301156947525633");
                equipmentids.remove("301156962205697");
                yongnengList = emsStatisticsMapper.selectEnergyByDay(equipmentids, day);
            }
            float shiji = yongnengList.get(0).getHowManyValue();
            float jihua = yongnengList.get(1).getHowManyValue();

            // 实际用能、计划用能、累计节能
            shijiAll += shiji;
            jihuaAll += jihua;
            if (currentdate.after(DateUtils.parseDate(day, "yyyy-MM-dd"))) {
                leijiAll += jihua - shiji;
            }

            if (shiji > jihua) {
                // 实际能耗(超标)
                chaobiaoData.add(shiji);
                jienengData.add(0f);
            }
            else {
                // 实际能耗(节能)
                jienengAmount++;
                chaobiaoData.add(0f);
                jienengData.add(shiji);
            }
//            // 计划用能
            jihuaData.add(jihua);
            // 累计节能
            leijiData.add(leijiAll);
        }

        map.put("monthDate", monthDate);

        map.put("shijiAll", shijiAll);
        map.put("jihuaAll", jihuaAll);
        map.put("leijiAll", leijiAll);

        map.put("jienengData", jienengData);
        map.put("chaobiaoData", chaobiaoData);
        map.put("jihuaData", jihuaData);
        map.put("leijiData", leijiData);

        map.put("anlysis", "节能趋势：与计划能耗相比，有 " + jienengAmount + "个 周期为节能状态，有 " + (lastday - jienengAmount) +
                "个 周期为耗能状态，与计划耗能相差 " + leijiAll + " kWh，整体状态为 " +
                (leijiAll > 0 ? "节能" : "耗能"));

        System.out.println("数据整理完毕！");
        return Result.OK(map);
    }

    /**
     * 能耗排名
     *
     * @param spaceId   区域ID
     * @param beginTime 开始年月
     * @param endTime   结束年月
     * @return 分析统计
     */
    @Override
    public Result<Object> ranking(String spaceId, String beginTime, String endTime) {
        Map<String,Object> map = new HashMap<>();
        LambdaQueryWrapper<TpmEquipment> eq = new LambdaQueryWrapper<TpmEquipment>()
                .eq(TpmEquipment::getSpaceid, spaceId)
                .select(TpmEquipment::getId);
        List<TpmEquipment> equipments = equipmentMapper.selectList(eq);
        List<String> equipmentids = new ArrayList<>();
        if (equipments != null && !equipments.isEmpty()) {
            equipmentids = equipments.stream().map(TpmEquipment::getId).collect(Collectors.toList());
        }
        System.out.println(equipmentids);
        // 区域基本信息
        SpaceDto energySpace = spaceMapper.selectSpaceById(spaceId);

        // 获取今年能耗
        List<EmsStatistics> thisYearList = new ArrayList<>();
        // 如果选择的是整个厂区、配电室的，则只需要获取配电室的几个表的信息即可
        if ("AREA0002".equals(energySpace.getSerialnum())) {
            // 加上配电室的几个表
            equipmentids.remove("301156882513921");
            equipmentids.remove("301156930748417");
            equipmentids.remove("301156947525633");
            equipmentids.remove("301156962205697");
        }
        else if ("AREA0015".equals(energySpace.getSerialnum())) {
            equipmentids = new ArrayList<>();
            // 去掉配电室的几个表
            equipmentids.add("301156882513921");
            equipmentids.add("301156930748417");
            equipmentids.add("301156947525633");
            equipmentids.add("301156962205697");
        }

        if (equipmentids != null && equipmentids.size() > 0) {
            thisYearList = emsStatisticsMapper.selectRankByEquipmentAndYearmonth(equipmentids, beginTime, endTime);
        }

        List<String> equipnames = new ArrayList<>(); // 名称
        List<Float> equipelecs = new ArrayList<>(); // 能耗
        if (thisYearList != null && thisYearList.size() > 0) {
            // 计算今年总能耗
            Float allElec = (float) thisYearList.stream().mapToDouble(EmsStatistics::getHowManyValue).sum();

            for (int i = 0; i < thisYearList.size(); i++) {
                equipnames.add(thisYearList.get(i).getEquipmentname());
                equipelecs.add(thisYearList.get(i).getHowManyValue());

                // 获取去年能耗
                Integer lastYear = Integer.parseInt(beginTime.substring(0, 4)) - 1;
                String lastBeginTime = lastYear + "-" + beginTime.substring(5);
                String lastEndTime = lastYear + "-" + endTime.substring(5);
                Float lastElectricity = emsStatisticsMapper.selectAllElecByEquipmentAndYearmonth(thisYearList.get(i).getEquipmentid(), lastBeginTime, lastEndTime);
                thisYearList.get(i).setHowManyValue2(lastElectricity == null ? 0f : lastElectricity);

                // 计算占比
                float otherValue = (float)Math.round((thisYearList.get(i).getHowManyValue() / allElec) * 10000) / 100f;
                thisYearList.get(i).setOtherValue(otherValue);

                // 计算同比增长率
                float thisElectricity = thisYearList.get(i).getHowManyValue();
                float otherValue2 = 0f;
                if (lastElectricity != null) {
                    otherValue2 = (float) Math.round(((thisElectricity - lastElectricity) / lastElectricity) * 10000) / 100f;
                }
                thisYearList.get(i).setOtherValue2(otherValue2);
            }
        }
        map.put("yuanquName", equipnames);
        map.put("numbers", equipelecs);
        map.put("tableData", thisYearList);

        System.out.println("数据整理完毕");
        return Result.OK(map);
    }

    /**
     * 分项能耗
     *
     * @param spaceId 区域ID
     * @param day     某天
     * @return 分析统计
     */
    @Override
    public Result<Object> itemizedEnergy(String spaceId, String day) throws ParseException {
        Map<String,Object> map = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        // 设置当前的时间戳
        Date thisDay = DateUtils.parseDate(day, "yyyy-MM-dd");
        calendar.setTime(thisDay);
        // 设置星期一为一周开始的第一天
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期所属周
        // *********我草，这个不行，跟mysql获取到的不一致，得用下面一个，但是下面一个跟中国的周好像也不一致
        // int week = calendar.get(Calendar.WEEK_OF_YEAR);
        LocalDate localDate = LocalDate.parse(day);
        int week = localDate.get (IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        // 获得当前日期所属月
        int month = calendar.get(calendar.MONTH) + 1;
        // 获得当前日期所属年
        int year = calendar.get(Calendar.YEAR);

        // 区域基本信息
        SpaceDto energySpace = spaceMapper.selectSpaceById(spaceId);
        // 设备ID
        List<String> equipmentids = new ArrayList<>();
        // 如果选择的是整个厂区、配电室的，则只需要获取总控表的信息即可
        if ("AREA0002".equals(energySpace.getSerialnum()) || "AREA0015".equals(energySpace.getSerialnum())) {
            EmsStatistics statisticsParams1 = new EmsStatistics();
            equipmentids.add("301156882513921");
        }
        else {
            LambdaQueryWrapper<TpmEquipment> eq = new LambdaQueryWrapper<TpmEquipment>()
                    .eq(TpmEquipment::getSpaceid, spaceId)
                    .select(TpmEquipment::getId);
            List<TpmEquipment> equipments = equipmentMapper.selectList(eq);
            if (equipments != null && !equipments.isEmpty()) {
                equipmentids = equipments.stream().map(TpmEquipment::getId).collect(Collectors.toList());
            }
            System.out.println(equipmentids);
            // 去掉配电室的几个表
            equipmentids.remove("301156882513921");
            equipmentids.remove("301156930748417");
            equipmentids.remove("301156947525633");
            equipmentids.remove("301156962205697");
        }

        // 能耗分项
        List<String> itemList = getEnergyItem("电");
        map.put("items", itemList);

        // 当天数据
        String lastDay = DateUtils.parseDateToStr("yyyy-MM-dd", DateUtils.addDays(thisDay, -1)); // 获取昨日
        List<EmsStatistics> dayStatistics = new ArrayList<>();
        List<EmsStatistics> lastDayStatistics = new ArrayList<>();
        if ("AREA0002".equals(energySpace.getSerialnum()) || "AREA0015".equals(energySpace.getSerialnum())) {
            dayStatistics = emsStatisticsMapper.selectItemByEquipidAndDayTotal(equipmentids, day);
            lastDayStatistics = emsStatisticsMapper.selectItemByEquipidAndDayTotal(equipmentids, lastDay);
        }
        else if (equipmentids != null && equipmentids.size() > 0) {
            dayStatistics = emsStatisticsMapper.selectItemByEquipidAndDay(equipmentids, day);
            lastDayStatistics = emsStatisticsMapper.selectItemByEquipidAndDay(equipmentids, lastDay);
        }
        if (dayStatistics != null) {
            double thisDaySum = dayStatistics.stream().mapToDouble(EmsStatistics::getHowManyValue).sum();
            EmsStatistics thisDayStatis = new EmsStatistics();
            thisDayStatis.setType("当日用能");
            thisDayStatis.setHowManyValue((float) thisDaySum);
            dayStatistics.add(thisDayStatis);

            double lastDaySum = lastDayStatistics.stream().mapToDouble(EmsStatistics::getHowManyValue).sum();
            EmsStatistics lastDayStatis = new EmsStatistics();
            lastDayStatis.setType("昨日用能");
            lastDayStatis.setHowManyValue((float) lastDaySum);
            dayStatistics.add(lastDayStatis);

            double chain = 0;
            if (lastDaySum != 0) {
                chain = Math.round(((thisDaySum - lastDaySum) / lastDaySum) * 10000) / 100f;
            }
            EmsStatistics chainStatis = new EmsStatistics();
            chainStatis.setType("环比");
            chainStatis.setHowManyValue((float) chain);
            dayStatistics.add(chainStatis);
        }
        map.put("dayStatistics", dayStatistics);

        // 当周数据
        Date lastWeekDay = DateUtils.addWeeks(thisDay, -1);
        calendar.setTime(lastWeekDay);
        int lastWeekYear = calendar.get(Calendar.YEAR);
        LocalDate localDate1 = LocalDate.parse(DateUtils.parseDateToStr("yyyy-MM-dd",lastWeekDay));
        int lastWeek = localDate1.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

        List<EmsStatistics> weekStatistics = new ArrayList<>();
        List<EmsStatistics> lastWeekStatistics = new ArrayList<>();
        if ("AREA0002".equals(energySpace.getSerialnum()) || "AREA0015".equals(energySpace.getSerialnum())) {
            weekStatistics = emsStatisticsMapper.selectItemByEquipidAndYearweekTotal(equipmentids, year, week);
            lastWeekStatistics = emsStatisticsMapper.selectItemByEquipidAndYearweekTotal(equipmentids, lastWeekYear, lastWeek);
        }
        else if (equipmentids != null && equipmentids.size() > 0) {
            weekStatistics = emsStatisticsMapper.selectItemByEquipidAndYearweek(equipmentids, year, week);
            lastWeekStatistics = emsStatisticsMapper.selectItemByEquipidAndYearweek(equipmentids, lastWeekYear, lastWeek);
        }
        if (weekStatistics != null) {
            double thisWeekSum = weekStatistics.stream().mapToDouble(EmsStatistics::getHowManyValue).sum();
            EmsStatistics thisWeekStatis = new EmsStatistics();
            thisWeekStatis.setType("当周用能");
            thisWeekStatis.setHowManyValue((float) thisWeekSum);
            weekStatistics.add(thisWeekStatis);

            double lastWeekSum = lastWeekStatistics.stream().mapToDouble(EmsStatistics::getHowManyValue).sum();
            EmsStatistics lastWeekStatis = new EmsStatistics();
            lastWeekStatis.setType("上周用能");
            lastWeekStatis.setHowManyValue((float) lastWeekSum);
            weekStatistics.add(lastWeekStatis);

            double chain = 0;
            if (lastWeekSum != 0) {
                chain = Math.round(((thisWeekSum - lastWeekSum) / lastWeekSum) * 10000) / 100f;
            }
            EmsStatistics chainStatis = new EmsStatistics();
            chainStatis.setType("环比");
            chainStatis.setHowManyValue((float) chain);
            weekStatistics.add(chainStatis);
        }
        map.put("weekStatistics", weekStatistics);

        // 当月数据
        Date lastMonthDay = DateUtils.addMonths(thisDay, -1);
        calendar.setTime(lastMonthDay);
        int lastMonth = calendar.get(calendar.MONTH) + 1;
        int lastMonthYear = calendar.get(Calendar.YEAR);
        List<EmsStatistics> monthStatistics = new ArrayList<>();
        List<EmsStatistics> lastMonthStatistics = new ArrayList<>();
        if ("AREA0002".equals(energySpace.getSerialnum()) || "AREA0015".equals(energySpace.getSerialnum())) {
            monthStatistics = emsStatisticsMapper.selectItemByEquipidAndYearmonthTotal(equipmentids, year, month);
            lastMonthStatistics = emsStatisticsMapper.selectItemByEquipidAndYearmonthTotal(equipmentids, lastMonthYear, lastMonth);
        }
        else if (equipmentids != null && equipmentids.size() > 0) {
            monthStatistics = emsStatisticsMapper.selectItemByEquipidAndYearmonth(equipmentids, year, month);
            lastMonthStatistics = emsStatisticsMapper.selectItemByEquipidAndYearmonth(equipmentids, lastMonthYear, lastMonth);
        }
        if (monthStatistics != null) {
            double thisMonthSum = monthStatistics.stream().mapToDouble(EmsStatistics::getHowManyValue).sum();
            EmsStatistics thisMonthStatis = new EmsStatistics();
            thisMonthStatis.setType("当月用能");
            thisMonthStatis.setHowManyValue((float) thisMonthSum);
            monthStatistics.add(thisMonthStatis);

            double lastMonthSum = lastMonthStatistics.stream().mapToDouble(EmsStatistics::getHowManyValue).sum();
            EmsStatistics lastMonthStatis = new EmsStatistics();
            lastMonthStatis.setType("上月用能");
            lastMonthStatis.setHowManyValue((float) lastMonthSum);
            monthStatistics.add(lastMonthStatis);

            double chain = 0;
            if (lastMonthSum != 0) {
                chain = Math.round(((thisMonthSum - lastMonthSum) / lastMonthSum) * 10000) / 100f;
            }
            EmsStatistics chainStatis = new EmsStatistics();
            chainStatis.setType("环比");
            chainStatis.setHowManyValue((float) chain);
            monthStatistics.add(chainStatis);
        }
        map.put("monthStatistics", monthStatistics);

        // 用能趋势
        List<String> hours = DateUtils.getDaySplitHour();
        map.put("hours", hours);

        List<Float> dongli = new ArrayList<>();
        List<Float> zhaoming = new ArrayList<>();
        List<Float> kongtiao = new ArrayList<>();
        List<Float> qita = new ArrayList<>();


        for(int i = 0; i < hours.size(); i++) {
            dongli.add(0f);
            zhaoming.add(0f);
            kongtiao.add(0f);
            qita.add(0f);
            List<EmsStatistics> hourStatis = new ArrayList<>();
            // 如果选择的是整个厂区、配电室的，需要修改算法
            if ("AREA0002".equals(energySpace.getSerialnum()) || "AREA0015".equals(energySpace.getSerialnum())) {
                hourStatis = emsStatisticsMapper.selectItemByEquipidAndDaytimeTotal(equipmentids, day, hours.get(i) + ":00", hours.get(i).substring(0,2) + ":59:59");
            }
            else if (equipmentids != null && equipmentids.size() > 0) {
                hourStatis = emsStatisticsMapper.selectItemByEquipidAndDaytime(equipmentids, day, hours.get(i) + ":00", hours.get(i).substring(0,2) + ":59:59");
            }
            if (hourStatis != null && hourStatis.size() > 0) {
                for (EmsStatistics hoursta : hourStatis) {
                    switch (hoursta.getEquipmentname()) {
                        case "动力用电":
                            dongli.set(i, hoursta.getHowManyValue());
                            break;
                        case "照明用电":
                            zhaoming.set(i, hoursta.getHowManyValue());
                            break;
                        case "空调用电":
                            kongtiao.set(i, hoursta.getHowManyValue());
                            break;
                        case "其它用电":
                            qita.set(i, hoursta.getHowManyValue());
                            break;
                    }
                }
            }
        }
        map.put("dongli", dongli);
        map.put("zhaoming", zhaoming);
        map.put("kongtiao", kongtiao);
        map.put("qita", qita);

        return Result.OK(map);
    }

    /**
     * 用能平衡分析
     *
     * @return 分析统计
     */
    @Override
    public Result<Object> energyBalanceAnalysis() {
        return null;
    }

    /**
     * 峰平谷分析
     *
     * @return 分析统计
     */
    @Override
    public Result<Object> peakValleyAnalysis() {
        return null;
    }

    /**
     * 能耗定额
     *
     * @return 分析统计
     */
    @Override
    public Result<Object> energyQuota() {
        return null;
    }

    /**
     * 能流分析
     *
     * @param spaceId   区域ID
     * @param beginTime 开始日期
     * @param endTime   结束日期
     * @return 分析统计
     */
    @Override
    public Result<Object> energyFlowAnalysis(String spaceId, String beginTime, String endTime) {
        Map<String,Object> map = new HashMap<>();
        LambdaQueryWrapper<TpmEquipment> eq = new LambdaQueryWrapper<TpmEquipment>()
                .eq(TpmEquipment::getSpaceid, spaceId)
                .select(TpmEquipment::getId);
        List<TpmEquipment> equipments = equipmentMapper.selectList(eq);
        List<String> equipmentids = new ArrayList<>();
        if (equipments != null && !equipments.isEmpty()) {
            equipmentids = equipments.stream().map(TpmEquipment::getId).collect(Collectors.toList());
        }
        System.out.println(equipmentids);

        List<FlowSource> sourceData = new ArrayList<>();
        List<FlowLinks> linksData = new ArrayList<>();
        Float elecSum = 0f;

        // 区域基本信息
        SpaceDto energySpace = spaceMapper.selectSpaceById(spaceId);
        // 如果选择的是生产车间、配电室，则执行另外的代码
        if ("AREA0002".equals(energySpace.getSerialnum()) || "AREA0015".equals(energySpace.getSerialnum())) {
            energyFlowAnalysisSub(energySpace, equipmentids, beginTime, endTime, map);

            return Result.OK(map);
        }

        // 能耗分项
        EnergyItemDto energyItemParams = new EnergyItemDto();
        energyItemParams.setEnergytypename("电");
        List<EnergyItemDto> itemList = energyItemMapper.selectEmsEnergyItemList(energyItemParams);
        if (itemList != null && itemList.size() > 0) {
            for (int i = 0; i < itemList.size(); i++) {
                List<EmsStatistics> emsStatisticsList = new ArrayList<>();
                if (equipmentids != null && equipmentids.size() > 0) {
                    emsStatisticsList = emsStatisticsMapper.selectElecByEquipAndItem(equipmentids,
                            itemList.get(i).getId(), beginTime + "-01", DateUtils.getLastDay(endTime.substring(0,4), endTime.substring(5)));
                }
                Float itemSum = 0f;
                if (emsStatisticsList != null) {
                    itemSum = (float) emsStatisticsList.stream().mapToDouble(EmsStatistics::getHowManyValue).sum();
                }
                elecSum += itemSum;
                sourceData.add(new FlowSource(itemList.get(i).getEnergyitemname(), 1, itemSum == null ? 0f : itemSum, "KWh"));
                linksData.add(new FlowLinks("电", itemList.get(i).getEnergyitemname(), itemSum == null ? 0f : itemSum));
                if (emsStatisticsList != null && emsStatisticsList.size() > 0) {
                    for (EmsStatistics sta : emsStatisticsList) {
                        sourceData.add(new FlowSource(sta.getEquipmentname(), 2, sta.getHowManyValue(), "KWh"));
                        linksData.add(new FlowLinks(itemList.get(i).getEnergyitemname(), sta.getEquipmentname(), sta.getHowManyValue()));
                    }

                }
            }
        }
        sourceData.add(0, new FlowSource("电", 0, elecSum == null ? 0f : elecSum, "KWh"));
        linksData.add(0, new FlowLinks("", "电", elecSum == null ? 0f : elecSum));


        map.put("sourceData", sourceData);
        map.put("linksData", linksData);

        return Result.OK(map);
    }

    /**
     * 能流分析-生产车间、配电室
     *
     * @param beginTime 开始日期
     * @param endTime 结束日期
     * @return
     */
    public void energyFlowAnalysisSub(SpaceDto energySpace, List<String> equipmentids, String beginTime, String endTime, Map<String,Object> map)
    {
        List<FlowSource> sourceData = new ArrayList<>();
        List<FlowLinks> linksData = new ArrayList<>();

        // 如果选择的是整个厂区，去掉配电室的几个表
        if ("AREA0002".equals(energySpace.getSerialnum()) ) {
            // 去掉配电室的几个表
            equipmentids.remove("301156882513921");
            equipmentids.remove("301156930748417");
            equipmentids.remove("301156947525633");
            equipmentids.remove("301156962205697");
            energyFlowAnalysisSub1(beginTime, endTime, sourceData, linksData);
        }
        // 如果选择的是配电室，则只查询配电室的几个表
        if ("AREA0015".equals(energySpace.getSerialnum())) {
            energyFlowAnalysisSub1(beginTime, endTime, sourceData, linksData);
            map.put("sourceData", sourceData);
            map.put("linksData", linksData);
            return;
        }

        // 能耗分项
        EnergyItemDto energyItemParams = new EnergyItemDto();
        energyItemParams.setEnergytypename("电");
        List<EnergyItemDto> itemList = energyItemMapper.selectEmsEnergyItemList(energyItemParams);
        if (itemList != null && itemList.size() > 0) {
            for (int i = 0; i < itemList.size(); i++) {
                List<EmsStatistics> emsStatisticsList = new ArrayList<>();
                if (equipmentids != null && equipmentids.size() > 0) {
                    emsStatisticsList = emsStatisticsMapper.selectElecByEquipAndItem(equipmentids,
                            itemList.get(i).getId(), beginTime + "-01", DateUtils.getLastDay(endTime.substring(0,4), endTime.substring(5)));
                }
                if (emsStatisticsList != null && emsStatisticsList.size() > 0) {
                    for (EmsStatistics sta : emsStatisticsList) {
                        sourceData.add(new FlowSource(sta.getEquipmentname(), 2, sta.getHowManyValue(), "KWh"));
                        linksData.add(new FlowLinks(itemList.get(i).getEnergyitemname(), sta.getEquipmentname(), sta.getHowManyValue()));
                    }

                }
            }
        }

        map.put("sourceData", sourceData);
        map.put("linksData", linksData);
    }

    /**
     * 能流分析-生产车间、配电室，组合1,2层
     *
     * @param beginTime 开始日期
     * @param endTime 结束日期
     * @return
     */
    public void energyFlowAnalysisSub1(String beginTime, String endTime, List<FlowSource> sourceData, List<FlowLinks> linksData)
    {
        List<String> equipids = new ArrayList<>();
        // 配电室的四块表
        equipids.add("301156882513921");
        equipids.add("301156930748417");
        equipids.add("301156947525633");
        equipids.add("301156962205697");

        List<EmsStatistics> emsStatList = emsStatisticsMapper.selectElecByEquipAndItem(equipids,null, beginTime + "-01", DateUtils.getLastDay(endTime.substring(0,4), endTime.substring(5)));

        // 第一层-总表
        List<EmsStatistics> zongbiao = emsStatList.stream().filter(p -> p.getEquipmentid() == "301156882513921").collect(Collectors.toList());
        Float zf = zongbiao == null || zongbiao.size() < 1 ? 0f : zongbiao.get(0).getHowManyValue();
        sourceData.add(new FlowSource("电", 0, zf, "KWh"));
        linksData.add(new FlowLinks("", "电", zf));

        // 第二层
        //  动力用电
        List<EmsStatistics> dongli = emsStatList.stream()
                .filter(p -> p.getEquipmentid() == "301156930748417" || p.getEquipmentid() == "301156962205697")
                .collect(Collectors.toList());
        Float df = dongli == null || dongli.size() < 1 ? 0f : (float) dongli.stream().mapToDouble(EmsStatistics::getHowManyValue).sum();;

        //  空调用电
        List<EmsStatistics> kongtiao = emsStatList.stream()
                .filter(p -> p.getEquipmentid() == "301156947525633")
                .collect(Collectors.toList());
        Float kf = kongtiao == null || kongtiao.size() < 1 ? 0f : (float) kongtiao.stream().mapToDouble(EmsStatistics::getHowManyValue).sum();;

        sourceData.add(new FlowSource("动力用电", 1, df, "KWh"));
        linksData.add(new FlowLinks("电", "动力用电", df));

        sourceData.add(new FlowSource("空调用电", 1, kf, "KWh"));
        linksData.add(new FlowLinks("电", "空调用电", kf));

        //  其它用电
        sourceData.add(new FlowSource("其它用电", 1, zf - df - kf, "KWh"));
        linksData.add(new FlowLinks("电", "其它用电", zf - df - kf));
    }

    /**
     * 查询设备详情（按日期查询电量、电量、电压、功率）
     *
     * @param equipmentid 设备ID
     * @param day         日期
     * @param tagtype     类型
     * @return 分析统计
     */
    @Override
    public Result<Object> searchEquipmentDetail(String equipmentid, String day, String tagtype) {
        List rtn = new ArrayList();
        EquipmentStatus equipmentStatus = equipmentStatusMapper.selectStatusByEquipmentid(equipmentid);
        String tagname = "";
        switch (tagtype) {
            case "electricity":
                DataElectricity emsElectricity = new DataElectricity();
                emsElectricity.setEquipmentid(equipmentid);
                emsElectricity.setDay(day);
                List<DataElectricity> elecList = electricityMapper.selectElectricityByDay(emsElectricity);

                // 电量，计算累计值
                Double elecsum = 0D;
                DecimalFormat df = new DecimalFormat("#.00");
                if (elecList != null && elecList.size() > 0) {
                    for (int i = 0; i < elecList.size(); i++) {
                        elecsum += elecList.get(i).getTagvalue() == null ? 0 : elecList.get(i).getTagvalue();
                        elecList.get(i).setTagvalue(Double.parseDouble(df.format(elecsum)));
                    }
                }

                rtn = elecList;
                break;
            case "currentA":
            case "currentB":
            case "currentC":
                if (equipmentStatus == null) {
                    break;
                }
                if ("currentA".equals(tagtype)) {
                    tagname = equipmentStatus.getTagnameca();
                }
                else if ("currentB".equals(tagtype)) {
                    tagname = equipmentStatus.getTagnamecb();
                }
                else {
                    tagname = equipmentStatus.getTagnamecc();
                }
                DataCurrent emsCurrent = new DataCurrent();
                emsCurrent.setEquipmentid(equipmentid);
                emsCurrent.setTagname(tagname);
                emsCurrent.setDay(day);
                rtn = dataCurrentMapper.selectCurrentByDay(emsCurrent);
                break;
            case "voltageA":
            case "voltageB":
            case "voltageC":
                if (equipmentStatus == null) {
                    break;
                }
                if ("voltageA".equals(tagtype)) {
                    tagname = equipmentStatus.getTagnameva();
                }
                else if ("voltageB".equals(tagtype)) {
                    tagname = equipmentStatus.getTagnamevb();
                }
                else {
                    tagname = equipmentStatus.getTagnamevc();
                }
                DataVoltage emsVoltage = new DataVoltage();
                emsVoltage.setEquipmentid(equipmentid);
                emsVoltage.setTagname(tagname);
                emsVoltage.setDay(day);
                rtn = dataVoltageMapper.selectVoltageByDay(emsVoltage);
                break;
            case "power":
                DataPower emsPower = new DataPower();
                emsPower.setEquipmentid(equipmentid);
                emsPower.setDay(day);
                rtn = dataPowerMapper.selectPowerByDay(emsPower);
                break;
            default:
                break;
        }
        return Result.OK(rtn);
    }


    /**
     * 获取分项
     * @param energytypename 能源分类名称
     * @return
     */
    private List<String> getEnergyItem(String energytypename) {
        List<String> rtn = new ArrayList<>();

        EnergyItemDto energyItem = new EnergyItemDto();
        energyItem.setEnergytypename(energytypename);
        List<EnergyItemDto> itemList = energyItemMapper.selectEmsEnergyItemList(energyItem);
        System.out.println(itemList);
        if (itemList != null && itemList.size() > 0) {
            for (int i = 0; i < itemList.size(); i++) {
                rtn.add(itemList.get(i).getEnergyitemname());
            }
        }
        return rtn;
    }

    /**
     * 能流图 - 源点
     */
    public class FlowSource {
        private String name;
        private Integer depth;
        private float nameValue;
        private String valueUnit;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getDepth() {
            return depth;
        }

        public void setDepth(Integer depth) {
            this.depth = depth;
        }

        public float getNameValue() {
            return nameValue;
        }

        public void setNameValue(float nameValue) {
            this.nameValue = nameValue;
        }

        public String getValueUnit() {
            return valueUnit;
        }

        public void setValueUnit(String valueUnit) {
            this.valueUnit = valueUnit;
        }

        public FlowSource() {
        }

        public FlowSource(String name, Integer depth, float nameValue, String valueUnit) {
            this.name = name;
            this.depth = depth;
            this.nameValue = nameValue;
            this.valueUnit = valueUnit;
        }
    }

    /**
     * 能流图 - 连接
     */
    public class FlowLinks {
        private String source;
        private String target;
        private float value;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public FlowLinks() {
        }

        public FlowLinks(String source, String target, float value) {
            this.source = source;
            this.target = target;
            this.value = value;
        }
    }

}
