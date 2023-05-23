create TABLE
    student (
        id SERIAL PRIMARY KEY,
        name TEXT NOT NULL
);

insert into student (id, name) values (1, 'Ade'), (2, 'Ola'), (3, 'David');