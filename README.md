# server basic service

## includes
```
	core
	async-event
	almost-universal-relationship
	user-meta-serializer
```

## maven build
```
	mvn clean install -DskipTests -U
	or
	mvn clean install -Dscale=/ (if you can connect to rabbitmq server's default vhost)
```