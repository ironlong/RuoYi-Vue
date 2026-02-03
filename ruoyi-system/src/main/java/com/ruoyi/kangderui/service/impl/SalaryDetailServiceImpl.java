package com.ruoyi.kangderui.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.kangderui.mapper.SalaryDetailMapper;
import com.ruoyi.kangderui.domain.SalaryDetail;
import com.ruoyi.kangderui.service.ISalaryDetailService;

/**
 * 员工工资明细Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-02-02
 */
@Service
public class SalaryDetailServiceImpl implements ISalaryDetailService 
{
    @Autowired
    private SalaryDetailMapper salaryDetailMapper;

    /**
     * 查询员工工资明细
     * 
     * @param salaryDetailId 员工工资明细主键
     * @return 员工工资明细
     */
    @Override
    public SalaryDetail selectSalaryDetailBySalaryDetailId(Long salaryDetailId)
    {
        return salaryDetailMapper.selectSalaryDetailBySalaryDetailId(salaryDetailId);
    }

    /**
     * 查询员工工资明细列表
     * 
     * @param salaryDetail 员工工资明细
     * @return 员工工资明细
     */
    @Override
    public List<SalaryDetail> selectSalaryDetailList(SalaryDetail salaryDetail)
    {
        return salaryDetailMapper.selectSalaryDetailList(salaryDetail);
    }

    /**
     * 新增员工工资明细
     * 
     * @param salaryDetail 员工工资明细
     * @return 结果
     */
    @Override
    public int insertSalaryDetail(SalaryDetail salaryDetail)
    {
        salaryDetail.setCreateTime(DateUtils.getNowDate());
        return salaryDetailMapper.insertSalaryDetail(salaryDetail);
    }

    /**
     * 修改员工工资明细
     * 
     * @param salaryDetail 员工工资明细
     * @return 结果
     */
    @Override
    public int updateSalaryDetail(SalaryDetail salaryDetail)
    {
        salaryDetail.setUpdateTime(DateUtils.getNowDate());
        return salaryDetailMapper.updateSalaryDetail(salaryDetail);
    }

    /**
     * 批量删除员工工资明细
     * 
     * @param salaryDetailIds 需要删除的员工工资明细主键
     * @return 结果
     */
    @Override
    public int deleteSalaryDetailBySalaryDetailIds(Long[] salaryDetailIds)
    {
        return salaryDetailMapper.deleteSalaryDetailBySalaryDetailIds(salaryDetailIds);
    }

    /**
     * 删除员工工资明细信息
     * 
     * @param salaryDetailId 员工工资明细主键
     * @return 结果
     */
    @Override
    public int deleteSalaryDetailBySalaryDetailId(Long salaryDetailId)
    {
        return salaryDetailMapper.deleteSalaryDetailBySalaryDetailId(salaryDetailId);
    }
}
