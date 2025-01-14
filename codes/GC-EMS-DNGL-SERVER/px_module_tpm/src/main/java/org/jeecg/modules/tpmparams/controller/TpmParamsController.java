package org.jeecg.modules.tpmparams.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.tpmparams.entity.TpmParams;
import org.jeecg.modules.tpmparams.service.ITpmParamsService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 设备参数
 * @@author 品讯科技
 * @Date:   2024-01-11
 * @Version: V1.0
 */
@Api(tags="设备参数")
@RestController
@RequestMapping("/tpmParams/tpmParams")
@Slf4j
public class TpmParamsController extends JeecgController<TpmParams, ITpmParamsService> {
	@Autowired
	private ITpmParamsService tpmParamsService;

	 /**
	  * 取得工程下的测点(Tag)信息(从数据库中取，且去掉系统中已经设置好的)
	  */
	 @ApiOperation("取得工程下的测点(Tag)信息(从数据库中取，且去掉系统中已经设置好的)")
	 @GetMapping("/tagList")
	 public Result<List<TpmParams>> tagList(TpmParams params) {
		 List<TpmParams> paramsList = tpmParamsService.selectTpmParamsList(params);
		 return Result.OK(paramsList);
	 }

	/**
	 * 分页列表查询
	 *
	 * @param tpmParams
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "设备参数-分页列表查询")
	@ApiOperation(value="设备参数-分页列表查询", notes="设备参数-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TpmParams>> queryPageList(TpmParams tpmParams,
												  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												  HttpServletRequest req) {
		QueryWrapper<TpmParams> queryWrapper = QueryGenerator.initQueryWrapper(tpmParams, req.getParameterMap());
		Page<TpmParams> page = new Page<TpmParams>(pageNo, pageSize);
		IPage<TpmParams> pageList = tpmParamsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param tpmParams
	 * @return
	 */
	@AutoLog(value = "设备参数-添加")
	@ApiOperation(value="设备参数-添加", notes="设备参数-添加")
	//@RequiresPermissions("org.jeecg.modules:tpm_params:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TpmParams tpmParams) {
		tpmParamsService.save(tpmParams);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param tpmParams
	 * @return
	 */
	@AutoLog(value = "设备参数-编辑")
	@ApiOperation(value="设备参数-编辑", notes="设备参数-编辑")
	//@RequiresPermissions("org.jeecg.modules:tpm_params:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TpmParams tpmParams) {
		tpmParamsService.updateById(tpmParams);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "设备参数-通过id删除")
	@ApiOperation(value="设备参数-通过id删除", notes="设备参数-通过id删除")
	//@RequiresPermissions("org.jeecg.modules:tpm_params:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		tpmParamsService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "设备参数-批量删除")
	@ApiOperation(value="设备参数-批量删除", notes="设备参数-批量删除")
	//@RequiresPermissions("org.jeecg.modules:tpm_params:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tpmParamsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "设备参数-通过id查询")
	@ApiOperation(value="设备参数-通过id查询", notes="设备参数-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TpmParams> queryById(@RequestParam(name="id",required=true) String id) {
		TpmParams tpmParams = tpmParamsService.getById(id);
		if(tpmParams ==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(tpmParams);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param tpmParams
    */
    //@RequiresPermissions("org.jeecg.modules:tpm_params:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TpmParams tpmParams) {
        return super.exportXls(request, tpmParams, TpmParams.class, "设备参数");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("tpm_params:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TpmParams.class);
    }

}
