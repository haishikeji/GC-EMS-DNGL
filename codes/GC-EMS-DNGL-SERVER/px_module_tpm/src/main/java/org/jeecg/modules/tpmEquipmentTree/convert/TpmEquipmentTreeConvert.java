package org.jeecg.modules.tpmEquipmentTree.convert;

import org.jeecg.modules.tpmEquipmentTree.entity.TpmEquipmentTree;
import org.jeecg.modules.tpmEquipmentTree.vo.TpmEquipmentTreeNodeVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,imports ={ CollectionUtils.class, Collectors.class})
public interface TpmEquipmentTreeConvert {

    TpmEquipmentTreeConvert INSTANCE = Mappers.getMapper(TpmEquipmentTreeConvert.class);

    List<TpmEquipmentTreeNodeVO> convert(List<TpmEquipmentTree> equipmentTreeList);

}
