package org.jeecg.modules.cmmsUpkeepItem.controller;

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
import org.jeecg.modules.cmmsInspectItem.entity.CmmsInspectItemVo;
import org.jeecg.modules.cmmsUpkeepItem.entity.CmmsUpkeepItem;
import org.jeecg.modules.cmmsUpkeepItem.entity.CmmsUpkeepItemDto;
import org.jeecg.modules.cmmsUpkeepItem.service.ICmmsUpkeepItemService;

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
 * @Description: 保养项
 * @@author 品讯科技
 * @Date:   2024-01-15
 * @Version: V1.0
 */
@Api(tags="保养项")
@RestController
@RequestMapping("/cmmsUpkeepItem/cmmsUpkeepItem")
@Slf4j
public class CmmsUpkeepItemController extends JeecgController<CmmsUpkeepItem, ICmmsUpkeepItemService> {
	@Autowired
	private ICmmsUpkeepItemService cmmsUpkeepItemService;

	 /**
	  * 根据设备id查找设备类型id以及设备类型名称，根据设备类型id查询保养项
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "保养项-通过设备id查询")
	 @ApiOperation(value="保养项-通过设备id查询", notes="保养项-通过设备id查询")
	 @GetMapping(value = "/getCmmsUpkeepItemByEqid")
	 public Result<List<CmmsUpkeepItemDto>> getCmmsUpkeepItemByEqid(@RequestParam(name="id",required=true) String id) {
		 List<CmmsUpkeepItemDto> cmmsUpkeepItemDtoList = cmmsUpkeepItemService.getCmmsUpkeepItemByEqid(id);
		 if(cmmsUpkeepItemDtoList.size() == 0) {
			 return Result.error("未找到对应保养项");
		 }
		 return Result.OK(cmmsUpkeepItemDtoList);
	 }

	/**
	 * 分页列表查询
	 *
	 * @param cmmsUpkeepItem
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="保养项-分页列表查询", notes="保养项-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<CmmsUpkeepItem>> queryPageList(CmmsUpkeepItem cmmsUpkeepItem,
													   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													   HttpServletRequest req) {
		CmmsUpkeepItem cmmsUpkeepItem1 = new CmmsUpkeepItem();
		cmmsUpkeepItem1.setEquipdefid(cmmsUpkeepItem.getEquipdefid());
		QueryWrapper<CmmsUpkeepItem> queryWrapper = QueryGenerator.initQueryWrapper(cmmsUpkeepItem1, req.getParameterMap());
		queryWrapper.like(cmmsUpkeepItem.getItemcode()!=null && !cmmsUpkeepItem.getItemcode().equals(""),"itemcode",cmmsUpkeepItem.getItemcode());
		queryWrapper.like(cmmsUpkeepItem.getItemname()!=null && !cmmsUpkeepItem.getItemname().equals(""),"itemname",cmmsUpkeepItem.getItemname());
		Page<CmmsUpkeepItem> page = new Page<CmmsUpkeepItem>(pageNo, pageSize);
		IPage<CmmsUpkeepItem> pageList = cmmsUpkeepItemService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

//	//@AutoLog(value = "保养项-分页列表查询")
//	@ApiOperation(value="保养项-分页列表查询", notes="保养项-分页列表查询")
//	@GetMapping(value = "/list")
//	public Result<IPage<CmmsUpkeepItemDto>> queryPageList(CmmsUpkeepItem cmmsUpkeepItem,
//								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
//								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
//								   HttpServletRequest req) {
////		QueryWrapper<CmmsUpkeepItem> queryWrapper = QueryGenerator.initQueryWrapper(cmmsUpkeepItem, req.getParameterMap());
//		Page<CmmsUpkeepItemDto> page = new Page<CmmsUpkeepItemDto>(pageNo, pageSize);
////		IPage<CmmsUpkeepItem> pageList = cmmsUpkeepItemService.page(page, queryWrapper);
//		List<CmmsUpkeepItemDto> pageList = cmmsUpkeepItemService.getAllCmmsUpkeepItem(page, cmmsUpkeepItem);
//		page.setRecords(pageList);
//		return Result.OK(page);
//	}


	/**
	 *   添加
	 *
	 * @param cmmsUpkeepItem
	 * @return
	 */
	@AutoLog(value = "保养项-添加")
	@ApiOperation(value="保养项-添加", notes="保养项-添加")
	//@RequiresPermissions("org.jeecg.modules:ems_cmms_upkeep_item:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CmmsUpkeepItem cmmsUpkeepItem) {
//		cmmsUpkeepItemService.save(cmmsUpkeepItem);
		boolean isOk = cmmsUpkeepItemService.insertCmmsUpkeepItem(cmmsUpkeepItem);
		if(isOk){
			return Result.OK("添加成功！");
		}else{
			return Result.OK("添加失败！");
		}

	}

	/**
	 *  编辑
	 *
	 * @param cmmsUpkeepItem
	 * @return
	 */
	@AutoLog(value = "保养项-编辑")
	@ApiOperation(value="保养项-编辑", notes="保养项-编辑")
	//@RequiresPermissions("org.jeecg.modules:ems_cmms_upkeep_item:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CmmsUpkeepItem cmmsUpkeepItem) {
		cmmsUpkeepItemService.updateById(cmmsUpkeepItem);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "保养项-通过id删除")
	@ApiOperation(value="保养项-通过id删除", notes="保养项-通过id删除")
	//@RequiresPermissions("org.jeecg.modules:ems_cmms_upkeep_item:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		cmmsUpkeepItemService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "保养项-批量删除")
	@ApiOperation(value="保养项-批量删除", notes="保养项-批量删除")
	//@RequiresPermissions("org.jeecg.modules:ems_cmms_upkeep_item:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.cmmsUpkeepItemService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "保养项-通过id查询")
	@ApiOperation(value="保养项-通过id查询", notes="保养项-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<CmmsUpkeepItem> queryById(@RequestParam(name="id",required=true) String id) {
		CmmsUpkeepItem cmmsUpkeepItem = cmmsUpkeepItemService.getById(id);
		if(cmmsUpkeepItem==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(cmmsUpkeepItem);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param cmmsUpkeepItem
    */
    //@RequiresPermissions("org.jeecg.modules:ems_cmms_upkeep_item:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CmmsUpkeepItem cmmsUpkeepItem) {
        return super.exportXls(request, cmmsUpkeepItem, CmmsUpkeepItem.class, "保养项");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ems_cmms_upkeep_item:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CmmsUpkeepItem.class);
    }

}
