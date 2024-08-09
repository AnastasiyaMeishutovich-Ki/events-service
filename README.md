# events-service

Install first buf:
```bash
brew install bufbuild/buf/buf
```
and then execute:
```bash
 buf dep update
```

Install also the plugin to have everything wired up automatically.

To generate stubs:
./gradlew clean build

To start the server:
./gradlew bootRun
