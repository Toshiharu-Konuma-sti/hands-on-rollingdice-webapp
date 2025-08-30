#!/bin/sh

# {{{ start_banner()
start_banner()
{
	echo "############################################################"
	echo "# START SCRIPT"
	echo "############################################################"
}
# }}}

# {{{ create_container()
create_container()
{
	echo "\n### START: Create new containers ##########"
	docker-compose \
		-f docker-compose-webapp-dev.yml \
		up -d -V --remove-orphans
}
# }}}

# {{{ destory_container()
destory_container()
{
	echo "\n### START: Destory existing containers ##########"
	docker-compose \
		-f docker-compose-webapp-dev.yml \
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

S_TIME=$(date +%s)

case "$1" in
	"down")
		clear
		start_banner
		destory_container
		remove_webapp_image
		show_list_container
		finish_banner $S_TIME
		;;
	"up")
		clear
		start_banner
		create_container
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
		destory_container
		remove_webapp_image
		create_container
		show_list_container
		show_url
		finish_banner $S_TIME
		;;
	*)
		echo "Usage: $0 [down|up|list|info]"
		echo ""
		exit 1
		;;
esac
