create table if not exists appoint (
    date text not null,
    time text not null,
    event_id int not null,
    patient_name text,
    patient_id integer not null,
    memo text not null,
    primary key(date, time)
);

create table if not exists app_event (
    id integer primary key,
    event_id int not null,
    created_at text not null,
    model text not null,
    kind text not null,
    data text not null
);

create table if not exists event_id_store (
    id integer primary key,
    event_id integer not null
);



