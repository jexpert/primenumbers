Hi,

1. This is a very simple prime numbers generation which can generate numbers up to MAX integer only

2. To generate: URL: http://localhost:8080/primenumbers?max=100000 where max is an optional parameter
3. To check: URL http://localhost:8080/primecheck?number=999983 where number must be long

4. Configuration may define the generator implementation class - currently only one
5. Start: java -jar primenumbers-0.0.1-SNAPSHOT.jar server basic.yml

6. The code is self descriptive, I did not find much room for multithreading but tried to use Java 8 Scala-like collections

Regards

Mikhail
 