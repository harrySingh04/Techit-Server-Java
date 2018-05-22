
    create table hibernate_sequence (
       next_val bigint
    ) engine=InnoDB;

    insert into hibernate_sequence values ( 1 );

    insert into hibernate_sequence values ( 1 );

    insert into hibernate_sequence values ( 1 );

    insert into hibernate_sequence values ( 1 );

    create table ticket_updates (
       id integer not null,
        modified_date time,
        modifier_user_id integer,
        ticket_id integer,
        update_detail varchar(4000),
        primary key (id)
    ) engine=InnoDB;

    create table tickets (
       id integer not null,
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
        primary key (id)
    ) engine=InnoDB;

    create table tickets_technicians (
       ticket_id integer not null,
        user_id integer not null
    ) engine=InnoDB;

    create table units (
       id integer not null,
        description varchar(4000),
        email varchar(255),
        location varchar(255),
        phone_number varchar(255),
        unit_name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table users (
       id integer not null,
        department varchar(255),
        email varchar(255),
        first_name varchar(255) not null,
        last_name varchar(255) not null,
        password varchar(255) not null,
        phone_number varchar(255),
        user_role_id integer,
        username varchar(255) not null,
        unit_id integer,
        primary key (id)
    ) engine=InnoDB;

    alter table ticket_updates 
       add constraint FK2voine2npgsepce7wlbcefs76 
       foreign key (modifier_user_id) 
       references users (id);

    alter table ticket_updates 
       add constraint FKlpktydbe16ywvi544isafii9v 
       foreign key (ticket_id) 
       references tickets (id);

    alter table tickets 
       add constraint FKdp5i1hou98n2co3e49fffh9fp 
       foreign key (requester_id) 
       references users (id);

    alter table tickets 
       add constraint FKmj126vcy9uobxd6rfu269wjc2 
       foreign key (unit_id) 
       references units (id);

    alter table tickets_technicians 
       add constraint FKsuhtniggy71fds0o5shmggqw1 
       foreign key (user_id) 
       references users (id);

    alter table tickets_technicians 
       add constraint FKg7g9kx96hij4mqqw2iqphcssb 
       foreign key (ticket_id) 
       references tickets (id);

    alter table users 
       add constraint FKp2hfld4bhbwtakwrmt4xq6een 
       foreign key (unit_id) 
       references units (id);
