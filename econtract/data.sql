insert into "user" (userid, password, name, email, companyid)
values ('request1', '$2a$10$cYdFZoku5bOUGwYEL.Tk5e6fFjf1V0SQj5v98OdeO8UFPWI5pVK4y', '오라클', 'request@oracle.com' ,'company1');

insert into "user" (userid, password, name, email, companyid)
values ('approve1', '$2a$10$cYdFZoku5bOUGwYEL.Tk5e6fFjf1V0SQj5v98OdeO8UFPWI5pVK4y', '오라클', 'approve@oracle.com' ,'company1');

insert into "user" (userid, password, name, email, companyid)
values ('admin', '$2a$10$cYdFZoku5bOUGwYEL.Tk5e6fFjf1V0SQj5v98OdeO8UFPWI5pVK4y', '어드민', 'admin@oracle.com' ,'company1');

insert into "status" (code, context) 
values (1, '계약서 생성(요청자)');

insert into "status" (code, context) 
values (2, '승인 메일 발송');

insert into "status" (code, context) 
values (3, '승인자 승인 완료');

insert into "status" (code, context) 
values (4, '계약서 합의 완료');
