import { post } from '@/utils/request'

export function fetchDetail(id) {
  return post('/exam/api/exam/exam/detail', { id: id })
}

export function saveData(data) {
  return post('/exam/api/exam/exam/save', data)
}

export function fetchList() {
  return post('/exam/api/exam/exam/paging', { current: 1, size: 100 })
}
