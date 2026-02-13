#!/bin/sh

S_TIME=$(date +%s)
CUR_DIR=$(cd $(dirname $0); pwd)
. $CUR_DIR/common.sh
. $CUR_DIR/custom.sh

case "$1" in
	"assemble")
		start_banner
		./gradlew assemble -x cyclonedxBom --info
		finish_banner $S_TIME
		;;
	"doc")
		start_banner
		./gradlew generateOpenApiDocsNoServer -x cyclonedxBom
		./gradlew openApiGenerate -x cyclonedxBom
		./gradlew javadoc -x cyclonedxBom
		command -v tree >/dev/null 2>&1 && tree -L 2 build/docs/
		finish_banner $S_TIME
		;;
	"test")
		start_banner
		./gradlew test -x cyclonedxBom
		finish_banner $S_TIME
		;;
	"")
		start_banner
		install_jdk

		echo "Test URL:"
		echo "- http://localhost:8182/api/v1/dices"

		export SPRING_PROFILES_ACTIVE=dev
		./gradlew clean
		./gradlew assemble -x cyclonedxBom
        java -jar ./build/libs/apisl.handson.rollingdice.webapp.webapi-*-SNAPSHOT.jar --management.otlp.metrics.export.enabled=false --otel.sdk.disabled=true
		;;
	*)
		show_usage_webapi
		;;
esac
