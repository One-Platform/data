/*
MySQL Backup
Source Server Version: 5.0.24
Source Database: mini-web
Date: 2012-9-10 20:35:32
*/


-- ----------------------------
--  Table structure for `t_check_boolean`
-- ----------------------------
DROP TABLE IF EXISTS `t_check_boolean`;
CREATE TABLE `t_check_boolean` (
  `ID` varchar(32) NOT NULL,
  `BOOLEAN_INT` int(11) default NULL,
  `BOOLEAN_STR` varchar(20) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_code_gender`
-- ----------------------------
DROP TABLE IF EXISTS `t_code_gender`;
CREATE TABLE `t_code_gender` (
  `ID` varchar(3) NOT NULL,
  `NAME` varchar(10) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_code_group`
-- ----------------------------
DROP TABLE IF EXISTS `t_code_group`;
CREATE TABLE `t_code_group` (
  `ID` varchar(3) NOT NULL,
  `NAME` varchar(10) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(200) default NULL,
  `AGE` int(11) default NULL,
  `BIRTHDAY` date default NULL,
  `MONEY` bigint(20) default NULL,
  `GENDER` varchar(3) default NULL,
  `GROUPIDS` varchar(3) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fk_gender_id` (`GENDER`),
  KEY `fk_group_id` (`GROUPIDS`),
  CONSTRAINT `fk_group_id` FOREIGN KEY (`GROUPIDS`) REFERENCES `t_code_group` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_gender_id` FOREIGN KEY (`GENDER`) REFERENCES `t_code_gender` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `T_PROCEDURE_INSERT_MODEL`;
CREATE TABLE `T_PROCEDURE_INSERT_MODEL` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(200) default NULL,
  `CREATETIME` date default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ----------------------------
--  Records 
-- ----------------------------
INSERT INTO `t_code_gender` VALUES ('0','女'),('1','男'),('3', 'g3'),('4', 'g4'),
('5', 'g5'),('6', 'g6'),('7', 'g7'),('8', 'g8'),('9', 'g9');

-- ----------------------------
--  Procedure
-- ----------------------------
CREATE PROCEDURE test_02( newid char(10))
begin
	select * from t_code_group where id = newid;
end;

CREATE PROCEDURE `test_03`()
begin
	select * from t_code_group;
	select * from t_user;
	select * from t_code_group;
	select name from t_code_group;
end;


CREATE PROCEDURE `test_04`(in newid VARCHAR(10),out out1 VARCHAR(10),out out2 VARCHAR(10))
BEGIN
	SELECT * from t_code_group;
	SELECT id into out1 FROM t_code_group  WHERE id = newid;
	SELECT name into out2 FROM t_code_group  WHERE id = newid;
END;

CREATE PROCEDURE `teststring`(IN `para1` varchar(10),OUT `para2` varchar(10))
BEGIN
	SELECT name into para2 FROM T_CODE_GROUP WHERE id = para1;
END;

CREATE OR REPLACE PROCEDURE `testinsert`(IN `ID` varchar(32), IN `NAME` varchar(200),  IN `CREATETIME` DATETIME)
BEGIN
  INSERT INTO T_PROCEDURE_INSERT_MODEL values (ID,NAME, CREATETIME);
END;
