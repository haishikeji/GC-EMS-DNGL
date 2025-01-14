package org.jeecg.modules.energyRate.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

/**
 * @Description: base_energy_rate
 * @@author 品讯科技
 * @Date:   2024-01-23
 * @Version: V1.0
 */
@ApiModel(value="base_energy_rate对象", description="能源费率")
@Data
@TableName("base_energy_rate")
public class EnergyRate implements Serializable {
    private static final long serialVersionUID = 1L;

	/**能源费率ID*/
	@Excel(name = "能源费率ID")
    @ApiModelProperty(value = "能源费率ID")
    private String id;
	/**创建者*/
    @ApiModelProperty(value = "创建者")
    @Dict(dictTable ="sys_user",dicText = "realname",dicCode = "username")
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
	@Excel(name = "备注")
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**名称*/
	@Excel(name = "名称")
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
	/**能源分类ID*/
	@Excel(name = "能源分类ID")
    @Dict(dictTable ="base_energy_type",dicText = "name",dicCode = "id")
    @ApiModelProperty(value = "能源分类ID")
    private String energytypeid;
	/**费率类型：分时费率、阶梯费率*/
	@Excel(name = "费率类型：分时费率、阶梯费率", width = 15)
    @Dict(dicCode = "energy_rate_type")
    @ApiModelProperty(value = "费率类型：分时费率、阶梯费率")
    private java.lang.String ratetype;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private java.lang.String rateunit;
	/**费率有效期开始*/
	@Excel(name = "费率有效期开始", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "费率有效期开始")
    private java.util.Date begintime;
	/**费率有效期结束*/
	@Excel(name = "费率有效期结束", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "费率有效期结束")
    private java.util.Date endtime;
}
