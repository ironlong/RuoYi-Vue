import request from '@/utils/request'

// 查询员工工资明细列表
export function listDetail(query) {
  return request({
    url: '/kangderui/detail/list',
    method: 'get',
    params: query
  })
}

// 查询员工工资明细详细
export function getDetail(salaryDetailId) {
  return request({
    url: '/kangderui/detail/' + salaryDetailId,
    method: 'get'
  })
}

// 新增员工工资明细
export function addDetail(data) {
  return request({
    url: '/kangderui/detail',
    method: 'post',
    data: data
  })
}

// 修改员工工资明细
export function updateDetail(data) {
  return request({
    url: '/kangderui/detail',
    method: 'put',
    data: data
  })
}

// 删除员工工资明细
export function delDetail(salaryDetailId) {
  return request({
    url: '/kangderui/detail/' + salaryDetailId,
    method: 'delete'
  })
}
