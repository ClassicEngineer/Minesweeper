# Minesweeper
Backend of Minesweeper game for T_G interview task

Local run: 


1. Run mongo with `docker-compose -f docker/mongodb-docker-compose.yml`
2. `mvn spring-boot:run`


For existing mongo container with other admin user-password:
1. `use admin`
2. `db.auth('${login}', '${password}')`
3. ```javascript
    db.createUser(
    {
      user: "root",
      pwd: "password",
      roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]
    })
