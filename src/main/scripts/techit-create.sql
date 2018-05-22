CREATE TABLE hibernate_sequence (
    next_val   bigint
) engine=InnoDB;

create table users (
   id integer primary key,
   department varchar(255),
   email varchar(255),
   first_name varchar(255) not null,
   last_name varchar(255) not null,
   password varchar(255) not null,
   phone_number varchar(255),
   user_role_id integer,
   username varchar(255) not null,
   unit_id integer
) engine=innodb;

create table units (
   id integer primary key,
   description varchar(4000),
   email varchar(255),
   location varchar(255),
   phone_number varchar(255),
   unit_name varchar(255) not null
) engine=InnoDB;

create table tickets (
   id integer primary key,
   completion_details varchar(255),
   creation_date time,
   current_priority integer,
   current_progress integer,
   department varchar(255),
   details varchar(4000),
   email varchar(255),
   end_date time,
   last_updated_date time,
   phone varchar(255),
   start_date time,
   subject varchar(255),
   location varchar(255),
   requester_id integer not null,
   unit_id integer,
   foreign key(requester_id) references users(id),
   foreign key(unit_id) references units(id)
) engine=InnoDB;

create table ticket_updates (
   id integer primary key,
   modified_date time,
   modifier_user_id integer,
   ticket_id integer,
   update_detail varchar(4000),
   foreign key(ticket_id) references tickets(id)
) engine=InnoDB;

create table tickets_technicians (
   ticket_id integer not null,
   user_id integer not null,
   foreign key(ticket_id) references tickets(id),
   foreign key(user_id) references users(id)
) engine=InnoDB;

