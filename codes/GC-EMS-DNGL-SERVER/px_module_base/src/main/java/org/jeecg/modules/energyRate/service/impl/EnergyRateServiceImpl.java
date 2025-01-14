package org.jeecg.modules.energyRate.service.impl;

import org.jeecg.modules.energyRate.entity.EnergyRate;
import org.jeecg.modules.energyRate.entity.EnergyRateDetail;
import org.jeecg.modules.energyRate.mapper.EnergyRateDetailMapper;
import org.jeecg.modules.energyRate.mapper.EnergyRateMapper;
import org.jeecg.modules.energyRate.service.IEnergyRateService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: base_energy_rate
 * @@author 品讯科技
 * @Date:   2024-01-23
 * @Version: V1.0
 */
@Service
public class EnergyRateServiceImpl extends ServiceImpl<EnergyRateMapper, EnergyRate> implements IEnergyRateService {

	@Autowired
	private EnergyRateMapper energyRateMapper;
	@Autowired
	private EnergyRateDetailMapper energyRateDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(EnergyRate energyRate, List<EnergyRateDetail> energyRateDetailList) {
		energyRateMapper.insert(energyRate);
		if(energyRateDetailList !=null && energyRateDetailList.size()>0) {
			for(EnergyRateDetail entity: energyRateDetailList) {
				//外键设置
				entity.setEnergyrateid(energyRate.getId());
				energyRateDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(EnergyRate energyRate, List<EnergyRateDetail> energyRateDetailList) {
		energyRateMapper.updateById(energyRate);
		
		//1.先删除子表数据
		energyRateDetailMapper.deleteByMainId(energyRate.getId());
		
		//2.子表数据重新插入
		if(energyRateDetailList !=null && energyRateDetailList.size()>0) {
			for(EnergyRateDetail entity: energyRateDetailList) {
				//外键设置
				entity.setEnergyrateid(energyRate.getId());
				energyRateDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		energyRateDetailMapper.deleteByMainId(id);
		energyRateMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			energyRateDetailMapper.deleteByMainId(id.toString());
			energyRateMapper.deleteById(id);
		}
	}
	
}
