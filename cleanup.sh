
#--------------------------------------------------------------------------------------------
# To remove all the images
docker rmi -f $(docker images -q)
#docker rmi patientdocumentsearchapp
#docker rmi mysqldb
