-- ----------------------------
--  Table structure for 't_check_boolean'
-- ----------------------------
CREATE TABLE t_check_boolean (
  ID varchar(32) NOT NULL,
  BOOLEAN_INT int default NULL,
  BOOLEAN_STR varchar(20) default NULL,
  PRIMARY KEY  (ID)
) ;

-- ----------------------------
--  Table structure for 't_code_gender'
-- ----------------------------
CREATE TABLE t_code_gender (
  ID varchar(3) NOT NULL,
  NAME varchar(10) default NULL,
  PRIMARY KEY  (ID)
);

-- ----------------------------
--  Table structure for 't_code_group'
-- ----------------------------
CREATE TABLE t_code_group (
  ID varchar(3) NOT NULL,
  NAME varchar(10) default NULL,
  PRIMARY KEY  (ID)
);

-- ----------------------------
--  Table structure for 't_user'
-- ----------------------------
CREATE TABLE t_user (
  ID varchar(32) NOT NULL,
  NAME varchar(200) default NULL,
  AGE int default NULL,
  BIRTHDAY datetime default NULL,
  MONEY bigint default NULL,
  GENDER varchar(3) default NULL,
  GROUPIDS varchar(3) default NULL,
  PRIMARY KEY  (ID),
  CONSTRAINT fk_group_id FOREIGN KEY (GROUPIDS) REFERENCES t_code_group (ID) ,
  CONSTRAINT fk_gender_id FOREIGN KEY (GENDER) REFERENCES t_code_gender (ID) 
) ;

-- ----------------------------
--  Table structure for 'T_PROCEDURE_INSERT_MODEL'
-- ----------------------------
CREATE TABLE T_PROCEDURE_INSERT_MODEL (
  ID varchar(32) NOT NULL,
  NAME varchar(200) default NULL,
  CRATETIME datetime default NULL,
  PRIMARY KEY  (ID)
) ;
-- ----------------------------
--  Records 
-- ----------------------------
INSERT INTO t_code_gender VALUES ("0","女"),('1','男'),('3', 'g3'),('4', 'g4'),
('5', 'g5'),('6', 'g6'),('7', 'g7'),('8', 'g8'),('9', 'g9');

-- ----------------------------
--  Procedure
-- ----------------------------
CREATE proc [dbo].[test_02]
@PARA1 varCHAR(20)
as
select * from t_code_group where id = @PARA1;

CREATE proc [dbo].[test_03]
as
select * from t_code_group;
select * from t_user;
select * from t_code_group;
select name from t_code_group;


create PROCEDURE [dbo].[test_04]
	@PARA1 varCHAR(20),
	@PARA2 varchar(20) OUTPUT,
	@PARA3 varchar(20) OUTPUT
AS
BEGIN
	SET NOCOUNT ON;
	SELECT * from t_code_group;
	SELECT @PARA2 = id FROM T_CODE_GROUP WHERE id = @PARA1;
	SELECT @PARA3 = name FROM T_CODE_GROUP WHERE id = @PARA1;
END;

CREATE PROCEDURE [dbo].[TESTSTRING]
	@PARA1 varCHAR(20),
	@PARA2 varchar(20) OUTPUT
AS
BEGIN
	SET NOCOUNT ON;
	SELECT @PARA2 = name FROM T_CODE_GROUP WHERE id = @PARA1;
END;

CREATE PROCEDURE [dbo].[TESTINSERT]
	@ID varchar(20),
	@NAME varchar(200),
	@CREATETIME datetime
AS
BEGIN
	SET NOCOUNT ON;
  INSERT INTO T_PROCEDURE_INSERT_MODEL values (ID,NAME, CREATETIME);
END;

