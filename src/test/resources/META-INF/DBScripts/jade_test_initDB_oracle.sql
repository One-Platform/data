-- Create table T_CODE_GENDER
create table T_CODE_GENDER
(
  ID   VARCHAR2(3) primary key,
  NAME VARCHAR2(10)
);
-- insert init data for T_CODE_GENDER
insert into T_CODE_GENDER (ID, NAME) values ('1','男');
insert into T_CODE_GENDER (ID, NAME) values ('0','女');
insert into T_CODE_GENDER (ID, NAME) values ('3', 'g3');
insert into T_CODE_GENDER (ID, NAME) values ('4', 'g4');
insert into T_CODE_GENDER (ID, NAME) values ('5', 'g5');
insert into T_CODE_GENDER (ID, NAME) values ('6', 'g6');
insert into T_CODE_GENDER (ID, NAME) values ('7', 'g7');
insert into T_CODE_GENDER (ID, NAME) values ('8', 'g8');
insert into T_CODE_GENDER (ID, NAME) values ('9', 'g9');
-- Create table T_CODE_GROUP
create table T_CODE_GROUP
(
  ID   VARCHAR2(3) primary key,
  NAME VARCHAR2(10)
);
-- Create table T_CHECK_BOOLEAN
create table T_CHECK_BOOLEAN
(
  ID          VARCHAR2(32) primary key,
  BOOLEAN_INT INTEGER,
  BOOLEAN_STR VARCHAR2(20)
);
-- Create table T_USER
create table T_USER
(
  ID       VARCHAR2(32) primary key,
  NAME     VARCHAR2(200),
  AGE      INTEGER,
  BIRTHDAY DATE,
  MONEY    LONG,
  GENDER   VARCHAR2(3) CONSTRAINT fk_gender_id  REFERENCES  t_code_gender(id),
  GROUPIDS VARCHAR2(300) CONSTRAINT fk_group_id  REFERENCES  t_code_group(id)
);

-- Create table T_PROCEDURE_INSERT_MODEL
create table T_PROCEDURE_INSERT_MODEL
(
  ID       VARCHAR2(32) primary key,
  NAME     VARCHAR2(200),
  CREATETIME DATE
);


-- ----------------------------
--  Procedure
-- ----------------------------
CREATE OR REPLACE PROCEDURE TESTSTRING(PARA1 IN VARCHAR2,PARA2 OUT VARCHAR2)  IS
BEGIN
   SELECT name INTO PARA2 FROM T_CODE_GROUP WHERE id = PARA1;
END TESTSTRING;


CREATE OR REPLACE PROCEDURE test_02(
    PARA1 IN VARCHAR2,
    cur_arg out sys_refcursor
)
is
begin
    open cur_arg for select * from t_code_group where id = PARA1;
end test_02;


CREATE OR REPLACE PROCEDURE test_03(
    cur_arg1 out sys_refcursor,
    cur_arg2 out sys_refcursor,
    cur_arg3 out sys_refcursor,
    cur_arg4 out sys_refcursor
)
is
begin
    open cur_arg1 for select * from t_code_group ;
    open cur_arg2 for select * from t_user ;
    open cur_arg3 for select * from t_code_group ;
    open cur_arg4 for select name from t_code_group ;
end test_03;

CREATE OR REPLACE PROCEDURE testinsert(ID IN VARCHAR2(32), NAME IN VARCHAR2(200), CREATETIME IN DATE)  IS
  BEGIN
    INSERT INTO T_PROCEDURE_INSERT_MODEL values (ID,NAME, CREATETIME);
  END testinsert;
