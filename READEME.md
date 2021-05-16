# Technology API

## Install Prerequisites

* Download & Install [Docker Toolbox](https://www.docker.com/products/docker-toolbox) on your computer.
* ```brew install secrethub```
* ```brew install yq```
* ```brew install gh```

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

secrethub mkdir $NAMESPACE/$REPOSITORY/github
secrethub write $NAMESPACE/$REPOSITORY/github/user
secrethub write $NAMESPACE/$REPOSITORY/git/password
secrethub mkdir $NAMESPACE/$REPOSITORY/aws
secrethub write $NAMESPACE/$REPOSITORY/aws/account_id
secrethub write $NAMESPACE/$REPOSITORY/aws/access_key_id
secrethub write $NAMESPACE/$REPOSITORY/aws/secret_access_key
secrethub write $NAMESPACE/$REPOSITORY/containerregistry/user
secrethub write $NAMESPACE/$REPOSITORY/containerregistry/password

gh auth login --with-token $(secrethub read thoughtworks-developer/technology-api/github/password)
gh secret set SECRETHUB_CREDENTIAL -b $(secrethub service init $NAMESPACE/$REPOSITORY --permission write) -R $NAMESPACE/$REPOSITORY
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
export CONTAINER_REGISTRY_USER=$(secrethub read $NAMESPACE/$REPOSITORY/github/user)
export CONTAINER_REGISTRY_PASSWORD=$(secrethub read $NAMESPACE/$REPOSITORY/github/password)
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
helm registry login --username $CONTAINER_REGISTRY_USER --password $CONTAINER_REGISTRY_PASSWORD $CONTAINER_REGISTRY
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

helm registry login --username $CONTAINER_REGISTRY_USER --password $CONTAINER_REGISTRY_PASSWORD $CONTAINER_REGISTRY
helm chart pull ghcr.io/thoughtworks-developer/technology-api:0.1.0
helm install snapshot --namespace $NAMESPACE --set imageCredentials.username=$CONTAINER_REGISTRY_USER --set imageCredentials.password=$CONTAINER_REGISTRY_PASSWORD ghcr.io/thoughtworks-developer/technology-api:0.1.0