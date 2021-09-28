create table if not exists appoint_time (
    appoint_time_id integer primary key,
    event_id integer not null,
    date text not null,
    from_time text not null,
    until_time text not null,
    kind text not null,
    capacity int not null
);

create table if not exists appoint (
    appoint_id integer primary key,
    event_id integer not null,
    appoint_time_id integer 
    patient_name text not null,
    patient_id integer not null,
    memo text not null,
    foreign key (appoint_time_id) references appoint_time(appoint_time_id)
);

create table if not exists app_event (
    app_event_id integer primary key,
    event_id int not null,
    created_at text not null,
    model text not null,
    kind text not null,
    data text not null
);

create table if not exists event_id_store (
    id int primary key not null,
    event_id int not null
);



