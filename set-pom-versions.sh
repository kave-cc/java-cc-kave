if [ $# -eq 0 ]
  then
    echo "No arguments supplied."
    exit 1
fi

if [ "$1" -eq "$1" ] 2>/dev/null; then
  BUILD_NUM=$1
else
  echo "Build number has to be provided as integer."
  exit 1
fi

echo "About to patch versions in all pom.xml files..."

START_LINES=`head -n 15 pom.xml`
NO_WHITESPACE_AND_EMPTY_LINES=`echo "$START_LINES" | sed -e 's/^[ 	]*//' | sed '/^$/d'`
ONE_LINE=`echo "$NO_WHITESPACE_AND_EMPTY_LINES" | tr -d '\n'`
VERSION_RAW=`echo "$ONE_LINE" | sed -E 's/.*<version>(.*)<\/version.*/\1/'`

echo "Version found: ${VERSION_RAW}"

NEW_VERSION="`echo "$VERSION_RAW" | sed -E 's/-SNAPSHOT//'`.${BUILD_NUM}"

echo "New version: ${NEW_VERSION}"

UPDATE="mvn versions:set -DnewVersion=$NEW_VERSION"

echo "Running ${UPDATE} ..."

#cd ..

echo "Pwd: `pwd`"
$UPDATE
