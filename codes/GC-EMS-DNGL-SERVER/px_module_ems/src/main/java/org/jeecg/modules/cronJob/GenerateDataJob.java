package org.jeecg.modules.cronJob;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.dataCurrent.entity.DataCurrent;
import org.jeecg.modules.dataCurrent.service.IDataCurrentService;
import org.jeecg.modules.dataDemand.entity.DataDemand;
import org.jeecg.modules.dataDemand.service.IDataDemandService;
import org.jeecg.modules.dataElectricity.entity.DataElectricity;
import org.jeecg.modules.dataElectricity.service.IDataElectricityService;
import org.jeecg.modules.dataVoltage.entity.DataVoltage;
import org.jeecg.modules.dataVoltage.service.IDataVoltageService;
import org.jeecg.modules.tpmEquipment.entity.TpmEquipment;
import org.jeecg.modules.tpmEquipment.service.ITpmEquipmentService;
import org.jeecg.modules.tpmTag.entity.TpmTag;
import org.jeecg.modules.tpmTag.service.ITpmTagService;
import org.quartz.JobExecutionException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class GenerateDataJob  {

    @Autowired
    private ITpmEquipmentService equipmentService; //设备service
    @Autowired
    private ITpmTagService tagService; //点位service
    @Autowired
    private IDataCurrentService currentService; //实时电流service
    @Autowired
    private IDataDemandService demandService; //实时需量service
    @Autowired
    private IDataElectricityService electricityService; // 分时用电量service
    @Autowired
    private IDataVoltageService voltageService; //设备实时电压service

//    @Scheduled(cron = "0 * * * * ?")
    public void execute() throws JobExecutionException {
        // 电流值范围：20-50、电压值范围：350-450、需量：400-1500、用电量、0-1.5
        // tagtype:4需量、5电流、6电压、7电量

        ArrayList<DataDemand> dataDemands = new ArrayList<>();
        ArrayList<DataCurrent> dataCurrents = new ArrayList<>();
        ArrayList<DataVoltage> dataVoltages = new ArrayList<>();
        ArrayList<DataElectricity> dataElectricities = new ArrayList<>();

        List<TpmEquipment> equipments = equipmentService.list();
        for (TpmEquipment equipment : equipments) {
            LambdaQueryWrapper<TpmTag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            List<TpmTag> list = tagService.list(tagLambdaQueryWrapper.eq(TpmTag::getEquipmentid, equipment.getId()));
            if (list == null || list.isEmpty()) continue;
            for (TpmTag tpmTag : list) {
                if (StringUtils.isEmpty(tpmTag.getTagtype())) tpmTag.setTagtype("nil");
                String tagType = tpmTag.getTagtype();
                DataDemand dataDemand = new DataDemand();
                dataDemand.setTagid(tpmTag.getId());
                dataDemand.setTagname(tpmTag.getTagname());
                dataDemand.setEquipmentid(equipment.getId());
                dataDemand.setEquipmentname(equipment.getEquipmentname());
                dataDemand.setLogtime(new Date());
                if (tagType.equals("4")) {
                    dataDemand.setTagvalue(randomData(4));
                    dataDemands.add(dataDemand);
                } else if (tagType.equals("5")) {
                    DataCurrent dataCurrent = new DataCurrent();
                    BeanUtils.copyProperties(dataDemand, dataCurrent);
                    dataCurrent.setTagvalue(randomData(5));
                    dataCurrents.add(dataCurrent);
                } else if (tagType.equals("6")) {
                    DataVoltage dataVoltage = new DataVoltage();
                    BeanUtils.copyProperties(dataDemand, dataVoltage);
                    dataVoltage.setTagvalue(randomData(6));
                    dataVoltages.add(dataVoltage);
                } else if (tagType.equals("7")) {
                    DataElectricity dataElectricity = new DataElectricity();
                    BeanUtils.copyProperties(dataDemand, dataElectricity);
                    Double value = randomData(7);
                    dataElectricity.setTagvalue2(value);
/*
                    LambdaQueryWrapper<DataElectricity> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(DataElectricity::getEquipmentid, equipment.getId());
                    wrapper.eq(DataElectricity::getTagid, tpmTag.getId());
                    Page<DataElectricity> page = electricityService.page(new Page<>(1, 1), wrapper);
                    DataElectricity one = page.getRecords().get(0);
                    if (one != null && ObjectUtils.isNotEmpty(one.getTagvalue())) {
                        dataElectricity.setTagvalue(one.getTagvalue() + value);
                    } else {
                        dataElectricity.setTagvalue(value);
                    }*/
                    dataElectricities.add(dataElectricity);
                }
            }
        }

        demandService.saveBatch(dataDemands);
        currentService.saveBatch(dataCurrents);
        voltageService.saveBatch(dataVoltages);
        electricityService.saveBatch(dataElectricities);

    }

    public Double GenerateData(int tagType) {
        switch (tagType) {
            case 4:
                return 400 + Math.random() * (1500 - 400);
            case 5:
                return 20 + Math.random() * (50 - 20);
            case 6:
                return 350 + Math.random() * (450 - 350);
            case 7:
                return Math.random() * 1.5;
        }
        return null;
    }

    public Double randomData(int tagType) {
        Double aDouble = GenerateData(tagType);
        BigDecimal bd = new BigDecimal(aDouble).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
