drop database HospitalSurvey;
create database HospitalSurvey DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
use HospitalSurvey;

# reserved table
# now we store appid and secret in code
# should store those info in db
CREATE TABLE `wechatAccount` (
  `aid` int(11) NOT NULL,
  `appID` varchar(20) UNIQUE NOT NULL,
  `appSecret` varchar(30) NOT NULL,
  PRIMARY KEY (`aid`)
);

CREATE TABLE `hospital` (
  `aid` int(11) NOT NULL,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`aid`)
);

CREATE TABLE `doctor` (
  `aid` int(11) NOT NULL,
  `username`  varchar(20) BINARY NOT NULL,
  `name` varchar(20) DEFAULT NULL,
  `pwd` varchar(64) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `hospitalID` int(11) NOT NULL,
  `state` int(2) DEFAULT '1',
  PRIMARY KEY (`aid`),
  CONSTRAINT  FOREIGN KEY (`hospitalID`) REFERENCES `hospital` (`aid`)
);

CREATE TABLE `Authorization` (
  `aid` int(11) NOT NULL,
  `surveySet` int(2) DEFAULT '0',
  `patientSet` int(2) DEFAULT '0',
  `deliverySet` int(2) DEFAULT '0',
  `typeSet` int(2) DEFAULT '0',
  `retrieveSet` int(2) DEFAULT '0',
  `forfeitSet` int(2) DEFAULT '0',
  `sysSet` int(2) DEFAULT '0',
  `superSet` int(2) DEFAULT '0',
  PRIMARY KEY (`aid`),
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `doctor` (`aid`)
);

CREATE TABLE `PatientType` (
  `patientTypeId` int(11) NOT NULL,
  `patientTypeName` varchar(255) NOT NULL,
  `maxNum` int(5) NOT NULL,
  `bday` int(5) NOT NULL,
  `penalty` double NOT NULL,
  `resendDays` int(5) NOT NULL,
  PRIMARY KEY (`patientTypeId`)
);

CREATE TABLE `Patient` (
  `patientId` varchar(255) NOT NULL,
  `pwd` varchar(64) DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  `appID` varchar(28) NOT NULL,
  `openID` varchar(50) UNIQUE NOT NULL,
  `uniqID` varchar(50) UNIQUE DEFAULT NULL,
  `outpatientID` varchar(50) UNIQUE DEFAULT NULL,  # 预留门诊号
  `inpatientID` varchar(50) UNIQUE DEFAULT NULL,   # 预留住院号
  `sex` varchar(1) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `birthday` datetime NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `patientTypeId` int(11) DEFAULT NULL,
  `aid` int(11) DEFAULT NULL,
  PRIMARY KEY (`patientId`),
  CONSTRAINT  FOREIGN KEY (`patientTypeId`) REFERENCES `PatientType` (`patientTypeId`) ON DELETE SET NULL,
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `doctor` (`aid`)
);

CREATE TABLE `surveyType` (
  `typeId` int(11) NOT NULL,
  `typeName` varchar(20) NOT NULL,
  PRIMARY KEY (`typeId`)
);

CREATE TABLE `survey` (
  `surveyId` int(11) NOT NULL,
  `surveyName` varchar(20) NOT NULL,
  `author` varchar(20) DEFAULT  NULL,
  `num` int(11) NOT NULL,
  `currentNum` int(5) NOT NULL,
  `department` varchar(20) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `putdate` datetime DEFAULT NULL,
  `typeId` int(11) DEFAULT NULL,
  `aid` int(11) DEFAULT NULL,
  PRIMARY KEY (`surveyId`),
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `doctor` (`aid`),
  CONSTRAINT  FOREIGN KEY (`typeId`) REFERENCES `surveyType` (`typeId`) ON DELETE SET NULL
) ;

CREATE TABLE `Question` (
  `questionId` int(11) NOT NULL AUTO_INCREMENT,
  `surveyId` int(11) NOT NULL,
  `textChoice` int(1) DEFAULT 0,
  `textChoiceContent` varchar(255) DEFAULT NULL,
  `questionContent` varchar(255) NOT NULL,
  `questionType` int(3) NOT NULL, /*1-'multi_choice', 2-'single_choice',3-'text'*/
  `aid` int(11) NOT NULL,
  PRIMARY KEY (`questionId`),
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `doctor` (`aid`),
  CONSTRAINT  FOREIGN KEY (`surveyId`) REFERENCES `survey` (`surveyId`) ON DELETE CASCADE
);

CREATE TABLE `deliveryInfo` (
  `deliveryId` int(11) NOT NULL,
  `deliveryDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `overday` int(11) DEFAULT '0',
  `state` int(2) DEFAULT '0',
  `patientId` varchar(255) DEFAULT NULL,
  `surveyId` int(11) DEFAULT NULL,
  `penalty` double NOT NULL,
  `aid` int(11) DEFAULT NULL,
  PRIMARY KEY (`deliveryId`),
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `doctor` (`aid`),
  CONSTRAINT  FOREIGN KEY (`patientId`) REFERENCES `Patient` (`patientId`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`surveyId`) REFERENCES `survey` (`surveyId`) ON DELETE CASCADE
);

CREATE TABLE `RetrieveInfo` (
  `deliveryId` int(11) NOT NULL,
  `patientId` varchar(255) NOT NULL,
  `surveyId` int(11) NOT NULL,
  `retrieveDate` datetime DEFAULT NULL,
  `aid` int(11) NOT NULL,
  PRIMARY KEY (`deliveryId`),
  CONSTRAINT  FOREIGN KEY (`deliveryId`) REFERENCES `deliveryInfo` (`deliveryId`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`surveyId`) REFERENCES `survey` (`surveyId`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`patientId`) REFERENCES `Patient` (`patientId`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `doctor` (`aid`)
);

CREATE TABLE `Answer` (
  `answerId` int(11) NOT NULL AUTO_INCREMENT,
  `deliveryId` int(11) NOT NULL,
  `questionId` int(11) NOT NULL,
  `surveyId` int(11) NOT NULL,
  `patientId` varchar(255) NOT NULL,
  `choiceContent` varchar(255) NOT NULL,
  `aid` int(11) NOT NULL,
  PRIMARY KEY (`answerId`),
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `doctor` (`aid`),
  CONSTRAINT  FOREIGN KEY (`deliveryId`) REFERENCES `deliveryInfo` (`deliveryId`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`questionId`) REFERENCES `Question` (`questionId`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`patientId`) REFERENCES `Patient` (`patientId`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`surveyId`) REFERENCES `survey` (`surveyId`) ON DELETE CASCADE
);

CREATE TABLE `Choice` (
  `choiceId` int(11) NOT NULL AUTO_INCREMENT,
  `answerId` int(11) NOT NULL DEFAULT 0,
  `questionId` int(11) NOT NULL DEFAULT 0,
  `score` int(11) NOT NULL,
  `choiceContent` varchar(255) NOT NULL,
  `aid` int(11) NOT NULL,
  PRIMARY KEY (`choiceId`),
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `doctor` (`aid`)
);

CREATE TABLE `ForfeitInfo` (
  `deliveryId` int(11) NOT NULL,
  `forfeit` double DEFAULT NULL,
  `isPay` int(2) DEFAULT '0',
  `aid` int(11) DEFAULT NULL,
  PRIMARY KEY (`deliveryId`),
  CONSTRAINT  FOREIGN KEY (`deliveryId`) REFERENCES `deliveryInfo` (`deliveryId`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `doctor` (`aid`)
);

INSERT INTO wechatAccount VALUES(0, "huxitianshi", "appSecret");


INSERT INTO hospital VALUES(0, "潮峻科技");
INSERT INTO hospital VALUES(1, "上海儿童医学中心");
INSERT INTO hospital VALUES(2, "金杨社区卫生服务中心");
INSERT INTO hospital VALUES(3, "泥城社区卫生服务中心");
INSERT INTO hospital VALUES(4, "高桥社区卫生服务中心");
INSERT INTO hospital VALUES(5, "大团社区卫生服务中心");
INSERT INTO hospital VALUES(6, "陆家嘴社区卫生服务中心");
INSERT INTO hospital VALUES(7, "沪东社区卫生服务中心");
INSERT INTO hospital VALUES(8, "北蔡社区卫生服务中心");
INSERT INTO hospital VALUES(9, "合庆社区卫生服务中心");
INSERT INTO hospital VALUES(10, "三林社区卫生服务中心");
INSERT INTO hospital VALUES(12, "联洋社区卫生服务中心");
INSERT INTO hospital VALUES(13, "塘桥社区卫生服务中心");
INSERT INTO hospital VALUES(14, "惠南社区卫生服务中心");
INSERT INTO hospital VALUES(15, "祝桥社区卫生服务中心");
INSERT INTO hospital VALUES(16, "洋泾社区卫生服务中心");
INSERT INTO hospital VALUES(17, "迎博社区卫生服务中心");
INSERT INTO hospital VALUES(18, "潍坊社区卫生服务中心");


INSERT INTO PatientType VALUES(1,"病人",10,30,1,2);
INSERT INTO PatientType VALUES(2,"病人家属",10,30,1,2);

INSERT INTO surveyType VALUES(1,"第一类");
INSERT INTO surveyType VALUES(2,"2nd");

INSERT INTO doctor VALUES(1,"admin","张三","admin","13547865412",1, 1);
INSERT INTO doctor VALUES(2,"d1","张一三","d1","13547865412",1, 0);
INSERT INTO doctor VALUES(5,"doctor","张二三","doctor","13547865412",1, 1);
INSERT INTO doctor VALUES(6,"lht","lht","lht","13547865412",0, 1);

INSERT INTO Patient VALUES(1,"123456","李四","appid","p1","uniqid1","outpatientid1", "inpatientid1","1","13567891234","123@abc.com", "2016-6-10","2017-06-25 00:00:00",1,2);
INSERT INTO Patient VALUES(2,"123456","赵六","appid","p2","uniqid2","outpatientid2","inpatientid2","0","13567891234","456@abc.com","2013-10-10","2017-03-01 00:00:00",1,6);

INSERT INTO Authorization VALUES(2,0,0,0,0,0,0,0,1);
INSERT INTO Authorization VALUES(1,0,0,0,0,0,0,0,1);
INSERT INTO Authorization VALUES(5,0,0,0,0,0,0,0,1);
INSERT INTO Authorization VALUES(6,1,1,1,1,1,1,1,1);

INSERT INTO survey VALUES(1,"survey1","admin",10,2,"a1","aaa","2017-06-25 00:00:00",1,2);
INSERT INTO survey VALUES(2,"survey2","doctor2",10,2,"a2","baa","2017-06-25 00:00:00",1,6);

INSERT INTO deliveryInfo VALUES(1,"2017-01-15 00:00:00","2017-03-04 00:00:00",0,0,1,1,1,6);
INSERT INTO deliveryInfo VALUES(2,"2017-02-15 00:00:00","2017-04-04 00:00:00",0,0,1,1,1,6);
INSERT INTO deliveryInfo VALUES(3,"2017-03-15 00:00:00","2017-05-04 00:00:00",0,0,1,1,1,6);
INSERT INTO deliveryInfo VALUES(4,"2017-04-15 00:00:00","2017-06-04 00:00:00",0,0,1,2,1,6);
INSERT INTO deliveryInfo VALUES(5,"2017-05-15 00:00:00","2017-07-04 00:00:00",0,0,1,2,1,6);
INSERT INTO deliveryInfo VALUES(6,"2017-06-15 00:00:00","2017-07-04 00:00:00",0,0,1,2,1,6);

INSERT INTO Question VALUES(1,1,0,"","第一个问题",1,6);
INSERT INTO Question VALUES(2,1,0,"","2nd question",2,6);
INSERT INTO Question VALUES(3,1,0,"","text question",1,6);
INSERT INTO Question VALUES(4,2,0,"","text question",1,6);

INSERT INTO Answer VALUES(1,1,1,1,1,"a2",6);
INSERT INTO Answer VALUES(2,1,3,1,1,"some text1",6);
INSERT INTO Answer VALUES(3,1,2,1,1,"a5",6);
INSERT INTO Answer VALUES(4,4,2,2,1,"a6",6);
INSERT INTO Answer VALUES(5,5,2,2,2,"some text2",6);

INSERT INTO Choice VALUES(1,1,1,1,"choice_1",6);
INSERT INTO Choice VALUES(2,1,1,2,"choice_2",6);
INSERT INTO Choice VALUES(3,2,3,3,"choice_3",6);
INSERT INTO Choice VALUES(4,2,2,4,"choice_4",6);
INSERT INTO Choice VALUES(5,3,2,5,"choice_5",6);
INSERT INTO Choice VALUES(6,4,2,6,"choice_6",6);
INSERT INTO Choice VALUES(7,5,3,7,"choice_7",6);

INSERT INTO RetrieveInfo VALUES(1,1,1,"2017-06-25 00:00:00",6);
INSERT INTO RetrieveInfo VALUES(2,1,1,"2017-05-25 00:00:00",6);
INSERT INTO RetrieveInfo VALUES(3,2,1,"2017-05-25 00:00:00",6);
INSERT INTO RetrieveInfo VALUES(4,2,1,"2017-06-25 00:00:00",6);
INSERT INTO RetrieveInfo VALUES(5,2,2,"2017-06-25 00:00:00",6);

