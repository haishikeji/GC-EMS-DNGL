package org.jeecg.modules.cmmsInspect.entity;

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
 * @Description: 巡检工单
 * @@author 品讯科技
 * @Date:   2024-01-24
 * @Version: V1.0
 */
@Data
@TableName("cmms_inspect")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="cmms_inspect对象", description="巡检工单")
public class CmmsInspect implements Serializable {
    private static final long serialVersionUID = 1L;

	/**巡检工单ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "巡检工单ID")
    private java.lang.String id;
	/**创建者*/
    @ApiModelProperty(value = "创建者")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新者*/
    @ApiModelProperty(value = "更新者")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**工单编号*/
	@Excel(name = "工单编号", width = 15)
    @ApiModelProperty(value = "工单编号")
    private java.lang.String inspectcode;
	/**工单名称*/
	@Excel(name = "工单名称", width = 15)
    @ApiModelProperty(value = "工单名称")
    private java.lang.String inspectname;
	/**任务类型：单次任务、周期任务*/
	@Excel(name = "任务类型：单次任务、周期任务", width = 15)
    @ApiModelProperty(value = "任务类型：单次任务、周期任务")
    private java.lang.String tasktype;
	/**状态：0待处理、1处理中、2已完成*/
	@Excel(name = "状态：0待处理、1处理中、2已完成", width = 15)
    @ApiModelProperty(value = "状态：0待处理、1处理中、2已完成")
    @Dict(dicCode = "inspect_order_status")
    private java.lang.String status;
	/**巡检细项*/
	@Excel(name = "巡检细项", width = 15)
    @ApiModelProperty(value = "巡检细项")
    private java.lang.String inspectdetail;
	/**巡检计划ID*/
	@Excel(name = "巡检计划ID", width = 15)
    @ApiModelProperty(value = "巡检计划ID")
    private java.lang.String inspectplanid;
	/**待办人*/
	@Excel(name = "待办人", width = 15)
    @ApiModelProperty(value = "待办人")
    private java.lang.String chargeruser;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}
