package org.jeecg.modules.baseSpace.convert;

import org.jeecg.modules.baseSpace.entity.Space;
import org.jeecg.modules.baseSpace.vo.SpaceTreeNodeVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,imports ={ CollectionUtils.class, Collectors.class})
public interface SpaceConvert {

    SpaceConvert INSTANCE = Mappers.getMapper(SpaceConvert.class);

    List<SpaceTreeNodeVO> convert(List<Space> spaceList);

}
