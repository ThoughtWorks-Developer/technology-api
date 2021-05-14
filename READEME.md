# Technology API

## Install Prerequisites

* Download & Install [Docker Toolbox](https://www.docker.com/products/docker-toolbox) on your computer.
* ```brew install secrethub```
* ```brew install yq```

## Setup Environment

```
export CONTAINER_REGISTRY=ghcr.io
export GIT_VERSION_CONTROL=github.com
export NAMESPACE=thoughtworks-developer
export REPOSITORY=technology-api
export HELM_EXPERIMENTAL_OCI=1
```

## Download Code

```
https://$GIT_VERSION_CONTROL/$NAMESPACE/$REPOSITORY
cd $REPOSITORY
```

## Setup Secrets

```
secrethub org init $NAMESPACE
secrethub repo init $NAMESPACE/$REPOSITORY
secrethub service init $NAMESPACE/$REPOSITORY --permission read --clip
secrethub mkdir $NAMESPACE/$REPOSITORY/git
secrethub write $NAMESPACE/$REPOSITORY/git/user
secrethub write $NAMESPACE/$REPOSITORY/git/password
secrethub mkdir $NAMESPACE/$REPOSITORY/aws
secrethub write $NAMESPACE/$REPOSITORY/aws/account_id
secrethub write $NAMESPACE/$REPOSITORY/aws/access_key_id
secrethub write $NAMESPACE/$REPOSITORY/aws/secret_access_key
```

## Maven Build

```
mvn clean package -Dmaven.test.skip=true
```

## Helm Chart

```
helm lint charts/.
helm template snapshot --set imageCredentials.username=$CONTAINER_REGISTRY_USER --set imageCredentials.password=$CONTAINER_REGISTRY_PASSWORD charts/.
helm install snapshot --namespace $NAMESPACE --dry-run --debug --set imageCredentials.username=$CONTAINER_REGISTRY_USER --set imageCredentials.password=$CONTAINER_REGISTRY_PASSWORD charts/.
```

## Container Registry

### Setup Environment

```
export APP_VERSION=$(xmllint --xpath '/*[local-name()="project"]/*[local-name()="version"]/text()' pom.xml)
export RELEASE_VERSION=$(yq eval '.version' charts/Chart.yaml)
export CONTAINER_REGISTRY_USER=$(secrethub read $NAMESPACE/$REPOSITORY/git/user)
export CONTAINER_REGISTRY_TOKEN=$(secrethub read $NAMESPACE/$REPOSITORY/git/password)
export AWS_ACCOUNT_ID=$(secrethub read $NAMESPACE/$REPOSITORY/aws/account)
export AWS_ACCESS_KEY_ID=$(secrethub read $NAMESPACE/$REPOSITORY/aws/accesskey)
export AWS_SECRET_ACCESS_KEY=$(secrethub read $NAMESPACE/$REPOSITORY/aws/secretaccess)
```

### Publish Docker Image

```
docker login --username $CONTAINER_REGISTRY_USER --password $CONTAINER_REGISTRY_TOKEN $CONTAINER_REGISTRY
docker build -t $NAMESPACE/$REPOSITORY .
docker tag $NAMESPACE/$REPOSITORY $CONTAINER_REGISTRY/$NAMESPACE/$REPOSITORY:$APP_VERSION
docker push $CONTAINER_REGISTRY/$NAMESPACE/$REPOSITORY:$APP_VERSION
```

### Publish Helm Chart

```
helm registry login --username $CONTAINER_REGISTRY_USER --password $CONTAINER_REGISTRY_TOKEN $CONTAINER_REGISTRY
helm chart save charts/. $CONTAINER_REGISTRY/$NAMESPACE/$REPOSITORY:$RELEASE_VERSION
helm chart push $CONTAINER_REGISTRY/$NAMESPACE/$REPOSITORY:$RELEASE_VERSION
```

## Kubernetes Cluster

### Select Kubernetes context

```
kubectl config view
kubectl config use-context docker-desktop
```

#### Install Helm Chart

```
helm install snapshot --namespace $NAMESPACE --set imageCredentials.username=$CONTAINER_REGISTRY_USER --set imageCredentials.password=$CONTAINER_REGISTRY_PASSWORD charts/.
```

## Commit Code

```
git add . && git commit -m "updated" && git push origin master
```

on: push
jobs:
  publish:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Build Docker image
        run: docker build -t company/app:${GITHUB_SHA:0:7} .
      - uses: secrethub/actions/env-export@v0.1.0
        env:
          SECRETHUB_CREDENTIAL: ${{ secrets.SECRETHUB_CREDENTIAL }}
          DOCKER_USERNAME: secrethub://company/app/docker/username
          DOCKER_PASSWORD: secrethub://company/app/docker/password
      - name: Publish Docker image
        run: |
          echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
          docker push company/app:${GITHUB_SHA:0:7}