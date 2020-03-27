# Note - To make the app.jar sleep until the database is up and running.
while ! nc -z mysqldb 3306;
do
  sleep 1;
done;
/usr/bin/java -Xmx400m -Xms400m -jar app.jar
