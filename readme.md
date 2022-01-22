
### Setup

Start postgres container:
```
docker run -p 5432:5432 --name postgres-test -e POSTGRES_PASSWORD=root -e POSTGRES_DB=test -d postgres
```

Create `expenses` table:

```sql
CREATE table expenses (
id serial primary key,
cost numeric(12, 2) not NULL,
occured_at TIMESTAMP NOT NULL,
categories VARCHAR ( 100 ) NOT null,
comment VARCHAR ( 200 )
);
```

Truncate `expenses` table:

```sql
truncate table expenses;
```

Select grouped and filtered by particular date:

```sql
SELECT sum(cost)
FROM expenses
where cast(occured_at as date) = '2021-03-01'
group by cast(occured_at as date);
```

Load data using `xlsx-to-postgres-loader`.

Start grafana container:
```
docker run -d -p 3000:3000 grafana/grafana
```

Use postgres datasource with the following configs:
```
host: <container-ip>:5432 // use  `docker network inspect bridge` to find out
database: test
user: postgres
password: root
TLS/SLL mode: disabled 
```

Import `grafana-dashboard.json`.