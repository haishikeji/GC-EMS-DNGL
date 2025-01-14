package org.jeecg.modules.dataVoltage.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class DataVoltageVO {

    /**A相电压点位值*/
    @Excel(name = "A相电压点位值", width = 15)
    @ApiModelProperty(value = "A相电压点位值")
    private java.lang.Double atagvalue;
    /**B相电压点位值*/
    @Excel(name = "B相电压点位值", width = 15)
    @ApiModelProperty(value = "B相电压点位值")
    private java.lang.Double btagvalue;
    /**C相电压点位值*/
    @Excel(name = "C相电压点位值", width = 15)
    @ApiModelProperty(value = "C相电压点位值")
    private java.lang.Double ctagvalue;
    /**时间*/
    @Excel(name = "时间", width = 15, format = "MM-dd HH:mm")
    @JsonFormat(timezone = "GMT+8",pattern = "MM-dd HH:mm")
    @DateTimeFormat(pattern="MM-dd HH:mm")
    @ApiModelProperty(value = "时间")
    private java.util.Date logtime;

}
