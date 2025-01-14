package org.jeecg.modules.cmmsInspectTcontentEquip.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.cmmsInspectTcontentEquip.entity.CmmsInspectTcontentEquip;
import org.jeecg.modules.cmmsInspectTcontentEquip.service.ICmmsInspectTcontentEquipService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 巡检工单-路线-内容-设备
 * @@author 品讯科技
 * @Date:   2024-01-24
 * @Version: V1.0
 */
@Api(tags="巡检工单-路线-内容-设备")
@RestController
@RequestMapping("/cmmsInspectTcontentEquip/cmmsInspectTcontentEquip")
@Slf4j
public class CmmsInspectTcontentEquipController extends JeecgController<CmmsInspectTcontentEquip, ICmmsInspectTcontentEquipService> {
	@Autowired
	private ICmmsInspectTcontentEquipService cmmsInspectTcontentEquipService;
	
	/**
	 * 分页列表查询
	 *
	 * @param cmmsInspectTcontentEquip
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "巡检工单-路线-内容-设备-分页列表查询")
	@ApiOperation(value="巡检工单-路线-内容-设备-分页列表查询", notes="巡检工单-路线-内容-设备-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<CmmsInspectTcontentEquip>> queryPageList(CmmsInspectTcontentEquip cmmsInspectTcontentEquip,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CmmsInspectTcontentEquip> queryWrapper = QueryGenerator.initQueryWrapper(cmmsInspectTcontentEquip, req.getParameterMap());
		Page<CmmsInspectTcontentEquip> page = new Page<CmmsInspectTcontentEquip>(pageNo, pageSize);
		IPage<CmmsInspectTcontentEquip> pageList = cmmsInspectTcontentEquipService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param cmmsInspectTcontentEquip
	 * @return
	 */
	@AutoLog(value = "巡检工单-路线-内容-设备-添加")
	@ApiOperation(value="巡检工单-路线-内容-设备-添加", notes="巡检工单-路线-内容-设备-添加")
	//@RequiresPermissions("org.jeecg.modules:cmms_inspect_tcontent_equip:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CmmsInspectTcontentEquip cmmsInspectTcontentEquip) {
		cmmsInspectTcontentEquipService.save(cmmsInspectTcontentEquip);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param cmmsInspectTcontentEquip
	 * @return
	 */
	@AutoLog(value = "巡检工单-路线-内容-设备-编辑")
	@ApiOperation(value="巡检工单-路线-内容-设备-编辑", notes="巡检工单-路线-内容-设备-编辑")
	//@RequiresPermissions("org.jeecg.modules:cmms_inspect_tcontent_equip:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CmmsInspectTcontentEquip cmmsInspectTcontentEquip) {
		cmmsInspectTcontentEquipService.updateById(cmmsInspectTcontentEquip);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "巡检工单-路线-内容-设备-通过id删除")
	@ApiOperation(value="巡检工单-路线-内容-设备-通过id删除", notes="巡检工单-路线-内容-设备-通过id删除")
	//@RequiresPermissions("org.jeecg.modules:cmms_inspect_tcontent_equip:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		cmmsInspectTcontentEquipService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "巡检工单-路线-内容-设备-批量删除")
	@ApiOperation(value="巡检工单-路线-内容-设备-批量删除", notes="巡检工单-路线-内容-设备-批量删除")
	//@RequiresPermissions("org.jeecg.modules:cmms_inspect_tcontent_equip:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.cmmsInspectTcontentEquipService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "巡检工单-路线-内容-设备-通过id查询")
	@ApiOperation(value="巡检工单-路线-内容-设备-通过id查询", notes="巡检工单-路线-内容-设备-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<CmmsInspectTcontentEquip> queryById(@RequestParam(name="id",required=true) String id) {
		CmmsInspectTcontentEquip cmmsInspectTcontentEquip = cmmsInspectTcontentEquipService.getById(id);
		if(cmmsInspectTcontentEquip==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(cmmsInspectTcontentEquip);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param cmmsInspectTcontentEquip
    */
    //@RequiresPermissions("org.jeecg.modules:cmms_inspect_tcontent_equip:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CmmsInspectTcontentEquip cmmsInspectTcontentEquip) {
        return super.exportXls(request, cmmsInspectTcontentEquip, CmmsInspectTcontentEquip.class, "巡检工单-路线-内容-设备");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("cmms_inspect_tcontent_equip:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CmmsInspectTcontentEquip.class);
    }

}
