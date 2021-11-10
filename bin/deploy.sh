set -e

usage() {
  echo "Usage: deploy.sh [OPTIONS]"
  echo "This scripts deploys server to remote host identified by myclinic-remote-server."
  echo "Options:"
  echo "  --no-archive : skips archiving in remote host"
}

ARCHIVE="1"

while (( $# > 0 )); do
  case $1 in
    --no-archive)
      ARCHIVE="0"
      shift
      ;;
    *)
      echo "invalid arg: " $1 1>&2
      echo 1>&2
      usage 1>&2
      exit 1
  esac
done

IS_WINDOW=""
if [[ "$WSL_DISTRO_NAME" ]]; then
  IS_WINDOW="1"
fi

if [[ "$IS_WINDOW" ]]; then
  cmd.exe /C "sbt server/assembly"
else
  sbt server/assembly
fi
bin/make-deploy.sh
SERVER=myclinic-remote-server
BASEDIR="$SERVER:~/myclinic-scala-server/"
ssh $SERVER sudo systemctl stop myclinic-appoint
if [[ $ARCHIVE = "1" ]]; then
  ssh $SERVER 'cd ~/myclinic-scala-server && bin/move-to-archive.sh'
else
  ssh $SERVER 'cd ~/myclinic-scala-server && bin/discard-current.sh'
fi
scp server/target/scala-3.0.2/server-assembly-0.1.0-SNAPSHOT.jar $BASEDIR
scp -r server/deploy $BASEDIR/web
ssh $SERVER sudo systemctl start myclinic-appoint
