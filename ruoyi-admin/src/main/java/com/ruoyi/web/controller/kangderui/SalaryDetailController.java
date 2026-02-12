package com.ruoyi.web.controller.kangderui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.common.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.kangderui.domain.SalaryDetail;
import com.ruoyi.kangderui.service.ISalaryDetailService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.model.LoginUser;

/**
 * 员工工资明细Controller
 *
 * @author ruoyi
 * @date 2026-02-02
 */
@RestController
@RequestMapping("/kangderui/detail")
public class SalaryDetailController extends BaseController {

    @Autowired
    private ISalaryDetailService salaryDetailService;

    @Autowired
    ISysUserService iSysUserService;

    /**
     * 查询员工工资明细列表
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(SalaryDetail salaryDetail) {
        checkPasswordUpdatedForSalaryView();
        applyUserScope(salaryDetail);
        startPage();
        List<SalaryDetail> list = salaryDetailService.selectSalaryDetailList(salaryDetail);
        return getDataTable(list);
    }

    /**
     * 查询我的工资明细列表
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:my')")
    @GetMapping("/my/list")
    public TableDataInfo myList(SalaryDetail salaryDetail) {
        checkPasswordUpdatedForSalaryView();
        if (salaryDetail == null) {
            salaryDetail = new SalaryDetail();
        }
        salaryDetail.setUserId(getUserId());
        startPage();
        List<SalaryDetail> list = salaryDetailService.selectSalaryDetailList(salaryDetail);
        return getDataTable(list);
    }

    /**
     * 导出员工工资明细列表
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:export')")
    @Log(title = "员工工资明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SalaryDetail salaryDetail) {
        checkPasswordUpdatedForSalaryView();
        applyUserScope(salaryDetail);
        List<SalaryDetail> list = salaryDetailService.selectSalaryDetailList(salaryDetail);
        ExcelUtil<SalaryDetail> util = new ExcelUtil<SalaryDetail>(SalaryDetail.class);
        util.exportExcel(response, list, "员工工资明细数据");
    }

    /**
     * 导入员工工资明细数据
     */
    @Log(title = "员工工资明细", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('kangderui:detail:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        List<Map<Integer, Object>> rowMaps = parseExcelToMapList(file, 4);
        Object deptNameValue = getExcelCellValue(file, 0, 0, 0);
        Object salaryPeriod = getExcelCellValue(file, 0, 1, 7);
        List<SalaryDetail> detailList = mapToSalaryDetails(rowMaps, deptNameValue, salaryPeriod);
        String operName = getUsername();
        String message = salaryDetailService.importSalaryDetail(detailList, operName);
        return success(message);
    }

    /**
     * 导入模板下载
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<SalaryDetail> util = new ExcelUtil<SalaryDetail>(SalaryDetail.class);
        util.importTemplateExcel(response, "员工工资明细数据");
    }

    /**
     * 获取员工工资明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:query')")
    @GetMapping(value = "/{salaryDetailId}")
    public AjaxResult getInfo(@PathVariable("salaryDetailId") Long salaryDetailId) {
        checkPasswordUpdatedForSalaryView();
        SalaryDetail detail = salaryDetailService.selectSalaryDetailBySalaryDetailId(salaryDetailId);
        if (detail == null) {
            return error("数据不存在");
        }
        if (!isAdminUser() && detail.getUserId() != null && !detail.getUserId().equals(getUserId())) {
            return error("没有权限查看该工资明细");
        }
        return success(detail);
    }

    /**
     * 新增员工工资明细
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:add')")
    @Log(title = "员工工资明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SalaryDetail salaryDetail) {
        if (!isAdminUser()) {
            salaryDetail.setUserId(getUserId());
        }
        return toAjax(salaryDetailService.insertSalaryDetail(salaryDetail));
    }

    /**
     * 修改员工工资明细
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:edit')")
    @Log(title = "员工工资明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SalaryDetail salaryDetail) {
        if (!isAdminUser()) {
            return error("没有权限修改工资明细");
        }
        return toAjax(salaryDetailService.updateSalaryDetail(salaryDetail));
    }

    /**
     * 员工确认工资无误
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:confirm')")
    @Log(title = "员工工资确认", businessType = BusinessType.UPDATE)
    @PutMapping("/confirm/{salaryDetailId}")
    public AjaxResult confirm(@PathVariable("salaryDetailId") Long salaryDetailId) {
        checkPasswordUpdatedForSalaryView();
        SalaryDetail detail = salaryDetailService.selectSalaryDetailBySalaryDetailId(salaryDetailId);
        if (detail == null) {
            return error("数据不存在");
        }
        if (!isAdminUser() && detail.getUserId() != null && !detail.getUserId().equals(getUserId())) {
            return error("没有权限确认该工资");
        }
        if (StringUtils.equals("1", detail.getRemark())) {
            return success("已确认");
        }
        SalaryDetail update = new SalaryDetail();
        update.setSalaryDetailId(salaryDetailId);
        update.setRemark("1");
        update.setUpdateBy(getUsername());
        return toAjax(salaryDetailService.updateSalaryDetail(update));
    }

    /**
     * 删除员工工资明细
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:remove')")
    @Log(title = "员工工资明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{salaryDetailIds}")
    public AjaxResult remove(@PathVariable Long[] salaryDetailIds) {
        return toAjax(salaryDetailService.deleteSalaryDetailBySalaryDetailIds(salaryDetailIds));
    }

    private List<Map<Integer, Object>> parseExcelToMapList(MultipartFile file, int titleRowIndex) throws Exception {
        List<Map<Integer, Object>> rows = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return rows;
            }
            Row headerRow = sheet.getRow(titleRowIndex);
            if (headerRow == null) {
                return rows;
            }
            int lastCellNum = headerRow.getLastCellNum();
            List<String> headers = new ArrayList<>();
            for (int i = 0; i < lastCellNum; i++) {
                String header = StringUtils.trimToEmpty(Convert.toStr(getCellValue(headerRow.getCell(i))));
                headers.add(header);
            }
            int lastRowNum = sheet.getLastRowNum();
            for (int i = titleRowIndex + 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                if (isRowEmpty(row)) {
                    continue;
                }
                Map<Integer, Object> rowMap = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    String header = headers.get(j);
                    if (StringUtils.isEmpty(header)) {
                        continue;
                    }
                    rowMap.put(j, getCellValue(row.getCell(j)));
                }
                rows.add(rowMap);
            }
        }
        return rows;
    }

    private void applyUserScope(SalaryDetail salaryDetail) {
        if (salaryDetail == null) {
            return;
        }
        if (!isAdminUser()) {
            salaryDetail.setUserId(getUserId());
        }
    }

    private void checkPasswordUpdatedForSalaryView() {
        if (isAdminUser()) {
            return;
        }
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser != null ? loginUser.getUser() : null;
        if (user == null || user.getPwdUpdateDate() == null) {
            throw new ServiceException("请先修改初始密码后再查看工资");
        }
    }

    private boolean isAdminUser() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            return false;
        }
        if (loginUser.getUser().isAdmin()) {
            return true;
        }
        Long deptId = loginUser.getUser().getDeptId();
        return deptId != null && (deptId == 100L || deptId == 109L);
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK && StringUtils.isNotEmpty(Convert.toStr(getCellValue(cell)))) {
                return false;
            }
        }
        return true;
    }

    private Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            cellType = cell.getCachedFormulaResultType();
        }
        switch (cellType) {
            case STRING:
                return StringUtils.trimToEmpty(cell.getStringCellValue());
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                return cell.getNumericCellValue();
            case BLANK:
                return null;
            default:
                return StringUtils.trimToEmpty(cell.toString());
        }
    }

    private Object getExcelCellValue(MultipartFile file, int sheetIndex, int rowIndex, int columnIndex) throws Exception {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if (sheet == null) {
                return null;
            }
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                return null;
            }
            Cell cell = row.getCell(columnIndex);
            return getCellValue(cell);
        }
    }

    private List<SalaryDetail> mapToSalaryDetails(List<Map<Integer, Object>> rowMaps, Object deptNameValue, Object salaryPeriod) {
        List<SalaryDetail> details = new ArrayList<>();
        if (rowMaps == null || rowMaps.isEmpty()) {
            return details;
        }
        for (Map<Integer, Object> rowMap : rowMaps) {
            if ("合计：".equals(rowMap.get(0))) {
                continue;
            }
            if (String.valueOf(rowMap.get(2)).contains("制表")) {
                continue;
            }
            SalaryDetail detail = new SalaryDetail();
            //姓名
            detail.setNickName(ObjectUtils.isEmpty(rowMap.get(2)) ? null : Convert.toStr(rowMap.get(2)));
            SysUser sysUser = iSysUserService.selectUserByNickName(detail.getNickName());
            if (null == sysUser) {
                continue;
            }
            detail.setUserId(sysUser.getUserId());
            //工资卡号
            detail.setBankCardNumber(ObjectUtils.isEmpty(rowMap.get(1)) ? null : Convert.toStr(rowMap.get(1)));
            //部门ID
            detail.setDeptId(sysUser.getDeptId());
            //部门名称
            detail.setDeptName(ObjectUtils.isEmpty(deptNameValue) ? null : Convert.toStr(deptNameValue).replace("工资计算表", "").replace("人员", ""));
            //工资所属期
            detail.setSalaryPeriod(ObjectUtils.isEmpty(salaryPeriod) ? null : Convert.toStr(salaryPeriod).replace("所属期：", ""));
            //基本工资
            detail.setBasicSalary(ObjectUtils.isEmpty(rowMap.get(3)) ? null : Convert.toBigDecimal(rowMap.get(3)));
            //日工资
            detail.setBasicDailySalary(ObjectUtils.isEmpty(rowMap.get(4)) ? null : Convert.toBigDecimal(rowMap.get(4)));
            //工作日
            detail.setBasicWorkDays(ObjectUtils.isEmpty(rowMap.get(5)) ? null : Convert.toLong(rowMap.get(5)));
            //基本工资小计
            detail.setBasicSubtotal(ObjectUtils.isEmpty(rowMap.get(6)) ? null : Convert.toBigDecimal(rowMap.get(6)));
            //全勤奖
            detail.setAllowanceFullAttendance(ObjectUtils.isEmpty(rowMap.get(7)) ? null : Convert.toBigDecimal(rowMap.get(7)));
            //安全奖
            detail.setAllowanceSafety(ObjectUtils.isEmpty(rowMap.get(8)) ? null : Convert.toBigDecimal(rowMap.get(8)));
            //工龄工资
            detail.setAllowanceSeniority(ObjectUtils.isEmpty(rowMap.get(9)) ? null : Convert.toBigDecimal(rowMap.get(9)));
            //职务工资
            detail.setAllowancePosition(ObjectUtils.isEmpty(rowMap.get(10)) ? null : Convert.toBigDecimal(rowMap.get(10)));
            //浮动工资
            detail.setAllowanceFloating(ObjectUtils.isEmpty(rowMap.get(11)) ? null : Convert.toBigDecimal(rowMap.get(11)));
            //保密工资
            detail.setAllowanceConfidentiality(ObjectUtils.isEmpty(rowMap.get(12)) ? null : Convert.toBigDecimal(rowMap.get(12)));
            //交通补贴
            detail.setAllowanceTransportation(ObjectUtils.isEmpty(rowMap.get(13)) ? null : Convert.toBigDecimal(rowMap.get(13)));
            //特种作业证补贴
            detail.setAllowanceSpecialCertificate(ObjectUtils.isEmpty(rowMap.get(14)) ? null : Convert.toBigDecimal(rowMap.get(14)));
            //节假日补贴
            detail.setAllowanceHoliday(ObjectUtils.isEmpty(rowMap.get(15)) ? null : Convert.toBigDecimal(rowMap.get(15)));
            //工作表现奖
            detail.setAllowancePerformance(ObjectUtils.isEmpty(rowMap.get(16)) ? null : Convert.toBigDecimal(rowMap.get(16)));
            //其它应发小计
            detail.setAllowanceSubtotal(ObjectUtils.isEmpty(rowMap.get(25)) ? null : Convert.toBigDecimal(rowMap.get(25)));
            //应发金额
            detail.setTotalEarnings(ObjectUtils.isEmpty(rowMap.get(26)) ? null : Convert.toBigDecimal(rowMap.get(26)));
            //违纪扣款
            detail.setDeductionDiscipline(ObjectUtils.isEmpty(rowMap.get(27)) ? null : Convert.toBigDecimal(rowMap.get(27)));
            //个人所得税
            detail.setDeductionTax(ObjectUtils.isEmpty(rowMap.get(28)) ? null : Convert.toBigDecimal(rowMap.get(28)));
            //代扣公积金
            detail.setDeductionHousingFund(ObjectUtils.isEmpty(rowMap.get(29)) ? null : Convert.toBigDecimal(rowMap.get(29)));
            //代扣代缴保险
            detail.setDeductionInsurance(ObjectUtils.isEmpty(rowMap.get(30)) ? null : Convert.toBigDecimal(rowMap.get(30)));
            //暂扣工资
            // detail.setDeductionWithhold(ObjectUtils.isEmpty(rowMap.get(30)) ? null : Convert.toBigDecimal(rowMap.get(30)));
            //应扣小计
            detail.setDeductionSubtotal(ObjectUtils.isEmpty(rowMap.get(31)) ? null : Convert.toBigDecimal(rowMap.get(31)));
            //实发金额
            detail.setNetSalary(ObjectUtils.isEmpty(rowMap.get(32)) ? null : Convert.toBigDecimal(rowMap.get(32)));
            details.add(detail);
        }
        return details;
    }
}
