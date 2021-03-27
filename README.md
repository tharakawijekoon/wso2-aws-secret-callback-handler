# wso2-aws-secret-callback-handler
A sample secret callback handler to retrieve the password for keystore from AWS Secrets Manager. The AWS SDK uses some libraries that are also used by the identity server. However the AWS SDK has certain minumum version requirements for these libraries, the library versions used in the identity server do not meet these requirements. To solve this issue the aws-java-sdk-bundle that includes all service and dependent JARs with third-party libraries relocated to different namespaces is being used.


# Build, Deploy & Run

## Build
Clone the repository, change directory into it and execute the following command to build the project

```mvn clean install```

## Deploy

Copy and place the built JAR artifact from the /target/org.wso2.custom.secret.handler-1.0.0.jar to the <IS_HOME>/repository/components/lib directory.

Download the aws-java-sdk-bundle jar file[1] corresponding to the sdk version used[2] and add it to the <IS_HOME>/lib/ directory.

Navigate to <IS_HOME>/repository/conf/security/secret-conf.properties and add the following configuration.

```
keystore.identity.store.secretProvider=org.wso2.custom.secret.handler.CustomAWSSecretCallbackHandler
keystore.identity.key.secretProvider=org.wso2.custom.secret.handler.CustomAWSSecretCallbackHandler
```

## Run

Start the the server, the password for keystore would be retrieved from AWS Secrets Manager.

[1]https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk-bundle/1.11.74

[2]https://github.com/tharakawijekoon/wso2-aws-secret-callback-handler/blob/master/pom.xml#L25
