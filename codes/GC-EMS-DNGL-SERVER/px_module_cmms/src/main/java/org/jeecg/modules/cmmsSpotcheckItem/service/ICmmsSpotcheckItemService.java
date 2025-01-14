package org.jeecg.modules.cmmsSpotcheckItem.service;

import org.jeecg.modules.cmmsSpotcheckItem.entity.CmmsSpotcheckItem;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Description: 点检项
 * @@author 品讯科技
 * @Date:   2024-01-15
 * @Version: V1.0
 */
public interface ICmmsSpotcheckItemService extends IService<CmmsSpotcheckItem> {


    /**
     * 查询点检项列表
     *
     * @param equipmentid 设备ID
     * @return 点检项集合
     */
    public List<CmmsSpotcheckItem> selectCmmsSpotcheckItemByEquipmentid(String equipmentid);

}
