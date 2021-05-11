## GitHub

```
brew install gh
export GITHUB_TOKEN=<Personal Access Token>
gh auth login
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
helm chart save CHART_PATH demo.goharbor.io/thoughtworks-developer/REPOSITORY[:TAG]
```

### Push a chart to this project
