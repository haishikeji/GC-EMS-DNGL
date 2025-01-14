package org.jeecg.modules.dataCurrent.entity;

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
 * @Description: 设备实时电流
 * @@author 品讯科技
 * @Date:   2024-01-19
 * @Version: V1.0
 */
@Data
@TableName("ems_data_current")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="设备实时电流对象", description="设备实时电流")
public class DataCurrent implements Serializable {
    private static final long serialVersionUID = 1L;

	/**设备实时电流ID*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "设备实时电流ID")
    private java.lang.Long id;
	/**点位ID*/
	@Excel(name = "点位ID", width = 15)
    @ApiModelProperty(value = "点位ID")
    private java.lang.String tagid;
	/**点位名*/
	@Excel(name = "点位名", width = 15)
    @ApiModelProperty(value = "点位名")
    private java.lang.String tagname;
	/**设备ID*/
	@Excel(name = "设备ID", width = 15)
    @ApiModelProperty(value = "设备ID")
    private java.lang.String equipmentid;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private java.lang.String equipmentname;
	/**时间*/
	@Excel(name = "时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "时间")
    private java.util.Date logtime;
	/**点位值(电流)*/
	@Excel(name = "点位值(电流)", width = 15)
    @ApiModelProperty(value = "点位值(电流)")
    private java.lang.Double tagvalue;
	/**所属年*/
	@Excel(name = "所属年", width = 15)
    @ApiModelProperty(value = "所属年")
    private java.lang.Integer year;
	/**所属月*/
	@Excel(name = "所属月", width = 15)
    @ApiModelProperty(value = "所属月")
    private java.lang.Integer month;
	/**日期*/
	@Excel(name = "日期", width = 15)
    @ApiModelProperty(value = "日期")
    private java.lang.String day;
	/**时间*/
	@Excel(name = "时间", width = 15)
    @ApiModelProperty(value = "时间")
    private java.lang.String time;
	/**所属年月*/
	@Excel(name = "所属年月", width = 15)
    @ApiModelProperty(value = "所属年月")
    private java.lang.String yearmonth;
	/**所属周*/
	@Excel(name = "所属周", width = 15)
    @ApiModelProperty(value = "所属周")
    private java.lang.Integer week;
	/**周几*/
	@Excel(name = "周几", width = 15)
    @ApiModelProperty(value = "周几")
    private java.lang.Integer dayofweek;
}
