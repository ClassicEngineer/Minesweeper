# Minesweeper
Backend of Minesweeper game for T_G interview task

Local run: 
0. ``` db.createUser(
  {
    user: "root",
    pwd: "password",
    roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]
  }
)```
1. Run mongo with `docker-compose -f docker/mongodb-docker-compose.yml`
2. `mvn spring-boot:run`
