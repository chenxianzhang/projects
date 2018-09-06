/**
 * 任务页面操作类型
 * operate --教师->拟题(T_statement)、传答案(T_uploadAnswer)、查看详情(T_viewDetail); 学生->查看详情(S_viewDetail)、答题(S_answer)
 * */
export const TASK_OPERATOR_TYPE = {
  TEACHER_STATEMENT:0,
  TEACHER_UPLOAD_ANSWER:1,
  TEACHER_VIEW_DETAIL:2,

  STUDENT_VIEW_DETAIL:3,
  STUDENT_ANSWER:4
};

export const SUBJECT_TYPE = {
  CHOOSE: 0,
  JUDGE: 1,
  SUBJECTIVE: 2
};
