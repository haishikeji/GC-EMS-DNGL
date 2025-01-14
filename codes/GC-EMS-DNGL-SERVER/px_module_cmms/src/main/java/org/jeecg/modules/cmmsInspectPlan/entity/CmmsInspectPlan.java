package org.jeecg.modules.cmmsInspectPlan.entity;

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
 * @Description: 巡检计划
 * @@author 品讯科技
 * @Date:   2024-01-23
 * @Version: V1.0
 */
@Data
@TableName("cmms_inspect_plan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="cmms_inspect_plan对象", description="巡检计划")
public class CmmsInspectPlan implements Serializable {
    private static final long serialVersionUID = 1L;

	/**巡检计划ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "巡检计划ID")
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
	/**巡检计划编号*/
	@Excel(name = "巡检计划编号", width = 15)
    @ApiModelProperty(value = "巡检计划编号")
    private java.lang.String plancode;
	/**巡检计划名称*/
	@Excel(name = "巡检计划名称", width = 15)
    @ApiModelProperty(value = "巡检计划名称")
    private java.lang.String planname;
	/**是否开启时间设置：Y是、N否*/
	@Excel(name = "是否开启时间设置：Y是、N否", width = 15)
    @ApiModelProperty(value = "是否开启时间设置：Y是、N否")
    private java.lang.String iftimeset;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private java.util.Date begintime;
	/**重复*/
	@Excel(name = "重复", width = 15)
    @ApiModelProperty(value = "重复")
    private java.lang.Integer repeatnum;
	/**重复类型：年、月、周、日、时、分*/
	@Excel(name = "重复类型：年、月、周、日、时、分", width = 15)
    @ApiModelProperty(value = "重复类型：年、月、周、日、时、分")
    private java.lang.String repeattype;
	/**提前通知时间*/
	@Excel(name = "提前通知时间", width = 15)
    @ApiModelProperty(value = "提前通知时间")
    private java.lang.Integer noticenum;
	/**提前通知类型：天、时、分*/
	@Excel(name = "提前通知类型：天、时、分", width = 15)
    @ApiModelProperty(value = "提前通知类型：天、时、分")
    private java.lang.String noticetype;
	/**状态（0启用 1停用）*/
	@Excel(name = "状态（0启用 1停用）", width = 15,dicCode = "common_status")
    @ApiModelProperty(value = "状态（0启用 1停用）")
    @Dict(dicCode = "common_status")
    private java.lang.String status;
	/**待办人*/
	@Excel(name = "待办人", width = 15)
    @ApiModelProperty(value = "待办人")
    private java.lang.String chargeruser;
	/**下次执行时间*/
	@Excel(name = "下次执行时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "下次执行时间")
    private java.util.Date nexttime;
	/**任务数量*/
	@Excel(name = "任务数量", width = 15)
    @ApiModelProperty(value = "任务数量")
    private java.lang.Integer tasknum;
	/**下次通知时间*/
	@Excel(name = "下次通知时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "下次通知时间")
    private java.util.Date noticetime;
	/**是否已通知：是/否*/
	@Excel(name = "是否已通知：是/否", width = 15)
    @ApiModelProperty(value = "是否已通知：是/否")
    private java.lang.String ifnotice;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}
