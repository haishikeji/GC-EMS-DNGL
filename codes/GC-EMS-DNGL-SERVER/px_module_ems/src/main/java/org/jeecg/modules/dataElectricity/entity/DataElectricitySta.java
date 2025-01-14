package org.jeecg.modules.dataElectricity.entity;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * ClassName: EmsElectricitySta
 * Package: org.jeecg.modules.dataElectricity.entity
 * Description:
 *
 * @author 品讯科技
 * @Create 2024/2/22 16:23
 * @Version 1.0
 */
@Data
public class DataElectricitySta implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 设备/回路名称 */
    @Excel(name = "设备名称")
    private String equipmentname;

    /** 设备/回路编号 */
    @Excel(name = "设备编号")
    private String equipmentcode;

    /** 开始示数 */
    @Excel(name = "开始示数")
    private Double beginvalue;

    /** 结束示数 */
    @Excel(name = "结束示数")
    private Double endvalue;

    /** 用电量(kWh) */
    @Excel(name = "用电量(kWh)")
    private Double tagvalue;

    /** 电费(元) */
    @Excel(name = "电费(元)")
    private Double electricityvalue;

}
