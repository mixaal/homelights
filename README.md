# Home lights cinematic control

Philips Hue home lights cinematic control

##
```
mvn clean install -DskipTests=true
```

## Configuration


```
cat ~/.homelights/config.properties
home.lights.accessKey=<your access key>
home.lights.bridge.ip=http://<bridge ip>:80
home.lights.movie.mode=true
```

## Run

```
java -jar target/desktop-to-lights-1.0-SNAPSHOT.jar
```

```
java -Dhome.lights.movie.mode=false -jar target/desktop-to-lights-1.0-SNAPSHOT.jar 
```
