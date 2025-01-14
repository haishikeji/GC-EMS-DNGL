package org.jeecg.modules.Statistics.vo;


import lombok.Data;

@Data
public class MachineSum {

    /** 运行中设备数量 */
    private Long runSum;
    /** 设备总数 */
    private Long allSum;

}
