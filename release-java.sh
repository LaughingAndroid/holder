## annotation
#./gradlew :annotation:clean :annotation:pBPTML
#./gradlew :annotation:bintrayUpload
#
## compiler
#./gradlew :compiler:clean :compiler:pBPTML
#./gradlew :compiler:bintrayUpload

# router
module=holder-complier
./gradlew  bintrayUpload -PbintrayUser=mchwind -PbintrayKey=$1 -PdryRun=false -x javadoc
