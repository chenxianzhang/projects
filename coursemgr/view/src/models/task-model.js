export class Subject {
  no= '';//题目编号
  stem= '';//题干
  selections= [];//选项
  result= '';//答案
  score= 0;//分数
  edit='';//是否在编辑状态

  constructor(){
    this.no= '';//题目编号
    this.stem= '';//题干
    this.selections= ["选项一","选项二"];//选项
    this.result= '';//答案
    this.score= 0;//分数
    this.edit = true;

  }
};

export class Task {
  name = '';//任务名称
  weight = '';//权重
  inspireDate = '';//截止日期
  createDate= '';//创建日期
  publishDate= '';//发布日期
  totalScore= '';//总分
  subjects= [];//题目列表

  constructor(){
    this.name = '';//任务名称
    this.weight = '';//权重
    this.inspireDate = '';//截止日期
    this.createDate= '';//创建日期
    this.publishDate= '';//发布日期
    this.totalScore= '';//总分
    this.subjects= [];//题目列表
  }

};

