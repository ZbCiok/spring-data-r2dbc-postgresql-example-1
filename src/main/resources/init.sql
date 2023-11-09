create table product
(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name  varchar(50),
    price decimal
);

insert into product(name, price)
values ('product_A', 1.0);
insert into product(name, price)
values ('product_B', 2.0);
insert into product(name, price)
values ('product_C', 3.0);
insert into product(name, price)
values ('product_D', 4.0);

-- =============================================
DROP TABLE IF EXISTS player;
CREATE table player (id SERIAL PRIMARY KEY, name VARCHAR(255), age INT NOT NULL);

insert into player(name, age) values ('playe#1', 25);