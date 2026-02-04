package com.ruoyi.kangderui.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.exception.ServiceException;
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

    /**
     * 导入员工工资明细数据
     *
     * @param detailList 员工工资明细列表
     * @param operName 操作用户
     * @return 结果信息
     */
    @Override
    public String importSalaryDetail(List<SalaryDetail> detailList, String operName)
    {
        if (detailList == null || detailList.isEmpty())
        {
            throw new ServiceException("导入员工工资明细数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SalaryDetail detail : detailList)
        {
            try
            {
                detail.setCreateBy(operName);
                insertSalaryDetail(detail);
                successNum++;
                successMsg.append("<br/>").append(successNum).append("、姓名 ").append(detail.getNickName()).append(" 导入成功");
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、姓名 " + detail.getNickName() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        return successMsg.toString();
    }
}
