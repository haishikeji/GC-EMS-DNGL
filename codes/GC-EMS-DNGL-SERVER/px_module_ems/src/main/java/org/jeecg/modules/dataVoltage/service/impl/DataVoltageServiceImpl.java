package org.jeecg.modules.dataVoltage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.dataVoltage.entity.DataVoltage;
import org.jeecg.modules.dataVoltage.mapper.DataVoltageMapper;
import org.jeecg.modules.dataVoltage.service.IDataVoltageService;
import org.jeecg.modules.dataVoltage.vo.DataVoltageVO;
import org.jeecg.modules.paramquery.dto.ParamQueryDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: ems_data_voltage
 * @@author 品讯科技
 * @Date:   2024-01-19
 * @Version: V1.0
 */
@Service
public class DataVoltageServiceImpl extends ServiceImpl<DataVoltageMapper, DataVoltage> implements IDataVoltageService {

    @Autowired
    private DataVoltageMapper dataVoltageMapper;

    /**
     * 监测参数查询——电压
     */
    public List<DataVoltageVO> dataList(ParamQueryDataDTO dto){
        return dataVoltageMapper.dataList(dto);
    }

}
