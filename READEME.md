## GitHub

```
brew install gh
export GITHUB_TOKEN=<Personal Access Token>
gh auth login
```

## Helm

```
helm create technology-api
mv technology-api charts
helm lint charts/.
helm install --dry-run --debug ./charts
helm repo add thoughtworks-developer https://demo.goharbor.io/
helm package charts/.
```

## Circle CI

```
brew install circleci
export CIRCLECI_CLI_TOKEN=<PERSONAL API TOKEN>
circleci setup
circleci local execute -e SECRETHUB_CREDENTIAL=$SECRETHUB_CREDENTIAL
```

## SecretHub

```
brew install secrethub
secrethub init
secrethub org init thoughtworks-developer
secrethub repo init thoughtworks-developer/technology-api
secrethub service init thoughtworks-developer/technology-api --permission read --clip

secrethub mkdir thoughtworks-developer/technology-api/dockerhub
secrethub write thoughtworks-developer/technology-api/dockerhub/user
secrethub write thoughtworks-developer/technology-api/dockerhub/password


secrethub mkdir thoughtworks-developer/technology-api/harbor
secrethub write thoughtworks-developer/technology-api/harbor/user
secrethub write thoughtworks-developer/technology-api/harbor/password

secrethub mkdir thoughtworks-developer/technology-api/aws
secrethub write thoughtworks-developer/technology-api/aws/account_id
secrethub write thoughtworks-developer/technology-api/aws/access_key_id
secrethub write thoughtworks-developer/technology-api/aws/secret_access_key


```

```
secrethub rm -r thoughtworks-developer/technology-api/dockerhub
secrethub service ls thoughtworks-developer/technology-api
secrethub repo revoke thoughtworks-developer/technology-api s-ktw7j5Cth4Fm
secrethub repo rm thoughtworks-developer/technology-api
secrethub org rm thoughtworks-developer
```

## Docker Push Commands

### Tag an image

```
docker tag thoughtworks-developer/technology-api:latest demo.goharbor.io/thoughtworks-developer/technology-api:0.0.1
```

### Push an image

```
docker push demo.goharbor.io/thoughtworks-developer/technology-api:0.0.1
```


## Helm Push Command

### Tag a chart for this project

```
export HELM_EXPERIMENTAL_OCI=1
export AWS_ACCOUNT_ID=<AWS Account ID>
aws ecr create-repository --repository-name thoughtworks-developer/technology-api --region us-east-2
aws ecr get-login-password --region us-east-2 | helm registry login --username --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.us-east-2.amazonaws.com
helm chart save charts/. $AWS_ACCOUNT_ID.dkr.ecr.us-east-2.amazonaws.com/thoughtworks-developer/technology-api:0.1.0
```

### Push a chart to this project

```
helm chart push demo.goharbor.io/thoughtworks-developer/technology-api:0.1.0
```


```


aws ecr get-login-password --region region | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.us-east-2.amazonaws.com
```

environment:
            AWS_DEFAULT_REGION: us-east-2
            AWS_ECR_ACCOUNT_URL: 609575337789.dkr.ecr.us-east-2.amazonaws.com
            AWS_ACCESS_KEY_ID: secrethub://thoughtworks-developer/technology-api/aws/access_key_id
            AWS_SECRET_ACCESS_KEY: secrethub://thoughtworks-developer/technology-api/aws/secret_access_key