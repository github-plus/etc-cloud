VERSION=1.1.0-SNAPSHOT
DATE= `data +"%F %T"`

build:
  @echo "script run time :" ${DATE}
  @mvn clean package -Pdev -pl co-money-gateway/ -am -Dmaven.skip.test=true


stop-server:
  @echo -e "\033[31;33m stop server pid \033[0m"

start-server:
  @echo -e "\033[31;33m start server pid \033[0m"


.PHONY: build stop-server start-server