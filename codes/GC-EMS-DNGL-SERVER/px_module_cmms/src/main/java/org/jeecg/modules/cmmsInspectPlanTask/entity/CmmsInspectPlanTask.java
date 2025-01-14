package org.jeecg.modules.cmmsInspectPlanTask.entity;

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
 * @Description: 巡检计划-任务
 * @@author 品讯科技
 * @Date:   2024-01-23
 * @Version: V1.0
 */
@Data
@TableName("cmms_inspect_plan_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="cmms_inspect_plan_task对象", description="巡检计划-任务")
public class CmmsInspectPlanTask implements Serializable {
    private static final long serialVersionUID = 1L;

	/**巡检计划任务ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "巡检计划任务ID")
    private java.lang.String id;
	/**巡检计划ID*/
	@Excel(name = "巡检计划ID", width = 15)
    @ApiModelProperty(value = "巡检计划ID")
    private java.lang.String inspectplanid;
	/**任务类型：巡检路线/巡检内容*/
	@Excel(name = "任务类型：巡检路线/巡检内容", width = 15)
    @ApiModelProperty(value = "任务类型：巡检路线/巡检内容")
    private java.lang.String tasktype;
	/**巡检路线ID/巡检点ID*/
	@Excel(name = "巡检路线ID/巡检点ID", width = 15)
    @ApiModelProperty(value = "巡检路线ID/巡检点ID")
    private java.lang.String relid;
	/**是否必须执行：0否，1是*/
	@Excel(name = "是否必须执行：0否，1是", width = 15)
    @ApiModelProperty(value = "是否必须执行：0否，1是")
    private java.lang.Integer ifmustexec;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}
