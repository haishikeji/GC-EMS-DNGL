package org.jeecg.modules.energyRate.service.impl;

import org.jeecg.modules.energyRate.entity.EnergyRateDetail;
import org.jeecg.modules.energyRate.mapper.EnergyRateDetailMapper;
import org.jeecg.modules.energyRate.service.IEnergyRateDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: base_energy_rate_detail
 * @@author 品讯科技
 * @Date:   2024-01-23
 * @Version: V1.0
 */
@Service
public class EnergyRateDetailServiceImpl extends ServiceImpl<EnergyRateDetailMapper, EnergyRateDetail> implements IEnergyRateDetailService {
	
	@Autowired
	private EnergyRateDetailMapper energyRateDetailMapper;
	
	@Override
	public List<EnergyRateDetail> selectByMainId(String mainId) {
		return energyRateDetailMapper.selectByMainId(mainId);
	}
}
