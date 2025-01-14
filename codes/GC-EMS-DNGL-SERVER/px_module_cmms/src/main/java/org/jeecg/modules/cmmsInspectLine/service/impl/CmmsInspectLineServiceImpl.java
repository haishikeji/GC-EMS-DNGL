package org.jeecg.modules.cmmsInspectLine.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cmmsInspectContent.entity.CmmsInspectContentVo;
import org.jeecg.modules.cmmsInspectContent.service.ICmmsInspectContentService;
import org.jeecg.modules.cmmsInspectLine.convert.CmmsInspectLineConvert;
import org.jeecg.modules.cmmsInspectLine.entity.CmmsInspectLine;
import org.jeecg.modules.cmmsInspectLine.entity.CmmsInspectLineVo;
import org.jeecg.modules.cmmsInspectLine.mapper.CmmsInspectLineMapper;
import org.jeecg.modules.cmmsInspectLine.service.ICmmsInspectLineService;
import org.jeecg.modules.cmmsInspectLineDetail.entity.CmmsInspectLineDetail;
import org.jeecg.modules.cmmsInspectLineDetail.entity.CmmsInspectLineDetailVo;
import org.jeecg.modules.cmmsInspectLineDetail.service.ICmmsInspectLineDetailService;
import org.jeecg.modules.cmmsInspectSpot.entity.CmmsInspectSpotVo;
import org.jeecg.modules.cmmsInspectSpot.service.ICmmsInspectSpotService;
import org.jeecg.modules.cmmsInspectSpotEquip.entity.CmmsInspectSpotEquip;
import org.jeecg.modules.cmmsInspectSpotEquip.service.ICmmsInspectSpotEquipService;
import org.jeecg.modules.cmmsInspectTcontentItem.service.ICmmsInspectTcontentItemService;
import org.jeecg.modules.util.AutoCodeUtil;
import org.jeecg.modules.util.UserConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Description: 巡检路线
 * @@author 品讯科技
 * @Date:   2024-01-22
 * @Version: V1.0
 */
@Service
public class CmmsInspectLineServiceImpl extends ServiceImpl<CmmsInspectLineMapper, CmmsInspectLine> implements ICmmsInspectLineService {

    @Autowired
    @SuppressWarnings("all")
    private CmmsInspectLineMapper inspectLineMapper;
    @Autowired
    @SuppressWarnings("all")
    private AutoCodeUtil autoCodeUtil;
    @Autowired
    @SuppressWarnings("all")
    private ICmmsInspectLineDetailService inspectLineDetailService;
    @Autowired
    @SuppressWarnings("all")
    private ICmmsInspectSpotEquipService inspectSpotEquipService;
    @Autowired
    @SuppressWarnings("all")
    private ICmmsInspectContentService inspectContentService;
    @Autowired
    @SuppressWarnings("all")
    private ICmmsInspectSpotService inspectSpotService;
    @Autowired
    @SuppressWarnings("all")
    private ICmmsInspectTcontentItemService inspectTcontentItemService;

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 新增巡检路线
    *   date: 2024/1/22
    */
    @Override
    public boolean addCmmsInspectLine(CmmsInspectLineVo cmmsInspectLine) {
        if (cmmsInspectLine.getId() == null || "".equals(cmmsInspectLine.getId())){
            cmmsInspectLine.setLinecode(autoCodeUtil.genSerialCode(UserConstants.CMMSINSPECTLINE_CODE, null));
        }
        boolean b = this.save(cmmsInspectLine);
        // 批量保存巡检路线详情 （同一个巡检路线存在多少巡检点）
        batchSaveSub(cmmsInspectLine);

        return b;
    }

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 修改巡检路线
    *   date: 2024/1/22
    */
    @Override
    public boolean updateCmmsInspectLine(CmmsInspectLineVo cmmsInspectLine) {
        // 根据巡检路线id删除巡检路线详情
        QueryWrapper<CmmsInspectLineDetail> quer = new QueryWrapper<>();
        quer.eq("inspectlineid",cmmsInspectLine.getId());
        inspectLineDetailService.remove(quer);

        batchSaveSub(cmmsInspectLine);

        return this.updateById(cmmsInspectLine);
    }

    /**
     *   author: dzc
     *   version: 1.0
     *   des: 根据id查询巡检路线详情
     *   date: 2024/1/24
     */
    @Override
    public CmmsInspectLineVo getCmmsInspectLineById(String id) {
        // 查询巡检路线
        CmmsInspectLine cmmsInspectLine = this.getById(id);
        CmmsInspectLineVo inspectLineVo = CmmsInspectLineConvert.INSTANCE.convert(cmmsInspectLine);

        // 查询巡检路线详情
        List<CmmsInspectLineDetailVo> list = inspectLineDetailService.selectCmmsInspectLineDetailList(id);

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                // 查询出每个巡检的下的巡检内容
                List<CmmsInspectSpotEquip> spotcountList = inspectSpotEquipService.getInspectSpotEquipBySprtId(list.get(i).getInspectspotid());
                ArrayList<CmmsInspectContentVo> contentList = new ArrayList<>();
                if (spotcountList != null && spotcountList.size() > 0) {
                    for (int j = 0; j < spotcountList.size(); j++) {
                        CmmsInspectContentVo cmmsInspectContent = inspectContentService.getCmmsInspectContentById(spotcountList.get(j).getInspectcontid());
                        contentList.add(cmmsInspectContent);
                    }
                }
                list.get(i).setInspectContentList(contentList);
            }
        }

        inspectLineVo.setDetailList(list);

        return inspectLineVo;
    }

    @Override
    public CmmsInspectLineVo getCmmsInspectLineTreeById(String id) {
        // 查询巡检路线
        CmmsInspectLine cmmsInspectLine = this.getById(id);
        CmmsInspectLineVo inspectLineVo = CmmsInspectLineConvert.INSTANCE.convert(cmmsInspectLine);

        // 查询巡检路线详情
        List<CmmsInspectLineDetailVo> list = inspectLineDetailService.selectCmmsInspectLineDetailList(id);

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                // 查询出每个巡检的下的巡检内容
                List<CmmsInspectSpotEquip> spotcountList = inspectSpotEquipService.getInspectSpotEquipBySprtId(list.get(i).getInspectspotid());
                ArrayList<CmmsInspectContentVo> contentList = new ArrayList<>();
                if (spotcountList != null && spotcountList.size() > 0) {
                    for (int j = 0; j < spotcountList.size(); j++) {
                        CmmsInspectContentVo cmmsInspectContent = inspectContentService.getCmmsInspectContentTreeById(spotcountList.get(j).getInspectcontid());
                        contentList.add(cmmsInspectContent);
                    }
                }
                list.get(i).setTitle(list.get(i).getContentname());
                list.get(i).setKey(list.get(i).getContentname());
                list.get(i).setType("巡检点");
                list.get(i).setSonList(contentList);
            }
        }

        inspectLineVo.setTitle(cmmsInspectLine.getLinename());
        inspectLineVo.setKey(cmmsInspectLine.getLinename());
        inspectLineVo.setType("巡检路线");
        inspectLineVo.setSonList(list);

        return inspectLineVo;
    }

    /**
     *   author: dzc
     *   version: 1.0
     *   des: 通过id查询（将巡检项变成 巡检工单-巡检项的格式）
     *   date: 2024/1/26
     */
    @Override
    public CmmsInspectLineVo getCmmsInspectLineByIdA(String id,String inspectid,String tasktype) {
        // 查询巡检路线
        CmmsInspectLine cmmsInspectLine = this.getById(id);
        CmmsInspectLineVo inspectLineVo = CmmsInspectLineConvert.INSTANCE.convert(cmmsInspectLine);

        // 查询巡检路线详情
        List<CmmsInspectLineDetailVo> list = inspectLineDetailService.selectCmmsInspectLineDetailList(id);

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                // 查询出每个巡检的下的巡检内容
                List<CmmsInspectSpotEquip> spotcountList = inspectSpotEquipService.getInspectSpotEquipBySprtId(list.get(i).getInspectspotid());
                ArrayList<CmmsInspectContentVo> contentList = new ArrayList<>();
                if (spotcountList != null && spotcountList.size() > 0) {
                    for (int j = 0; j < spotcountList.size(); j++) {
                        CmmsInspectContentVo cmmsInspectContent = inspectContentService.getCmmsInspectContentByIdA(spotcountList.get(j).getInspectcontid(),inspectid,tasktype);
                        contentList.add(cmmsInspectContent);
                    }
                }
                list.get(i).setInspectContentList(contentList);
            }
        }

        inspectLineVo.setDetailList(list);

        return inspectLineVo;
    }


    @Override
    public CmmsInspectLineVo getCmmsInspectLineTreeByIdA(String id,String inspectid,String tasktype) {
        // 查询巡检路线
        CmmsInspectLine cmmsInspectLine = this.getById(id);
        CmmsInspectLineVo inspectLineVo = CmmsInspectLineConvert.INSTANCE.convert(cmmsInspectLine);

        // 查询巡检路线详情
        List<CmmsInspectLineDetailVo> list = inspectLineDetailService.selectCmmsInspectLineDetailList(id);

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                // 查询出每个巡检的下的巡检内容
                List<CmmsInspectSpotEquip> spotcountList = inspectSpotEquipService.getInspectSpotEquipBySprtId(list.get(i).getInspectspotid());
                ArrayList<CmmsInspectContentVo> contentList = new ArrayList<>();
                if (spotcountList != null && spotcountList.size() > 0) {
                    for (int j = 0; j < spotcountList.size(); j++) {
                        CmmsInspectContentVo cmmsInspectContent = inspectContentService.getCmmsInspectContentTreeByIdA(spotcountList.get(j).getInspectcontid(),inspectid,tasktype);
                        contentList.add(cmmsInspectContent);
                    }
                }
                boolean flag = true;
                for (CmmsInspectContentVo cont:contentList) {
                    // 如果巡检内容中 的countStatus有一个不为1 则该巡检点状态为0
                    if ("0".equals(cont.getCompletionStatus())){
                        flag = false;
                        break;
                    }
                }
                if (flag){
                    list.get(i).setCompletionStatus("1");
                }else {
                    list.get(i).setCompletionStatus("0");
                }
                list.get(i).setTitle(list.get(i).getContentname());
                list.get(i).setKey(list.get(i).getContentname());
                list.get(i).setType("巡检点");
                list.get(i).setChildren(contentList);
            }
        }
        boolean f = true;
        for (CmmsInspectLineDetailVo vo : list) {
            // 如果有任何一个巡检点的状态为0 则证明该巡检路线未完成 为0
            if ("0".equals(vo.getCompletionStatus())){
                f = false;
                break;
            }
        }
        if (f){
            inspectLineVo.setCompletionStatus("1");
        }else {
            inspectLineVo.setCompletionStatus("0");
        }
        inspectLineVo.setTitle(cmmsInspectLine.getLinename());
        inspectLineVo.setKey(cmmsInspectLine.getLinename());
        inspectLineVo.setType("巡检路线");
        inspectLineVo.setChildren(list);

        return inspectLineVo;
    }


    @Override
    public String getNameById(String id) {
        return inspectLineMapper.getNameById(id);
    }


    @Override
    public List<CmmsInspectLineVo> getCmmsInspectLineListDetails(Page<CmmsInspectLineVo> page, CmmsInspectLine cmmsInspectLine) {
        List<CmmsInspectLineVo> lineList = inspectLineMapper.getCmmsInspectLineListDetails(page, cmmsInspectLine);
        ArrayList<CmmsInspectLineVo> list = new ArrayList<>();
        ArrayList<CompletableFuture<CmmsInspectLineVo>> completableFutureList = new ArrayList<>();

        for (CmmsInspectLineVo vo:lineList) {
            CompletableFuture<CmmsInspectLineVo> getInspectListFuture = CompletableFuture.supplyAsync(() -> {
                CmmsInspectLineVo inspectLine = this.getCmmsInspectLineById(vo.getId());
                return inspectLine;
            });
            completableFutureList.add(getInspectListFuture);
        }

        CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0])).thenRun(() -> {
            for (CompletableFuture<CmmsInspectLineVo> future:completableFutureList) {
                CmmsInspectLineVo join = future.join();
                list.add(join);
            }
        }).join();
        //for (CmmsInspectLineVo vo : lineList) {
        //    CmmsInspectLineVo inspectLine = this.getCmmsInspectLineById(vo.getId());
        //    list.add(inspectLine);
        //}

        return list;
    }

    @Override
    public String getCodeById(String relid) {
        return inspectLineMapper.getCodeById(relid);
    }

    private void batchSaveSub(CmmsInspectLineVo cmmsInspectLine) {
        List<CmmsInspectLineDetailVo> detailList = cmmsInspectLine.getDetailList();
        ArrayList<CmmsInspectLineDetail> list = new ArrayList<>();
        if (detailList.size() > 0 && detailList != null &&  detailList.get(0) != null){
            for (int i = 0; i < detailList.size(); i++) {
                detailList.get(i).setInspectlineid(cmmsInspectLine.getId());
                CmmsInspectLineDetail cmmsInspectLineDetail = new CmmsInspectLineDetail();
                cmmsInspectLineDetail.setInspectlineid(cmmsInspectLine.getId());
                if (ObjectUtil.isNotNull(detailList.get(i).getInspectspotid())){
                    cmmsInspectLineDetail.setInspectspotid(detailList.get(i).getInspectspotid());
                }
                list.add(cmmsInspectLineDetail);
            }
        }
        // 保存巡检路线详细
        inspectLineDetailService.saveBatch(list);
    }
}
