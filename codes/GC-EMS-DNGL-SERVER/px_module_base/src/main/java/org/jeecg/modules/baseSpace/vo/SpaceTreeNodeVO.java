package org.jeecg.modules.baseSpace.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.List;

@Data
public class SpaceTreeNodeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**上级id*/
    @Excel(name = "上级id", width = 15)
    @ApiModelProperty(value = "上级id")
    private java.lang.String parentid;
    /**名称*/
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**是否有子节点*/
    @Excel(name = "是否有子节点", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否有子节点")
    private java.lang.String hasChild;

    /**
     * 子集
     */
    List<SpaceTreeNodeVO> children;
}
