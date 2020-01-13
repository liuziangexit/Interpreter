find . -name "*.java" > sources.txt
javac @sources.txt
rm -rf sources.txt

rm -rf interpreter.jar

cd src

find . -type f | grep .class | tr '\n' ' ' \
| xargs jar -cf ../interpreter.jar 

cd ..

find src -type f | grep .class | xargs rm
