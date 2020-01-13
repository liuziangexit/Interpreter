echo 'building jar...'
./build_jar.sh
echo 'running test...'
java -cp interpreter.jar com.miwan.interpreter.Test
