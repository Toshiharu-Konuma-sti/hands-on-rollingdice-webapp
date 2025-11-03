
# {{{ start_banner()
start_banner()
{
	echo "############################################################"
	echo "# START SCRIPT"
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

