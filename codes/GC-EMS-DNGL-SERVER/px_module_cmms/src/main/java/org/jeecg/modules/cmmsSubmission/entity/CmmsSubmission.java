package org.jeecg.modules.cmmsSubmission.entity;

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
 * @Description: 送检工单表
 * @@author 品讯科技
 * @Date:   2024-01-29
 * @Version: V1.0
 */
@Data
@TableName("cmms_submission")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="cmms_submission对象", description="送检工单表")
public class CmmsSubmission implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**送检工单编号*/
    @Excel(name = "送检工单编号", width = 15)
    @ApiModelProperty(value = "送检工单编号")
    private java.lang.String sjcode;
	/**送检负责人*/
	@Excel(name = "送检负责人", width = 15)
    @ApiModelProperty(value = "送检负责人")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private java.lang.String verificatePerson;
	/**检定方式*/
	@Excel(name = "检定方式", width = 15, dicCode = "verificate_method")
	@Dict(dicCode = "verificate_method")
    @ApiModelProperty(value = "检定方式")
    private java.lang.String verificateMethod;
	/**检定单位id*/
	@Excel(name = "检定单位id", width = 15, dictTable = "base_client", dicText = "client_name", dicCode = "id")
	@Dict(dictTable = "base_client", dicText = "client_name", dicCode = "id")
    @ApiModelProperty(value = "检定单位id")
    private java.lang.String verificateClientId;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
    /**送检工单状态（0未开始1进行中1已完成）*/
    @Excel(name = "送检工单状态", width = 15, dicCode = "submission_gdstatus")
    @Dict(dicCode = "submission_gdstatus")
    @ApiModelProperty(value = "送检工单状态（0未开始1进行中1已完成）")
    private java.lang.String submissionGdstatus;
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
}
