export class Selection{
  edit=false;
  id='';
  optionDes='';
  optionTag="";
  questionId='';

  constructor(cnt){
    this.optionDes = '选项' + cnt;
    this.edit = false;
    this.optionTag="";
    this.questionId='';
  }
}

export class Subject {
  id='';
  no= '';//题目编号
  stem= '';//题干
  selections= [];//选项
  score= 0;//分数
  edit='';//是否在编辑状态
  answer = '';//答案
  questionType='';//题目 类别

  constructor(){
    let selection = new Selection(1);
    this.id = '';
    this.no= '';//题目编号
    this.stem= '';//题干
    this.selections= [selection];//选项
    this.score= 0;//分数
    this.edit = false;
    this.answer = '';//答案
    this.questionType = '';
    this.teacherScore = '';
  }
};

export class Task {
  id='';
  name = '';//任务名称
  weight = '';//权重
  inspireDate = '';//截止日期
  createDate= '';//创建日期
  startDate= '';//开始日期
  publishDate= '';//发布日期
  totalScore= '';//总分
  subjects= [];//题目列表
  markType='';//评分方式--主观题

  constructor(){
    this.id= '';
    this.name = '';//任务名称
    this.weight = '';//权重
    this.inspireDate = '';//截止日期
    this.createDate= '';//创建日期
    this.startDate= '';//开始日期
    this.publishDate= '';//发布日期
    this.totalScore= '';//总分
    this.subjects= [];//题目列表
    this.markType = '';
  }

};

