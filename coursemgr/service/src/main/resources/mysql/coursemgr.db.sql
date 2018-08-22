
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
	`serialNo` varchar(10) NOT NULL, #学号或者教工号，管理员登录账号
	`password` varchar(20) DEFAULT NULL,
    `name` varchar(20) NOT NULL,  #姓名
	`college` varchar(50),            #学院
	`profession` varchar(50),         #专业
	`cellphone` varchar(11),          #手机号
	`email` varchar(50),              #邮箱
	`roles` varchar(50),   #所拥有的角色,存储角色id，每个id之间‘,’分隔
    `sex` ENUM('男', '女') NOT NULL default '男',     #性别  男，女
	`createDate` datetime
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
	`name` varchar(20) NOT NULL,     #显示名称
	`value` varchar(30) NOT NULL		 #枚举值
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,  #课程名称
	`credit` INT,                #学分
	`description` LONGTEXT,
	`creatorNo` varchar(10),     #创建者编号-教师职工号
	`createDate` datetime,
	`groupingType`  varchar(15)   #分组类型  默认一个分组？
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `course_students`;
CREATE TABLE `course_students` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `courseId` INT NOT NULL,  #课程id
	`studentNo` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `course_tasks`;
CREATE TABLE `course_tasks` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `courseId` INT NOT NULL,  #课程id
	`name` varchar(20) NOT NULL,    #任务名称
	`weight` float,        #任务权重
	`publishTime` datetime,        #发布时间
	`deadline` datetime,             #截止时间
	`totalScore` INT              #总分
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `question_type`;
CREATE TABLE `question_type` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
	`sortNo` INT,    #用于排序
  `name` varchar(15) UNIQUE  #题目类型，单选题，多选题，判断题，主观题等
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `task_questions`;
CREATE TABLE `task_questions` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `taskId` INT NOT NULL,  #任务id
	`questionNo` INT,         #试题题号
	`stems`  varchar(255),  #题干
	`questionType`  varchar(15),   # 题目类型  单选题，多选题，判断题，主观题等
	`score` FLOAT,          #分数
	`options` LONGTEXT,        #选择题的选项(每个选项之间以#号分隔，前端需校验选项的输入，不能输入#)，主观题则为空
	`answers` varchar(15),      #标准答案，如果是多选题，每个选项之间以;分隔,；主观题无标准答案;判断题为正确或错误
	`answerAttachment` varchar(255),   #主观题答案附件地址
	`markType` varchar(15)     #主观题评分类型，自评，组内互评，组间互评
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `student_tasks`;
CREATE TABLE `student_tasks` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
	`studentNo` varchar(10),    #学号 关联user serialNo
  `taskId` varchar(15),  #任务id
	`status` ENUM('完成', '带审阅', '未提交') NOT NULL default '未提交',
	`score` FLOAT,       #分数
	`submitTime` datetime     #提交时间或完成时间
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `student_paper`;
CREATE TABLE `student_paper` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
	`studentNo` varchar(10),    #学号 关联user serialNo
  `questionId` BIGINT,  #试题id
	`answers`  varchar(1000),
	`score` FLOAT default 0     #得分
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
	`groupLeaderNo` varchar(10),    #组长  关联user serialNo
  `courseId` INT NOT NULL,  #课程id
	`createDate` datetime
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `group_memeber`;
CREATE TABLE `group_memeber` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
	`groupId` INT,    #所属分组id
  `studentNo` varchar(10)    #组内成员  关联user serialNo
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

