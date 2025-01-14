package org.jeecg.modules.dataDemand.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class DataDemandVO {

    /**正向有功需量*/
    @Excel(name = "正向有功需量", width = 15)
    @ApiModelProperty(value = "正向有功需量")
    private java.lang.Double tagvalue;
    /**时间*/
    @Excel(name = "时间", width = 15, format = "MM-dd HH:mm")
    @JsonFormat(timezone = "GMT+8",pattern = "MM-dd HH:mm")
    @DateTimeFormat(pattern="MM-dd HH:mm")
    @ApiModelProperty(value = "时间")
    private java.util.Date logtime;

}
