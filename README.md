# window-post-api-lambdas
Post APIs

## Development

### Building

#### Composite build (local building)

Composite build:
`gradle --include-build ../window-api-models generateProto`

#### Local testing

(needs AWS SAM Local)

`gradle runLocal --include-build ../window-api-models shadowJar`

#### Java 9 issues

When trying to pull from S3, Gradle doesn't work with Java 9 properly.  So set `JAVA_HOME` to 1.8 first (for now):
`set -x JAVA_HOME /Library/Java/JavaVirtualMachines/jdk1.8.0_152.jdk/Conitents/Home`

*Copyright (c) 2017 Rik Brown*

#### CodeBuild Gradle version issues

CodeBuild uses Gradle 2.7.

```
source "/Users/rikbrown/.sdkman/bin/sdkman-init.sh"
sdk use gradle 2.7 
gradle ...
```