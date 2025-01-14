package org.jeecg.modules.dataPower.service.impl;

import org.jeecg.modules.dataPower.entity.DataPower;
import org.jeecg.modules.dataPower.mapper.DataPowerMapper;
import org.jeecg.modules.dataPower.service.IDataPowerService;
import org.jeecg.modules.dataPower.vo.DataPowerVO;
import org.jeecg.modules.paramquery.dto.ParamQueryDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: ems_data_power
 * @@author 品讯科技
 * @Date:   2024-01-19
 * @Version: V1.0
 */
@Service
public class DataPowerServiceImpl extends ServiceImpl<DataPowerMapper, DataPower> implements IDataPowerService {

    @Autowired
    private DataPowerMapper dataPowerMapper;

    /**
     * 监测参数查询——功率
     */
    public List<DataPowerVO> dataList(ParamQueryDataDTO dto){
        return dataPowerMapper.dataList(dto);
    }

}
