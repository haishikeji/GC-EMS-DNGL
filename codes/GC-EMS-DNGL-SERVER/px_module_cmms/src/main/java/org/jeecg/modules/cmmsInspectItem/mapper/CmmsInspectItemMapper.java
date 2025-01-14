package org.jeecg.modules.cmmsInspectItem.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cmmsInspectItem.entity.CmmsInspectItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cmmsInspectItem.entity.CmmsInspectItemVo;

/**
 * @Description: 巡检项
 * @@author 品讯科技
 * @Date:   2024-01-11
 * @Version: V1.0
 */
public interface CmmsInspectItemMapper extends BaseMapper<CmmsInspectItem> {

    List<CmmsInspectItemVo> getCmmsInspectItems(Page<CmmsInspectItemVo> page, @Param("cmmsInspectItem") CmmsInspectItem cmmsInspectItem);

    List<CmmsInspectItem> getCmmsInspectItemsByEqid(@Param("eqid") String eqid);
}
