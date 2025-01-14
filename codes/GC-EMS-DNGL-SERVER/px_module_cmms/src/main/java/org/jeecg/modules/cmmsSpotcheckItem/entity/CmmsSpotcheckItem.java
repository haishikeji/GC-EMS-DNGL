package org.jeecg.modules.cmmsSpotcheckItem.entity;

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
 * @Description: 点检项
 * @@author 品讯科技
 * @Date:   2024-01-15
 * @Version: V1.0
 */
@Data
@TableName("cmms_spotcheck_item")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="cmms_spotcheck_item对象", description="点检项")
public class CmmsSpotcheckItem implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String itemname;
	/**设备类型ID*/
	@Excel(name = "设备类型ID", width = 15, dictTable = "tpm_equipment_tree", dicText = "name", dicCode = "id")
	@Dict(dictTable = "tpm_equipment_tree", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "设备类型ID")
    private java.lang.String equipmenttreeid;
	/**点检项编号*/
	@Excel(name = "点检项编号", width = 15)
    @ApiModelProperty(value = "点检项编号")
    private java.lang.String itemcode;
	/**标准*/
	@Excel(name = "标准", width = 15)
    @ApiModelProperty(value = "标准")
    private java.lang.String conditions;
	/**点检项分类*/
	@Excel(name = "点检项分类", width = 15, dicCode = "spotcheck_item_type")
	@Dict(dicCode = "spotcheck_item_type")
    @ApiModelProperty(value = "点检项分类")
    private java.lang.String itemtype;
	/**标签*/
	@Excel(name = "标签", width = 15)
    @ApiModelProperty(value = "标签")
    private java.lang.String tag;
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
}
