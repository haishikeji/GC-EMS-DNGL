package org.jeecg.modules.equipmentOnoffSection.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.equipmentOnoffSection.entity.EquipmentOnoffSection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.entity.SysUser;

/**
 * @Description: tpm_equipment_onoff_section
 * @@author 品讯科技
 * @Date:   2024-02-16
 * @Version: V1.0
 */
public interface EquipmentOnoffSectionMapper extends BaseMapper<EquipmentOnoffSection> {

    /**
     * 获取设备运行时间段信息(状态横向)
     * @param page
     * @param equipmentid 设备id
     * @param begintime 开始日期
     * @param endtime 结束日期
     * @return
     */
    public IPage<EquipmentOnoffSection> selectOnoffTransverseList(Page<EquipmentOnoffSection> page, @Param("equipmentid") String equipmentid,
                                         @Param("begintime") String begintime, @Param("endtime") String endtime);

    /**
     * 获取设备运行时间段信息(状态横向)
     * @param begintime 开始日期
     * @param endtime 结束日期
     * @return
     */
    public List<EquipmentOnoffSection> selectOnoffTransverse(String begintime, String endtime);

}
