package org.jeecg.modules.energyItem.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.energyItem.entity.EnergyItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.energyItem.entity.EnergyItemDto;

/**
 * @Description: 能耗分项
 * @@author 品讯科技
 * @Date:   2024-01-23
 * @Version: V1.0
 */
@Mapper
public interface EnergyItemMapper extends BaseMapper<EnergyItem> {

    /**
     * 查询能耗分项列表
     *
     * @param energyItem 能耗分项
     * @return 能耗分项集合
     */
    public List<EnergyItemDto> selectEmsEnergyItemList(@Param("energyItem") EnergyItemDto energyItem);

}
