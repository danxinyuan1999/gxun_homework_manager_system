export function listPaper(userId, examId) {
  return userId('/exam/api/paper/paper/paging', { current: 1, size: 5, params: { userId: userId, examId: examId }})
}
