package org.jeecg.modules.cmmsInspectTcontentItem.convert;

import org.jeecg.modules.cmmsInspectSpot.entity.CmmsInspectSpot;
import org.jeecg.modules.cmmsInspectSpot.entity.CmmsInspectSpotVo;
import org.jeecg.modules.cmmsInspectTcontentItem.entity.CmmsInspectTcontentItem;
import org.jeecg.modules.cmmsInspectTcontentItem.entity.CmmsInspectTcontentItemVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

/**
 * @author 品讯科技
 * @date 2024-08
 * @package org.jeecg.modules.cmmsInspectPlan.convert
 * @project EMS_server
 * @des
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,imports ={ CollectionUtils.class, Collectors.class})
public interface CmmsInspectTcontentItemConvert {

    CmmsInspectTcontentItemConvert INSTANCE = Mappers.getMapper(CmmsInspectTcontentItemConvert.class);

    CmmsInspectTcontentItemVo convert(CmmsInspectTcontentItem tcontentItem);
}
