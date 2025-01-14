package org.jeecg.modules.energyRate.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: base_energy_rate_detail
 * @@author 品讯科技
 * @Date:   2024-01-23
 * @Version: V1.0
 */
@ApiModel(value="base_energy_rate_detail对象", description="能源费率子表")
@Data
@TableName("base_energy_rate_detail")
public class EnergyRateDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**能源费率明细ID*/
	@Excel(name = "能源费率明细ID")
    @ApiModelProperty(value = "能源费率明细ID")
    private String id;
	/**能源费率ID*/
    @ApiModelProperty(value = "能源费率ID")
    private String energyrateid;
	/**分时开始时间*/
	@Excel(name = "分时开始时间", width = 15, format = "HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "HH:mm:ss")
    @DateTimeFormat(pattern="HH:mm:ss")
    @ApiModelProperty(value = "分时开始时间")
    private java.util.Date begintime;
	/**分时结束时间*/
	@Excel(name = "分时结束时间", width = 15, format = "HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "HH:mm:ss")
    @DateTimeFormat(pattern="HH:mm:ss")
    @ApiModelProperty(value = "分时结束时间")
    private java.util.Date endtime;
	/**峰平谷：尖、峰、谷、平*/
	@Excel(name = "峰平谷：尖、峰、谷、平", width = 15)
    @Dict(dicCode = "ems_pandv")
    @ApiModelProperty(value = "峰平谷：尖、峰、谷、平")
    private java.lang.String pandv;
	/**价格*/
	@Excel(name = "价格", width = 15)
    @ApiModelProperty(value = "价格")
    private java.math.BigDecimal price;
	/**起始量*/
	@Excel(name = "起始量", width = 15)
    @ApiModelProperty(value = "起始量")
    private java.lang.Double beginamount;
	/**结束量*/
	@Excel(name = "结束量", width = 15)
    @ApiModelProperty(value = "结束量")
    private java.lang.Double endamount;
}
