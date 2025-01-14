package org.jeecg.modules.cmmsSpotcheckItem.service.impl;

import org.jeecg.modules.cmmsSpotcheckItem.entity.CmmsSpotcheckItem;
import org.jeecg.modules.cmmsSpotcheckItem.mapper.CmmsSpotcheckItemMapper;
import org.jeecg.modules.cmmsSpotcheckItem.service.ICmmsSpotcheckItemService;
import org.jeecg.modules.tpmEquipment.entity.TpmEquipment;
import org.jeecg.modules.tpmEquipment.mapper.TpmEquipmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 点检项
 * @@author 品讯科技
 * @Date:   2024-01-15
 * @Version: V1.0
 */
@Service
public class CmmsSpotcheckItemServiceImpl extends ServiceImpl<CmmsSpotcheckItemMapper, CmmsSpotcheckItem> implements ICmmsSpotcheckItemService {

    @Autowired
    private TpmEquipmentMapper tpmEquipmentMapper;
    @Autowired
    private CmmsSpotcheckItemMapper spotcheckItemMapper;

    /**
     * 查询点检项列表
     *
     * @param equipmentid 设备ID
     * @return 点检项集合
     */
    public List<CmmsSpotcheckItem> selectCmmsSpotcheckItemByEquipmentid(String equipmentid) {
        // 获取设备信息
        TpmEquipment tpmEquipment = tpmEquipmentMapper.selectById(equipmentid);

        if(tpmEquipment!=null){
            CmmsSpotcheckItem cmmsSpotcheckItem = new CmmsSpotcheckItem();
            cmmsSpotcheckItem.setEquipmenttreeid(tpmEquipment.getEquipmenttreeid());
            return spotcheckItemMapper.selectCmmsSpotcheckItemList(cmmsSpotcheckItem);
        }else return new ArrayList<>();

    }

}
