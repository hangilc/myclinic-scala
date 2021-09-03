create table if not exists appoint (
    date text not null,
    time text not null,
    patient_name text,
    patient_id integer not null,
    memo text not null,
    primary key(date, time)
);

create table if not exists app_event (
    id integer primary key,
    created_at text not null,
    model text not null,
    kind text not null,
    data text not null
);



