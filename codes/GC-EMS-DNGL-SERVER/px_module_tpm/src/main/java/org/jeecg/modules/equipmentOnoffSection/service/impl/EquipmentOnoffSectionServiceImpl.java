package org.jeecg.modules.equipmentOnoffSection.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.equipmentOnoffSection.entity.EquipmentOnoffSection;
import org.jeecg.modules.equipmentOnoffSection.mapper.EquipmentOnoffSectionMapper;
import org.jeecg.modules.equipmentOnoffSection.service.IEquipmentOnoffSectionService;
import org.jeecg.modules.system.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: tpm_equipment_onoff_section
 * @@author 品讯科技
 * @Date:   2024-02-16
 * @Version: V1.0
 */
@Service
public class EquipmentOnoffSectionServiceImpl extends ServiceImpl<EquipmentOnoffSectionMapper, EquipmentOnoffSection> implements IEquipmentOnoffSectionService {

    @Autowired
    private EquipmentOnoffSectionMapper onoffSectionMapper;

    /**
     * 获取设备运行时间段信息(状态横向)
     * @param page
     * @param equipmentid 设备id
     * @param begintime 开始日期
     * @param endtime 结束日期
     * @return
     */
    @Override
    public IPage<EquipmentOnoffSection> selectOnoffTransverseList(Page<EquipmentOnoffSection> page, String equipmentid, String begintime, String endtime) {
        return onoffSectionMapper.selectOnoffTransverseList(page, equipmentid, begintime, endtime);
    }

    /**
     * 获取设备运行时间段信息(状态横向)
     * @param begintime 开始日期
     * @param endtime 结束日期
     * @return
     */
    @Override
    public List<EquipmentOnoffSection> selectOnoffTransverse(String begintime, String endtime) {
        return onoffSectionMapper.selectOnoffTransverse(begintime, endtime);
    }

}
