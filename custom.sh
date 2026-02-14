
# {{{ create_container()
# $1: the current directory
create_container()
{
	CUR_DIR=$1
	echo "\n### START: Create new containers ##########"
	docker compose \
		-f $CUR_DIR/docker-compose-webapp.base.yml \
		-f $CUR_DIR/docker-compose-webapp.mode.dev.yml \
		up -d -V --remove-orphans
}
# }}}

# {{{ destory_container()
# $1: the current directory
destory_container()
{
	CUR_DIR=$1
	echo "\n### START: Destory existing containers ##########"
	docker compose \
		-f $CUR_DIR/docker-compose-webapp.base.yml \
		-f $CUR_DIR/docker-compose-webapp.mode.dev.yml \
		down -v --remove-orphans
}
# }}}

# {{{ remove_webapp_image()
remove_webapp_image()
{
	echo "\n### START: Remove webapp images ##########"

	docker rmi webapp/webui
	docker rmi webapp/webapi
}
# }}}

# {{{ rebuild_container()
# $1: the current directory
# $2: the name of container to rebuild
rebuild_container()
{
	CUR_DIR=$1
	CONTAINER_NM=$2
	echo "\n### START: Rebuild a container ##########"
	docker stop $CONTAINER_NM
	IMAGE_NM=$(docker inspect --format='{{.Config.Image}}' $CONTAINER_NM)
	docker rm $CONTAINER_NM
	docker rmi $IMAGE_NM
	docker compose \
		-f $CUR_DIR/docker-compose-webapp.base.yml \
		-f $CUR_DIR/docker-compose-webapp.mode.dev.yml \
		up -d -V --build $CONTAINER_NM
}
# }}}


# {{{ show_url()
show_url()
{
	cat << EOS

/************************************************************
 * Information:
 * - Access to Monitored servers with the URL below.
 *   - webui:               http://localhost:8181
 *   - webapi(GET/POST):    http://localhost:8182/api/v1/dices
 *   - micrometer: http://localhost:8181/actuator for webui
 *   - micrometer: http://localhost:8182/actuator for webapi
 ***********************************************************/

EOS
}
# }}}

# {{{ show_usage()
show_usage()
{
	cat << EOS
Usage: $(basename $0) [options]

Start the containers needed for the hands-on. If there are any containers
already running, stop them and remove resources beforehand.

Options:
  up                    Start the containers.
  down                  Stop the containers and remove resources.
  rebuild {container}   Stop the specified container, removes its image, and
                        restarts it.
  list                  Show the list of containers.
  info                  Show the information such as URLs.

EOS
}
# }}}

# {{{ show_usage_webapi()
show_usage_webapi()
{
	cat << EOS
Usage: $(basename $0) [options]

Runs this project as a Spring Boot application. Also simplify execution
of common Gradle tasks.

Options:
  assemble              Assemble the outputs of this project.
  doc                   Generate the OpenAPI Spec and Javadoc.
  test                  Run the unit tests.

EOS
}
# }}}

# {{{ show_usage_webui()
show_usage_webui()
{
	cat << EOS
Usage: $(basename $0) [options]

Runs this project as a Spring Boot application. Also simplify execution
of common Gradle tasks.

Options:
  assemble              Assemble the outputs of this project.
  doc                   Generate the Javadoc.
  test                  Run the unit tests.

EOS
}
# }}}

