package org.jeecg.modules.cmmsSpotcheckContent.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.cmmsSpotcheckContentItem.vo.CmmsSpotcheckContentItemVO;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

@Data
public class CmmsSpotcheckContentQueryVO  implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**点检内容编号*/
    @Excel(name = "点检内容编号", width = 15)
    @ApiModelProperty(value = "点检内容编号")
    private java.lang.String contentcode;
    /**点检内容名称*/
    @Excel(name = "点检内容名称", width = 15)
    @ApiModelProperty(value = "点检内容名称")
    private java.lang.String contentname;
    /**设备id*/
    @Excel(name = "设备id", width = 15, dictTable = "tpm_equipment", dicText = "equipmentname", dicCode = "id")
    @Dict(dictTable = "tpm_equipment", dicText = "equipmentname", dicCode = "id")
    @ApiModelProperty(value = "设备id")
    private java.lang.String equipmentid;
    /**状态：启用：0、禁用:1*/
    @Excel(name = "状态：启用：0、禁用:1", width = 15, dicCode = "spotcheck_content_status")
    @Dict(dicCode = "spotcheck_content_status")
    @ApiModelProperty(value = "状态：启用：0、禁用:1")
    private java.lang.String status;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;

    /**点检内容-点检项集合*/
    private List<CmmsSpotcheckContentItemVO> spotcheckContentItemList ;
}
