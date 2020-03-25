#!/bin/sh

# To keep things neat and tidy, let's remove any dangling images

if [ -z "$(docker images --filter "dangling=true" -q)" ]; then
    echo "no dangling images to remove"
else
    docker rmi $(docker images --filter "dangling=true" -q --no-trunc) --force || true
fi

#--------------------------------------------------------------------------------------------

# To stop the docker compose and Remove containers for services not defined in the Compose file

docker-compose down --remove-orphans