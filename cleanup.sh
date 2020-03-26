
#--------------------------------------------------------------------------------------------
# To remove all the images
# docker rmi -f $(docker images -q)

docker stop mysqldb
docker rm mysqldb
