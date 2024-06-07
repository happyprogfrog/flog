docker run -d \
--name flog-mysql \
-e MYSQL_ROOT_PASSWORD="flog" \
-e MYSQL_USER="flog" \
-e MYSQL_PASSWORD="flog" \
-e MYSQL_DATABASE="flog" \
-p 3306:3306 \
mysql