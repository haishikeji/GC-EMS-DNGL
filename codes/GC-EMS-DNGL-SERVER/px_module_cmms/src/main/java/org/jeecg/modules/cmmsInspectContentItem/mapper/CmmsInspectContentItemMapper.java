package org.jeecg.modules.cmmsInspectContentItem.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cmmsInspectContentItem.entity.CmmsInspectContentItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cmmsInspectContentItem.entity.CmmsInspectContentItemVo;

/**
 * @Description: 巡检内容-巡检项
 * @@author 品讯科技
 * @Date:   2024-01-15
 * @Version: V1.0
 */
public interface CmmsInspectContentItemMapper extends BaseMapper<CmmsInspectContentItem> {

    List<CmmsInspectContentItemVo> getCmmsInspectContentItemListByCountId(@Param("inspectcheckcontid") String inspectcheckcontid);
}
