#!/bin/sh

# To keep things neat and tidy, let's remove any dangling images

if [ -z "$(docker images --filter "dangling=true" -q)" ]; then
    echo "no dangling images to remove"
else
    docker rmi $(docker images --filter "dangling=true" -q --no-trunc) --force || true
fi

#--------------------------------------------------------------------------------------------
mvn clean package
docker-compose up -d