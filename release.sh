## annotation
#./gradlew :annotation:clean :annotation:pBPTML
#./gradlew :annotation:bintrayUpload
#
## compiler
#./gradlew :compiler:clean :compiler:pBPTML
#./gradlew :compiler:bintrayUpload


module=holder
./gradlew :${module}:aR
./gradlew  bintrayUpload -PbintrayUser=mchwind -PbintrayKey=e10434f24d57625f6566ab14a4fbe8f01fad9cdc -PdryRun=false -x javadocRelease