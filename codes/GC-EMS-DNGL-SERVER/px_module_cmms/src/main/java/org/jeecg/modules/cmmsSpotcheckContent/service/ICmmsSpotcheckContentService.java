package org.jeecg.modules.cmmsSpotcheckContent.service;

import org.jeecg.modules.cmmsSpotcheckContent.entity.CmmsSpotcheckContent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 点检内容
 * @@author 品讯科技
 * @Date:   2024-01-15
 * @Version: V1.0
 */
public interface ICmmsSpotcheckContentService extends IService<CmmsSpotcheckContent> {

    /**
     * 根据设备id修改该设备所有点检内容状态
     * 启用：0、禁用:1
     * */
    public void updateStatusByEquipmentid(String equipmentid, String status);

    /**
     * 根据设备id查询该设备现在点检内容已启用数量
     * */
    public int numByEquipmentid(String equipmentid);


}
