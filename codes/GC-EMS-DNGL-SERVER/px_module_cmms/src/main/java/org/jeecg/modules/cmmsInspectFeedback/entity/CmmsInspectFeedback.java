package org.jeecg.modules.cmmsInspectFeedback.entity;

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
 * @Description: 巡检工单-信息反馈
 * @@author 品讯科技
 * @Date:   2024-01-24
 * @Version: V1.0
 */
@Data
@TableName("cmms_inspect_feedback")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="cmms_inspect_feedback对象", description="巡检工单-信息反馈")
public class CmmsInspectFeedback implements Serializable {
    private static final long serialVersionUID = 1L;

	/**巡检工单-信息反馈ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "巡检工单-信息反馈ID")
    private java.lang.String id;
	/**创建者*/
    @ApiModelProperty(value = "创建者")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新者*/
    @ApiModelProperty(value = "更新者")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**巡检工单-任务ID*/
	@Excel(name = "巡检工单-任务ID", width = 15)
    @ApiModelProperty(value = "巡检工单-任务ID")
    private java.lang.String inspecttaskid;
	/**巡检点-巡检项ID*/
	@Excel(name = "巡检点-巡检项ID", width = 15)
    @ApiModelProperty(value = "巡检点-巡检项ID")
    private java.lang.String contitemtid;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.String status;
	/**问题标签*/
	@Excel(name = "问题标签", width = 15)
    @ApiModelProperty(value = "问题标签")
    private java.lang.String tag;
	/**文字反馈*/
	@Excel(name = "文字反馈", width = 15)
    @ApiModelProperty(value = "文字反馈")
    private java.lang.String feedback;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}
