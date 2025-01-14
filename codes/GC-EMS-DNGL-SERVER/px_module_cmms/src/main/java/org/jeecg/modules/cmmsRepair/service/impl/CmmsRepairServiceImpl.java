package org.jeecg.modules.cmmsRepair.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.cmmsOrderNode.entity.CmmsOrderNode;
import org.jeecg.modules.cmmsOrderNode.service.ICmmsOrderNodeService;
import org.jeecg.modules.cmmsRepair.entity.CmmsRepair;
import org.jeecg.modules.cmmsRepair.entity.CmmsRepairVo;
import org.jeecg.modules.cmmsRepair.entity.IndexNum;
import org.jeecg.modules.cmmsRepair.mapper.CmmsRepairMapper;
import org.jeecg.modules.cmmsRepair.service.ICmmsRepairService;
import org.jeecg.modules.sysUploadFile.entity.SysUploadFile;
import org.jeecg.modules.sysUploadFile.service.ISysUploadFileService;
import org.jeecg.modules.system.service.impl.SysBaseApiImpl;
import org.jeecg.modules.tpmEquipment.entity.TpmEquipment;
import org.jeecg.modules.tpmEquipment.service.ITpmEquipmentService;
import org.jeecg.modules.util.AutoCodeUtil;
import org.jeecg.modules.util.UserConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.UUID;

/**
 * @Description: 维修工单
 * @@author 品讯科技
 * @Date:   2024-01-09
 * @Version: V1.0
 */
@Service
public class CmmsRepairServiceImpl extends ServiceImpl<CmmsRepairMapper, CmmsRepair> implements ICmmsRepairService {

    @Autowired
    @SuppressWarnings("all")
    private CmmsRepairMapper cmmsRepairMapper;

    @Autowired
    @SuppressWarnings("all")
    private ICmmsOrderNodeService cmmsOrderNodeService;
    @Autowired
    @SuppressWarnings("all")
    private AutoCodeUtil autoCodeUtil;
    @Autowired
    @SuppressWarnings("all")
    private ISysUploadFileService fileService;
    @Autowired
    @SuppressWarnings("all")
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    @SuppressWarnings("all")
    private ITpmEquipmentService equipmentService;


    @Override
    public boolean addCmmsRepair(CmmsRepairVo cmmsRepair) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser loginUser = (LoginUser)subject.getPrincipal();
        String username = loginUser.getUsername();
        cmmsRepair.setCreateBy(username);
        cmmsRepair.setCreateTime(DateUtils.getDate());
        TpmEquipment equipment = equipmentService.getById(cmmsRepair.getEquipmentid());
        if (ObjectUtil.isNull(cmmsRepair.getEquipmentcode())){
            cmmsRepair.setEquipmentcode(equipment.getEquipmentcode());
        }
        if (ObjectUtil.isNull(cmmsRepair.getEquipmentname())){
            cmmsRepair.setEquipmentname(equipment.getEquipmentname());
        }
        if (cmmsRepair.getRepaircode() == null || "".equals(cmmsRepair.getRepaircode())){
            cmmsRepair.setRepaircode(autoCodeUtil.genSerialCode(UserConstants.CMMSREPAIR_CODE, null));
        }

        //if (cmmsRepair.getId() == null || "".equals(cmmsRepair.getId())){
        //    cmmsRepair.setId(UUID.randomUUID().toString());
        //}

        if(ObjectUtil.isEmpty(cmmsRepair.getStatus()) && ObjectUtil.isEmpty(cmmsRepair.getRepairstatus())){
            cmmsRepair.setStatus("0");
            cmmsRepair.setRepairstatus("0");
        }

        boolean b = this.save(cmmsRepair);

        CmmsOrderNode cmmsOrderNode = new CmmsOrderNode();
        //cmmsOrderNode.setId(SnowFlakeUtils.snowFlakeId());
        cmmsOrderNode.setNodename("0");
        cmmsOrderNode.setRelid(cmmsRepair.getId());
        cmmsOrderNode.setCreateBy(cmmsRepair.getCreateBy());
        cmmsOrderNode.setCreateTime(DateUtils.getDate());
        cmmsOrderNode.setHandledate(DateUtils.getDate());
        cmmsOrderNode.setOpinion("——");
        cmmsOrderNodeService.save(cmmsOrderNode);

        // 附件
        fileService.batchSave(cmmsRepair.getFiles(), cmmsRepair.getId(), "");

        return b;
    }


    @Override
    public CmmsRepairVo selectById(String id) {
        CmmsRepairVo cmmsRepairVo = new CmmsRepairVo();
        // 工单记录
        CmmsRepair obj = this.getById(id);

        if (obj != null) {
            cmmsRepairVo.setId(obj.getId());
            cmmsRepairVo.setCreateBy(obj.getCreateBy());
            cmmsRepairVo.setCreateTime(obj.getCreateTime());
            cmmsRepairVo.setUpdateBy(obj.getUpdateBy());
            cmmsRepairVo.setUpdateTime(obj.getUpdateTime());
            cmmsRepairVo.setRemark(obj.getRemark());
            cmmsRepairVo.setRepaircode(obj.getRepaircode());
            cmmsRepairVo.setRepairname(obj.getRepairname());
            cmmsRepairVo.setEquipmentid(obj.getEquipmentid());
            cmmsRepairVo.setEquipmentcode(obj.getEquipmentcode());
            cmmsRepairVo.setEquipmentname(obj.getEquipmentname());
            cmmsRepairVo.setPriority(obj.getPriority());
            cmmsRepairVo.setFaultdate(obj.getFaultdate());
            cmmsRepairVo.setFaultdesc(obj.getFaultdesc());
            if(ObjectUtil.isNotNull(obj.getRepairuser())){
                LoginUser user = sysBaseAPI.getUserById(obj.getRepairuser());
                cmmsRepairVo.setRepairuser(user.getRealname());
            }else {
                cmmsRepairVo.setRepairstatus("");
            }
            cmmsRepairVo.setRepairdate(obj.getRepairdate());
            cmmsRepairVo.setFeedback(obj.getFeedback());
            cmmsRepairVo.setResult(obj.getResult());
            cmmsRepairVo.setStatus(obj.getStatus());
            cmmsRepairVo.setRelid(obj.getRelid());
            cmmsRepairVo.setRepairstatus(obj.getRepairstatus());
        }

        // 维修操作记录
        CmmsOrderNode node = new CmmsOrderNode();
        node.setRelid(id);
        cmmsRepairVo.setNodeList(cmmsOrderNodeService.selectCmmsOrderNodeList(node));

        // 附件
        cmmsRepairVo.setFiles(fileService.selectFileList(new SysUploadFile(id, "")));

        return cmmsRepairVo;
    }

    @Override
    public List<CmmsRepair> selectList(IPage page,CmmsRepair cmmsRepair) {
        return cmmsRepairMapper.selectListByObj(page,cmmsRepair);
    }

    @Override
    public int repair(CmmsRepairVo cmmsRepair) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser loginUser = (LoginUser)subject.getPrincipal();
        String username = loginUser.getUsername();
        // 操作记录信息
        if (cmmsRepair.getNodeList() != null && cmmsRepair.getNodeList().size() > 0) {
            CmmsOrderNode node = cmmsRepair.getNodeList().get(0);
            node.setRelid(cmmsRepair.getId());
            node.setCreateBy(username);
            node.setCreateTime(DateUtils.getDate());
            if ("1".equals(cmmsRepair.getStatus())){
                LoginUser user = sysBaseAPI.getUserById(cmmsRepair.getNodeList().get(0).getOpinion());
                node.setOpinion(user.getRealname());
                node.setHandledate(DateUtils.getDate());
            }
            cmmsOrderNodeService.save(node);
        }

        // 附件
        fileService.batchSave(cmmsRepair.getFiles(), cmmsRepair.getId(), "");
        // 维修信息
        return cmmsRepairMapper.updateCmmsRepair(cmmsRepair);
    }

    @Override
    public int updateRepairStatus(CmmsRepair cmmsRepair) {
        return cmmsRepairMapper.updateRepairStatus(cmmsRepair.getId(),cmmsRepair.getRepairstatus());
    }

    @Override
    public IndexNum IndexRepairNum() {
        List<Integer> list = cmmsRepairMapper.getIndexRepairNum();
        IndexNum indexNum = new IndexNum();
        if (list.size() == 2 && ObjectUtil.isNotNull(list.get(0)) && ObjectUtil.isNotNull(list.get(1))){
            indexNum.setTodaynum(list.get(1));
            indexNum.setTotalnum(list.get(0));
        }else if (list.size() == 1 && ObjectUtil.isNotNull(0)){
            indexNum.setTodaynum(0);
            indexNum.setTotalnum(list.get(0));
        }else if (list.size() == 1 && ObjectUtil.isNotNull(1)){
            indexNum.setTodaynum(list.get(1));
            indexNum.setTotalnum(0);
        }else {
            indexNum.setTodaynum(0);
            indexNum.setTotalnum(0);
        }

        return indexNum;
    }

    @Override
    public List<CmmsRepair> getPageList(Page<CmmsRepair> page, CmmsRepair cmmsRepair) {
        return cmmsRepairMapper.getPageList(page,cmmsRepair);
    }
}
