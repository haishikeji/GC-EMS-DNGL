package org.jeecg.modules.dataDemand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.dataDemand.entity.DataDemand;
import org.jeecg.modules.dataDemand.vo.DataDemandVO;
import org.jeecg.modules.paramquery.dto.ParamQueryDataDTO;

import java.util.List;

/**
 * @Description: ems_data_demand
 * @@author 品讯科技
 * @Date:   2024-01-19
 * @Version: V1.0
 */
public interface DataDemandMapper extends BaseMapper<DataDemand> {

    /**
     * 监测参数查询——需量
     */
    List<DataDemandVO> dataList(ParamQueryDataDTO dto);

}
