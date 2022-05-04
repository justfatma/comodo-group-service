insert into todo_user (id, name, surname, email, password) values (1000, 'Kerem','Kirac', 'test123@gmail.com', 'password123' );
insert into todo_user (id, name, surname, email, password) values (1001, 'Alper','Yavuz', 'test456@gmail.com', 'password456' );

insert into todo_group (id, name, todo_user_id) values (10000, 'Home', 1000);
insert into todo_group (id, name, todo_user_id) values (10001, 'Kids', 1000);
insert into todo_group (id, name, todo_user_id) values (10002, 'Work', 1001);



