# Home lights cinematic control

Philips Hue home lights cinematic control

##
```
mvn clean install -DskipTests=true
```

## Configuration

```
cat ~/.homelights/config
ACCESS_KEY="<your access key>"
SERVICE_LOCATION="http://<bridge ip>:80"

```

## Run

```
java -jar target/desktop-to-lights-1.0-SNAPSHOT.jar
```
