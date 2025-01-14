package org.jeecg.modules.cmmsInspectSpotEquip.controller;

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
import org.jeecg.modules.cmmsInspectSpotEquip.entity.CmmsInspectSpotEquip;
import org.jeecg.modules.cmmsInspectSpotEquip.service.ICmmsInspectSpotEquipService;

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
 * @Description: 巡检点-设备
 * @@author 品讯科技
 * @Date:   2024-01-17
 * @Version: V1.0
 */
@Api(tags="巡检点-设备")
@RestController
@RequestMapping("/cmmsInspectSpotEquip/cmmsInspectSpotEquip")
@Slf4j
public class CmmsInspectSpotEquipController extends JeecgController<CmmsInspectSpotEquip, ICmmsInspectSpotEquipService> {
	@Autowired
	private ICmmsInspectSpotEquipService cmmsInspectSpotEquipService;
	
	/**
	 * 分页列表查询
	 *
	 * @param cmmsInspectSpotEquip
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "巡检点-设备-分页列表查询")
	@ApiOperation(value="巡检点-设备-分页列表查询", notes="巡检点-设备-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<CmmsInspectSpotEquip>> queryPageList(CmmsInspectSpotEquip cmmsInspectSpotEquip,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CmmsInspectSpotEquip> queryWrapper = QueryGenerator.initQueryWrapper(cmmsInspectSpotEquip, req.getParameterMap());
		Page<CmmsInspectSpotEquip> page = new Page<CmmsInspectSpotEquip>(pageNo, pageSize);
		IPage<CmmsInspectSpotEquip> pageList = cmmsInspectSpotEquipService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param cmmsInspectSpotEquip
	 * @return
	 */
	@AutoLog(value = "巡检点-设备-添加")
	@ApiOperation(value="巡检点-设备-添加", notes="巡检点-设备-添加")
	//@RequiresPermissions("org.jeecg.modules:ems_cmms_inspect_spot_equip:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CmmsInspectSpotEquip cmmsInspectSpotEquip) {
		cmmsInspectSpotEquipService.save(cmmsInspectSpotEquip);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param cmmsInspectSpotEquip
	 * @return
	 */
	@AutoLog(value = "巡检点-设备-编辑")
	@ApiOperation(value="巡检点-设备-编辑", notes="巡检点-设备-编辑")
	//@RequiresPermissions("org.jeecg.modules:ems_cmms_inspect_spot_equip:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CmmsInspectSpotEquip cmmsInspectSpotEquip) {
		cmmsInspectSpotEquipService.updateById(cmmsInspectSpotEquip);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "巡检点-设备-通过id删除")
	@ApiOperation(value="巡检点-设备-通过id删除", notes="巡检点-设备-通过id删除")
	//@RequiresPermissions("org.jeecg.modules:ems_cmms_inspect_spot_equip:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		cmmsInspectSpotEquipService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "巡检点-设备-批量删除")
	@ApiOperation(value="巡检点-设备-批量删除", notes="巡检点-设备-批量删除")
	//@RequiresPermissions("org.jeecg.modules:ems_cmms_inspect_spot_equip:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.cmmsInspectSpotEquipService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "巡检点-设备-通过id查询")
	@ApiOperation(value="巡检点-设备-通过id查询", notes="巡检点-设备-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<CmmsInspectSpotEquip> queryById(@RequestParam(name="id",required=true) String id) {
		CmmsInspectSpotEquip cmmsInspectSpotEquip = cmmsInspectSpotEquipService.getById(id);
		if(cmmsInspectSpotEquip==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(cmmsInspectSpotEquip);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param cmmsInspectSpotEquip
    */
    //@RequiresPermissions("org.jeecg.modules:ems_cmms_inspect_spot_equip:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CmmsInspectSpotEquip cmmsInspectSpotEquip) {
        return super.exportXls(request, cmmsInspectSpotEquip, CmmsInspectSpotEquip.class, "巡检点-设备");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ems_cmms_inspect_spot_equip:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CmmsInspectSpotEquip.class);
    }

}
