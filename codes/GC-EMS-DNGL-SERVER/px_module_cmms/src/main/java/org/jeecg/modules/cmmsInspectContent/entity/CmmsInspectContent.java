package org.jeecg.modules.cmmsInspectContent.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 巡检内容
 * @@author 品讯科技
 * @Date:   2024-01-15
 * @Version: V1.0
 */
@Data
@TableName("cmms_inspect_content")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="cmms_inspect_content对象", description="巡检内容")
public class CmmsInspectContent implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**巡检内容编号*/
	@Excel(name = "巡检内容编号", width = 15)
    @ApiModelProperty(value = "巡检内容编号")
    private java.lang.String contentcode;
	/**巡检内容名称*/
	@Excel(name = "巡检内容名称", width = 15)
    @ApiModelProperty(value = "巡检内容名称")
    private java.lang.String contentname;
	/**设备id*/
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
    @Dict(dictTable = "tpm_equipment", dicText = "equipmentname", dicCode = "id")
    private java.lang.String equipmentid;
	/**状态：启用：0、禁用:1*/
	@Excel(name = "状态：启用：0、禁用:1", width = 15, dicCode = "common_status")
    @ApiModelProperty(value = "状态：启用：0、禁用:1")
    @Dict(dicCode = "common_status")
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
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
    /** 分类 */
    @Excel(name = "分类", width = 15)
    @ApiModelProperty(value = "分类")
    @Dict(dicCode = "inspect_item_type")
    private java.lang.String classification;
}
