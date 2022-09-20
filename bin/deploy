#!/bin/bash

set -e

bin/deploy-prepare
bin/deploy-as-dev-next
ssh ${MYCLINIC_REMOTE_SSH} "cd ~/myclinic-scala-server; ./rotate-myclinic.sh"