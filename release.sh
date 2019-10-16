## annotation
#./gradlew :annotation:clean :annotation:pBPTML
#./gradlew :annotation:bintrayUpload
#
## compiler
#./gradlew :compiler:clean :compiler:pBPTML
#./gradlew :compiler:bintrayUpload


cp libs/holder/complier/publish.gradle temp
rm libs/holder/complier/publish.gradle
cp empty.gradle libs/holder/complier/publish.gradle

module=holder
./gradlew :${module}:aR
./gradlew  bintrayUpload -PbintrayUser=mchwind -PbintrayKey=$1 -PdryRun=false -x javadocRelease

cp temp libs/holder/complier/publish.gradle
rm temp