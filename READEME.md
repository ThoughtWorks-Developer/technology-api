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
helm template local charts/.
helm install local --dry-run --debug ./charts
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
helm chart save charts/. demo.goharbor.io/thoughtworks-developer/technology-api:0.0.2
```

### Push a chart to this project

```
helm chart push demo.goharbor.io/thoughtworks-developer/technology-api:0.0.2
```

curl --data-binary "@technology-api-0.1.0.tgz" http://localhost:8080/api/charts