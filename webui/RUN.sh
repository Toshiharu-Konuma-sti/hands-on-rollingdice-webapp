#!/bin/sh

# {{{ show_usage()
show_usage()
{
	cat << EOS
Usage: $(basename $0) [options]

Runs this project as a Spring Boot application. Also simplify execution
of common Gradle tasks.

Options:
  assemble              Assembles the outputs of this project.
  oas                   Generates the spring doc openapi file and
                        convert from json to html.
  javadoc               (Coming soon!!)

EOS
}
# }}}

S_TIME=$(date +%s)
CUR_DIR=$(cd $(dirname $0); pwd)
. $CUR_DIR/functions.sh

case "$1" in
	"assemble")
		start_banner
		./gradlew assemble -x cyclonedxBom --info
		finish_banner $S_TIME
		;;
	"doc")
		start_banner
		./gradlew javadoc -x cyclonedxBom
        tree -L 2 build/docs/
		finish_banner $S_TIME
		;;
	"")
		start_banner
		install_jdk

		echo "Test URL:"
		echo "- http://localhost:8181/"

		export SPRING_PROFILES_ACTIVE=dev
		./gradlew clean
		# ./gradlew bootRun
		./gradlew assemble -x cyclonedxBom
        java -jar ./build/libs/apisl.handson.rollingdice.webapp.webui-*-SNAPSHOT.jar --management.otlp.metrics.export.enabled=false --otel.sdk.disabled=true
		;;
	*)
		show_usage
		;;
esac
