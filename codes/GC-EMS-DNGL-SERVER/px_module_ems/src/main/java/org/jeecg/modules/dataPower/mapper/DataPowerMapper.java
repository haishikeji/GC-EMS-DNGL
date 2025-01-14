package org.jeecg.modules.dataPower.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.dataPower.entity.DataPower;
import org.jeecg.modules.dataPower.vo.DataPowerVO;
import org.jeecg.modules.paramquery.dto.ParamQueryDataDTO;

import java.util.List;

/**
 * @Description: ems_data_power
 * @@author 品讯科技
 * @Date:   2024-01-19
 * @Version: V1.0
 */
@Mapper
public interface DataPowerMapper extends BaseMapper<DataPower> {

    /**
     * 监测参数查询——功率
     */
    List<DataPowerVO> dataList(ParamQueryDataDTO dto);

    /**
     * 按日期查询功率
     *
     * @param power 功率
     * @return 功率集合
     */
    public List<DataPower> selectPowerByDay(DataPower power);
}
