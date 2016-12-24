<<<<<<< HEAD
#!/bin/sh

mvn javadoc:aggregate
cp -rf target/site/apidocs/* ../javalite.github.io/activejdbc/snapshot
cd ../javalite.github.io/activejdbc/snapshot
git add .
git commit -m "update JavaDoc"
git push origin master
=======
mvn javadoc:aggregate
cp -rf target/site/apidocs/* ../javalite.github.io/activeweb/snapshot
cd ../javalite.github.io/activeweb/snapshot
git add .
git commit -m "update JavaDoc"
git push origin master

>>>>>>> activeweb/master
