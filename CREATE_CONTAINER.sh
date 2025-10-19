#!/bin/sh

# {{{ start_banner()
start_banner()
{
	echo "############################################################"
	echo "# START SCRIPT"
	echo "############################################################"
}
# }}}

# {{{ finish_banner()
# $1: time to start this script
finish_banner()
{
	S_TIME=$1
	E_TIME=$(date +%s)
	DURATION=$((E_TIME - S_TIME))
	echo "############################################################"
	echo "# FINISH SCRIPT ($DURATION seconds)"
	echo "############################################################"
}
# }}}


# {{{ create_container()
# $1: the current directory
create_container()
{
	CUR_DIR=$1
	echo "\n### START: Create new containers ##########"
	docker-compose \
		-f $CUR_DIR/docker-compose-webapp-dev.yml \
		up -d -V --remove-orphans
}
# }}}

# {{{ destory_container()
# $1: the current directory
destory_container()
{
	CUR_DIR=$1
	echo "\n### START: Destory existing containers ##########"
	docker-compose \
		-f $CUR_DIR/docker-compose-webapp-dev.yml \
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
	docker-compose \
		-f $CUR_DIR/docker-compose-webapp-dev.yml \
		up -d -V --build $CONTAINER_NM
}
# }}}


# {{{ show_list_container()
show_list_container()
{
	echo "\n### START: Show a list of container ##########"
	docker ps -a
}
# }}}

# {{{ show_url()
show_url()
{
	cat << EOS

/************************************************************
 * Information:
 * - Access to Monitored servers with the URL below.
 *   - webui:      http://localhost:8181
 *   - webapi:     http://localhost:8182/api/dice/v1/roll
 *   - webapi:     http://localhost:8182/api/dice/v1/list
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


S_TIME=$(date +%s)
CUR_DIR=$(cd $(dirname $0); pwd)

case "$1" in
	"up")
		clear
		start_banner
		create_container $CUR_DIR
		show_list_container
		show_url
		finish_banner $S_TIME
		;;
	"down")
		clear
		start_banner
		destory_container $CUR_DIR
		remove_webapp_image
		show_list_container
		finish_banner $S_TIME
		;;
	"rebuild")
		clear
		start_banner
		rebuild_container $CUR_DIR $2
		show_list_container
		show_url
		finish_banner $S_TIME
		;;
	"list")
		clear
		show_list_container
		;;
	"info")
		show_url
		;;
	"")
		clear
		start_banner
		destory_container $CUR_DIR
		remove_webapp_image

		create_container $CUR_DIR
		show_list_container
		show_url
		finish_banner $S_TIME
		;;
	*)
		show_usage
		exit 1
		;;
esac
