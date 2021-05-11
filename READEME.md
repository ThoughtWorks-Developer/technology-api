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
secrethub mkdir thoughtworks-developer/technology-api/docker
secrethub write thoughtworks-developer/technology-api/docker/user
secrethub write thoughtworks-developer/technology-api/docker/password
```


```
secrethub rm -r thoughtworks-developer/technology-api/docker
secrethub service ls thoughtworks-developer/technology-api
secrethub repo revoke thoughtworks-developer/technology-api s-ktw7j5Cth4Fm
secrethub repo rm thoughtworks-developer/technology-api
secrethub org rm thoughtworks-developer
```