package org.jeecg.modules.cmmsSpotcheckItem.convert;

import org.jeecg.modules.cmmsSpotcheckItem.entity.CmmsSpotcheckItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,imports ={ CollectionUtils.class, Collectors.class})
public interface CmmsSpotcheckItemConvert {

    CmmsSpotcheckItemConvert INSTANCE = Mappers.getMapper(CmmsSpotcheckItemConvert.class);


    @Mapping(target ="equipmenttreeid",source ="equipmenttreeid")
    @Mapping(target ="itemcode",source ="itemcode")
    CmmsSpotcheckItem convert( CmmsSpotcheckItem cmmsSpotcheckItem,String itemcode,String equipmenttreeid);
}
