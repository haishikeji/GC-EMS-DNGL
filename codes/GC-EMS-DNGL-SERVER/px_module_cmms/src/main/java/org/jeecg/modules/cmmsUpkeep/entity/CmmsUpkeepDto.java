package org.jeecg.modules.cmmsUpkeep.entity;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.cmmsOrderNode.entity.CmmsOrderNode;
import org.jeecg.modules.cmmsUpkeepItem.entity.CmmsUpkeepItem;
import org.jeecg.modules.cmmsUpkeepItem.entity.CmmsUpkeepItemDto;
import org.jeecg.modules.sysUploadFile.entity.SysUploadFile;

import java.util.List;

/**
 * ClassName: CmmsUpkeepDto
 * Package: org.jeecg.modules.cmmsUpkeep.entity
 * Description:
 *
 * @author 品讯科技
 * @Create 2024/1/24 11:39
 * @Version 1.0
 */
@Data
public class CmmsUpkeepDto extends CmmsUpkeep{

    /**
     * 节点信息
     */
    private List<CmmsOrderNode> nodeList;

    /**
     * 图片
     */
    private List<SysUploadFile> files;

    /**
     * 设备id
     */
    @Dict(dicCode = "id", dictTable = "tpm_equipment", dicText = "equipmentname")
    private String equipmentid;

    /**
     * 保养项信息
     */
    private List<CmmsUpkeepItemDto> cmmsUpkeepItemList;
}
