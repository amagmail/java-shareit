create table if not exists users (
  id bigint generated by default as identity primary key,
  name varchar(255) not null,
  email varchar(512) not null,
  constraint users_email_uq unique (email)
);
create table if not exists requests (
  id bigint generated by default as identity primary key,
  description varchar(512) not null,
  requestor_id bigint not null,
  created timestamp without time zone not null,
  foreign key (requestor_id) references users(id)
);
create table if not exists items (
  id bigint generated by default as identity primary key,
  name varchar(255) not null,
  description varchar(512) not null,
  is_available boolean not null,
  owner_id bigint,
  request_id bigint,
  foreign key (owner_id) references users(id),
  foreign key (request_id) references requests(id)
);
create table if not exists bookings (
  id bigint generated by default as identity primary key,
  start_date timestamp without time zone not null,
  end_date timestamp without time zone not null,
  item_id bigint not null,
  booker_id bigint not null,
  status varchar(255) not null,
  foreign key (item_id) references items(id),
  foreign key (booker_id) references users(id)
);
create table if not exists comments (
  id bigint generated by default as identity primary key,
  text varchar(512) not null,
  item_id bigint not null,
  author_id bigint not null,
  created timestamp without time zone not null,
  foreign key (item_id) references items(id),
  foreign key (author_id) references users(id)
);
