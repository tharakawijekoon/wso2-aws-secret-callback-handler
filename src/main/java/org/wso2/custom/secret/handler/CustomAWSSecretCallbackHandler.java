package org.wso2.custom.secret.handler;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import org.wso2.securevault.secret.AbstractSecretCallbackHandler;
import org.wso2.securevault.secret.SingleSecretCallback;

import java.nio.ByteBuffer;
import java.security.InvalidParameterException;

public class CustomAWSSecretCallbackHandler extends AbstractSecretCallbackHandler {

    private static String passwordKeystore;

    protected void handleSingleSecretCallback(SingleSecretCallback singleSecretCallback) {

        try {
            passwordKeystore = CustomAWSSecretCallbackHandler.getSecret();
        } catch (Exception e) {
            e.printStackTrace();
        }

        singleSecretCallback.setSecret(passwordKeystore);

    }

    protected static String getSecret() {

        String passwordKeystore = null;
        String secretName = "secretName";
        String endpoint = "secretsmanager.us-west-2.amazonaws.com";
        String region = "us-west-2";

        AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
        AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
        clientBuilder.setEndpointConfiguration(config);
        AWSSecretsManager client = null;

        try {
            client = clientBuilder.build();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Finish");
        }

        String secret;
        ByteBuffer binarySecretData;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName).withVersionStage("AWSCURRENT");

        GetSecretValueResult getSecretValueResult = null;

        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);

        } catch (ResourceNotFoundException e) {
            System.out.println("The requested secret " + secretName + " was not found");
        } catch (InvalidRequestException e) {
            System.out.println("The request was invalid due to: " + e.getMessage());
        } catch (InvalidParameterException e) {
            System.out.println("The request had invalid params: " + e.getMessage());
        }

        if (getSecretValueResult == null) {
            return null;
        }

        // Depending on whether the secret was a string or binary, one of these fields will be populated
        if (getSecretValueResult.getSecretString() != null) {
            secret = getSecretValueResult.getSecretString();
            System.out.println(secret);
            passwordKeystore = secret;
        } else {
            binarySecretData = getSecretValueResult.getSecretBinary();
            System.out.println(binarySecretData.toString());
        }

        return passwordKeystore;
    }
}