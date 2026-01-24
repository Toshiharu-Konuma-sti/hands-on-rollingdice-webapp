
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

# {{{ install_jdk()
install_jdk()
{
	echo "\n### START: Install Open JDK ##########"
	which java
	if [ $? -ne 0 ]; then
		sudo apt install -y openjdk-21-jdk-headless
	fi
}
# }}}

