package org.jeecg.modules.elecfeeRatio.entity;

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
 * @Description: 力调系数
 * @@author 品讯科技
 * @Date:   2024-01-23
 * @Version: V1.0
 */
@Data
@TableName("base_elecfee_ratio")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="base_elecfee_ratio对象", description="力调系数")
public class ElecfeeRatio implements Serializable {
    private static final long serialVersionUID = 1L;

	/**力调系数ID*/
	@Excel(name = "力调系数ID", width = 15)
    @ApiModelProperty(value = "力调系数ID")
    private String id;
	/**创建者*/
    @ApiModelProperty(value = "创建者")
    @Dict(dictTable ="sys_user",dicText = "realname",dicCode = "username")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新者*/
    @ApiModelProperty(value = "更新者")
    private String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**部门ID*/
	@Excel(name = "部门ID", width = 15)
    @ApiModelProperty(value = "部门ID")
    @Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
    private String deptid;
	/**功率因数*/
	@Excel(name = "功率因数", width = 15)
    @ApiModelProperty(value = "功率因数")
    private Double powerfactor;
	/**考核因数*/
	@Excel(name = "考核因数", width = 15)
    @ApiModelProperty(value = "考核因数")
    private Double assessfactor;
	/**力调系数*/
	@Excel(name = "力调系数", width = 15)
    @ApiModelProperty(value = "力调系数")
    private Double ratio;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private Date begintime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private Date endtime;
}
