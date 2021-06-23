import { post } from '@/utils/request'

export function fetchDetail(data) {
  return post('/exam/api/repo/detail', data)
}

export function saveData(data) {
  return post('/exam/api/repo/save', data)
}

export function fetchList(data) {
  return post('/exam/api/repo/list', data)
}

export function batchAction(data) {
  return post('/exam/api/repo/batch-action', data)
}
