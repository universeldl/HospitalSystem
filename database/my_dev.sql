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
  `visible` BOOLEAN NOT NULL,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`aid`)
);

CREATE TABLE `doctor` (
  `aid` int(11) NOT NULL,
  `username`  varchar(20) BINARY NOT NULL,
  `name` varchar(20) DEFAULT NULL,
  `pwd` varchar(64) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `hospitalID` int(11) DEFAULT NULL,
  `state` int(2) DEFAULT '1',
  PRIMARY KEY (`aid`),
  CONSTRAINT  FOREIGN KEY (`hospitalID`) REFERENCES `hospital` (`aid`)
);

CREATE TABLE `Authorization` (
  `aid` int(11) NOT NULL,
  `surveySet` int(2) DEFAULT 0,
  `patientSet` int(2) DEFAULT 0,
  `deliverySet` int(2) DEFAULT 0,
  `typeSet` int(2) DEFAULT 0,
  `retrieveSet` int(2) DEFAULT 0,
  `forfeitSet` int(2) DEFAULT 0,
  `planSet` int(2) DEFAULT 0,
  `sysSet` int(2) DEFAULT 0,
  `superSet` int(2) DEFAULT 0,
  PRIMARY KEY (`aid`),
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `doctor` (`aid`)
);

CREATE TABLE `PatientType` (
  `patientTypeId` int(11) NOT NULL,
  `patientTypeName` varchar(255) NOT NULL,
  PRIMARY KEY (`patientTypeId`)
);

CREATE TABLE `Plan` (
  `planId` int(11) NOT NULL,
  `beginAge` int(2) DEFAULT 0,
  `endAge` int(2) NOT NULL,
  `oldPatient` int(1) DEFAULT NULL, /*1-新病例，2-既往病例，3-哮喘无忧APP用户， 4-不限 */
  `active` int(1) DEFAULT 1,
  `sex` varchar(1) DEFAULT 1,/*1-男，2-女, 3-不限*/
  `patientTypeId` int(11) DEFAULT NULL,
  `aid` int(11) DEFAULT NULL,
  PRIMARY KEY (`planId`),
  CONSTRAINT  FOREIGN KEY (`patientTypeId`) REFERENCES `PatientType` (`patientTypeId`) ON DELETE SET NULL,
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `doctor` (`aid`)
);

CREATE TABLE `Patient` (
  `patientId` varchar(255) NOT NULL,
  `pwd` varchar(64) DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  `appID` varchar(28) DEFAULT NULL,
  `openID` varchar(50) UNIQUE NOT NULL,
  `uniqID` varchar(50) UNIQUE DEFAULT NULL,
  `outpatientID` varchar(50) UNIQUE DEFAULT NULL,  # 预留门诊号
  `inpatientID` varchar(50) UNIQUE DEFAULT NULL,   # 预留住院号
  `sex` varchar(1) NOT NULL,
  `oldPatient` varchar(1) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `patientTypeId` int(11) DEFAULT NULL,
  `planId` int(11) DEFAULT NULL,
  `addnDoctorId` int(11) DEFAULT NULL,
  `aid` int(11) DEFAULT NULL,
  PRIMARY KEY (`patientId`),
  CONSTRAINT  FOREIGN KEY (`patientTypeId`) REFERENCES `PatientType` (`patientTypeId`) ON DELETE SET NULL,
  CONSTRAINT  FOREIGN KEY (`planId`) REFERENCES `Plan` (`planId`) ON DELETE SET NULL,
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
  `num` int(5) DEFAULT 0,
  `bday` int(5) NOT NULL,
  `currentNum` int(5) DEFAULT 0,
  `department` varchar(20) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `putdate` datetime DEFAULT NULL,
  `typeId` int(11) DEFAULT NULL,
  `frequency` int(2) DEFAULT 0,
  `times` int(2) DEFAULT 0,
  `aid` int(11) DEFAULT NULL,
  `sendOnRegister` BOOLEAN NOT NULL,
  PRIMARY KEY (`surveyId`),
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `doctor` (`aid`),
  CONSTRAINT  FOREIGN KEY (`typeId`) REFERENCES `surveyType` (`typeId`) ON DELETE SET NULL
) ;

CREATE TABLE `plan_survey` ( /*plan与survey的ManyToMany中间表*/
  `planId` int(11) NOT NULL,
  `surveyId` int(11) NOT NULL,
  PRIMARY KEY (planId, surveyId),
  CONSTRAINT  FOREIGN KEY (`planId`) REFERENCES `Plan` (`planId`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`surveyId`) REFERENCES `survey` (`surveyId`) ON DELETE CASCADE
);

CREATE TABLE `Question` (
  `questionId` int(11) NOT NULL AUTO_INCREMENT,
  `sortId` int(11) NOT NULL,
  `surveyId` int(11) NOT NULL,
  `textChoice` int(1) DEFAULT 0,
  `questionContent` varchar(255) NOT NULL,
  `questionType` int(3) NOT NULL, /*1-'multi_choice', 2-'single_choice',3-'text', 4-date selector*/
  `aid` int(11) NOT NULL,
  `startAge` int(2) DEFAULT -1,
  `endAge` int(2) DEFAULT -1,
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
  `byDoctor` varchar(25) DEFAULT '',
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
  `textChoice` int(1) DEFAULT 0,
  `textChoiceContent` varchar(255) DEFAULT NULL,
  #`choiceContent` varchar(255) NOT NULL,
  `lastModified` varchar(25) DEFAULT '',
  `modifiedDate` varchar(25) DEFAULT NULL,
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
  `questionId` int(11) DEFAULT 0,
  `score` decimal(8,2) NOT NULL,
  `choiceContent` varchar(255) NOT NULL,
  `choiceImgPath` varchar(255) DEFAULT NULL,
  `aid` int(11) NOT NULL,
  PRIMARY KEY (`choiceId`),
  CONSTRAINT  FOREIGN KEY (`aid`) REFERENCES `doctor` (`aid`)
);

CREATE TABLE `answer_choice` ( /*answer与choice的ManyToMany中间表*/
  `answerId` int(11) NOT NULL,
  `choiceId` int(11) NOT NULL,
  PRIMARY KEY (answerId, choiceId),
  CONSTRAINT  FOREIGN KEY (`answerId`) REFERENCES `Answer` (`answerId`) ON DELETE CASCADE,
  CONSTRAINT  FOREIGN KEY (`choiceId`) REFERENCES `Choice` (`choiceId`) ON DELETE CASCADE
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

INSERT INTO hospital VALUES(0, FALSE, "潮峻科技");
INSERT INTO hospital VALUES(1,  TRUE, "上海儿童医学中心");
INSERT INTO hospital VALUES(2,  TRUE, "陆家嘴社区卫生服务中心");
INSERT INTO hospital VALUES(3,  TRUE, "北蔡社区卫生服务中心");
INSERT INTO hospital VALUES(4,  TRUE, "合庆社区卫生服务中心");
INSERT INTO hospital VALUES(5, TRUE,  "联洋社区卫生服务中心");
INSERT INTO hospital VALUES(6, TRUE,  "惠南社区卫生服务中心");
INSERT INTO hospital VALUES(7, TRUE,  "塘桥社区卫生服务中心");
INSERT INTO hospital VALUES(8,  TRUE, "高桥社区卫生服务中心");
INSERT INTO hospital VALUES(9, TRUE,  "三林社区卫生服务中心");
INSERT INTO hospital VALUES(10, TRUE,  "祝桥社区卫生服务中心");
INSERT INTO hospital VALUES(11,  TRUE, "金杨社区卫生服务中心");
INSERT INTO hospital VALUES(12,  TRUE, "沪东社区卫生服务中心");
INSERT INTO hospital VALUES(13, TRUE,  "洋泾社区卫生服务中心");
INSERT INTO hospital VALUES(14,  TRUE, "泥城社区卫生服务中心");
INSERT INTO hospital VALUES(15,  TRUE, "大团社区卫生服务中心");
INSERT INTO hospital VALUES(17, TRUE,  "迎博社区卫生服务中心");
INSERT INTO hospital VALUES(18, TRUE,  "潍坊社区卫生服务中心");

INSERT INTO PatientType VALUES(1,"哮喘");
INSERT INTO PatientType VALUES(2,"咳嗽");
INSERT INTO surveyType VALUES(1,"第一类");
INSERT INTO surveyType VALUES(2,"2nd");

INSERT INTO doctor VALUES(1,"admin","张三","ISMvKXpXpadDiUoOSoAfww==","13547865412",1, 1);
INSERT INTO doctor VALUES(2,"admin1","张三1","ISMvKXpXpadDiUoOSoAfww==","13547865412",1, 1);
INSERT INTO doctor VALUES(3,"d1","张一三","4QrcOUm6Wau+VuBX8g+IPg==","13547865412",1, 0);
INSERT INTO doctor VALUES(4,"doctor1","张三2","4QrcOUm6Wau+VuBX8g+IPg==","13547865412",2, 1);
INSERT INTO doctor VALUES(5,"doctor2","张三3","4QrcOUm6Wau+VuBX8g+IPg==","13547865412",3, 1);
INSERT INTO doctor VALUES(11,"doctor3","张三4","4QrcOUm6Wau+VuBX8g+IPg==","13547865412",4, 1);
INSERT INTO doctor VALUES(7,"doctor4","张三5","4QrcOUm6Wau+VuBX8g+IPg==","13547865412",5, 1);
INSERT INTO doctor VALUES(8,"doctor5","张三6","4QrcOUm6Wau+VuBX8g+IPg==","13547865412",6, 1);
INSERT INTO doctor VALUES(9,"doctor6","张三7","4QrcOUm6Wau+VuBX8g+IPg==","13547865412",7, 1);
INSERT INTO doctor VALUES(10,"doctor7","张三8","4QrcOUm6Wau+VuBX8g+IPg==","13547865412",8, 1);
INSERT INTO doctor VALUES(6,"lht","lht","3DOSGROTSOLR+Or8OgGwFQ==","13547865412",0, 1);


INSERT INTO Plan VALUES(1, 0, 2, 1, 1, 1, 1, 6);
INSERT INTO Plan VALUES(2, 2, 5, 2, 1, 2, 1, 6);
INSERT INTO Plan VALUES(3, 6, 9, 4, 1, 3, 1, 6);

INSERT INTO Patient VALUES(1,"123456","李四","appid","p1","uniqid1","outpatientid1", "inpatientid1","1", "1", "13567891234","123@abc.com", "2016-6-10","2017-12-15 00:00:00",1,1,3,2);
INSERT INTO Patient VALUES(2,"123456","赵六","appid","p2","uniqid2","outpatientid2","inpatientid2","0", "2", "13567891234","456@abc.com","2013-10-10","2017-03-01 00:00:00",1,2,3,6);
INSERT INTO Patient VALUES(3,"123456","测试","appid","oaBonw30UBjZkLW5rf19h7KunM7s","na",NULL,NULL,"0", "3", "13567891234","456@abc.com","2013-10-10","2017-03-01 00:00:00",1,2,3,6);

INSERT INTO Authorization VALUES(2,1,1,1,1,1,1,1,1,0);
INSERT INTO Authorization VALUES(1,0,0,0,0,0,0,0,0,1);
INSERT INTO Authorization VALUES(5,0,0,0,0,0,0,0,0,1);
INSERT INTO Authorization VALUES(6,1,1,1,1,1,1,1,1,1);


INSERT INTO survey VALUES(1,"survey1","admin",10,30, 2,"a1","aaa","2017-06-25 00:00:00",1,2,6,2, FALSE);
INSERT INTO survey VALUES(2,"survey2","doctor2",10,30, 2,"a2","baa","2017-06-25 00:00:00",1,3,6,6, FALSE);

INSERT INTO deliveryInfo VALUES(1,"2017-01-15 00:00:00","2017-03-04 00:00:00",0,-1,1,1,6);
INSERT INTO deliveryInfo VALUES(2,"2017-02-15 00:00:00","2018-04-04 00:00:00",0,0,1,1,6);
INSERT INTO deliveryInfo VALUES(3,"2017-03-15 00:00:00","2017-05-04 00:00:00",0,-1,2,1,6);
INSERT INTO deliveryInfo VALUES(4,"2017-04-15 00:00:00","2017-06-04 00:00:00",0,-1,2,1,6);
INSERT INTO deliveryInfo VALUES(5,"2017-05-15 00:00:00","2017-07-04 00:00:00",0,-1,2,2,6);
INSERT INTO deliveryInfo VALUES(6,"2017-06-15 00:00:00","2018-07-04 00:00:00",0,0,2,2,6);
INSERT INTO deliveryInfo VALUES(7,"2018-03-02 00:00:00","2018-08-04 00:00:00",30,0,3,1,6);

INSERT INTO Question VALUES(1,7,1,1,"第一个问题",1,6,-1, -1);
INSERT INTO Question VALUES(2,6,1,0,"2nd question",2,6,3,5);
INSERT INTO Question VALUES(3,5,1,1,"3rd question",1,6,-1,-1);
INSERT INTO Question VALUES(4,4,1,0,"4th question",2,6,-1,-1);
INSERT INTO Question VALUES(5,3,1,1,"text question",3,6,-1,-1);
INSERT INTO Question VALUES(6,2,1,1,"date question1",4,6,-1,-1);
INSERT INTO Question VALUES(7,1,1,1,"date question2",4,6,-1,-1);


INSERT INTO Answer VALUES(1,1,1,1,1,0,"","","2016-06-14 00:00:00",6);
INSERT INTO Answer VALUES(2,3,2,1,1,0,"","","2016-06-14 00:00:00",6);
INSERT INTO Answer VALUES(3,3,3,1,1,1,"dfjkldfjskljdfklkdljfskldf","","2017-02-04 00:00:00",6);
INSERT INTO Answer VALUES(4,4,2,2,1,0,"","张一三","2018-02-04 00:00:00",6);
INSERT INTO Answer VALUES(5,5,2,2,2,1,"sdfskljfalsdjfklsjkf haha","lht","2017-06-04 00:00:00",6);
INSERT INTO Answer VALUES(6,5,4,2,2,1,"sdfskljfalsdjfklsjkf haha","lht","2017-06-04 00:00:00",6);
INSERT INTO Answer VALUES(10,1,4,1,1,0,NULL,NULL,NULL,1);

INSERT INTO Choice VALUES(1,1,1,"choice_1","./img/survey/choice1.png",6);
INSERT INTO Choice VALUES(2,1,2,"choice_2","./img/survey/choice2.png",6);
INSERT INTO Choice VALUES(3,1,3,"choice_3","./img/survey/choice3.png",6);
INSERT INTO Choice VALUES(4,1,4,"choice_4","",6);
INSERT INTO Choice VALUES(5,4,5,"choice_5","",6);
INSERT INTO Choice VALUES(6,2,6,"choice_6","",6);
INSERT INTO Choice VALUES(7,3,7,"choice_7","",6);
INSERT INTO Choice VALUES(14,4,1,"choice_14","",6);
INSERT INTO Choice VALUES(15,4,2,"choice_15","",6);

INSERT INTO RetrieveInfo VALUES(1,1,1,"2017-06-25 00:00:00","张三",6);
INSERT INTO RetrieveInfo VALUES(3,2,1,"2017-05-25 00:00:00","",6);
INSERT INTO RetrieveInfo VALUES(4,2,1,"2017-06-25 00:00:00","",6);
INSERT INTO RetrieveInfo VALUES(5,2,2,"2017-06-25 00:00:00","",6);

INSERT INTO plan_survey VALUES(1, 1);
INSERT INTO plan_survey VALUES(1, 2);
INSERT INTO plan_survey VALUES(2, 1);

INSERT INTO answer_choice VALUES(1, 1);
INSERT INTO answer_choice VALUES(1, 2);
INSERT INTO answer_choice VALUES(2, 3);
INSERT INTO answer_choice VALUES(4, 4);
INSERT INTO answer_choice VALUES(5, 3);
INSERT INTO answer_choice VALUES(2, 5);
INSERT INTO answer_choice VALUES(3, 7);
INSERT INTO answer_choice VALUES(10, 14);
