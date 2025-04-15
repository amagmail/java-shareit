create table if not exists users (
  id bigint generated by default as identity not null,
  name varchar(255) not null,
  email varchar(512) not null,
  constraint pk_users primary key (id),
  constraint uq_user_email unique (email)
);
create table if not exists items (
  id bigint generated by default as identity not null,
  name varchar(255) not null,
  description varchar(512) not null,
  is_available boolean not null,
  owner_id bigint,
  request_id bigint,
  constraint pk_items primary key (id)
);
create table if not exists bookings (
  id bigint generated by default as identity not null,
  start_date timestamp not null,
  end_date timestamp not null,
  item_id bigint not null,
  booker_id bigint not null,
  status varchar(255) not null
);
create table if not exists comments (
  id bigint generated by default as identity not null,
  text varchar(512) not null,
  item_id bigint not null,
  author_id bigint not null,
  created timestamp not null
);