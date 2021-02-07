#!/bin/bash

password=$1
sonarHost="https://sonar.dev.g2.myvirtualfridge.net"

echo "New password is $password"

echo "Setting a new password for the sonarqube admin user."
curl -u "admin:admin" -X POST --url "$sonarHost/api/users/change_password?login=admin&password=$password&previousPassword=admin" -s

echo "Forcing the use authentication."
curl -u "admin:$password" -X POST --url "$sonarHost/api/settings/set?key=sonar.forceAuthentication&value=true" -s

echo "Generating the admin user token to use it as a service connection."
sonarqubeToken=$(curl -u "admin:$password" -X POST --url "$sonarHost/api/user_tokens/generate?name=sonarqube-smart-fridge-token" -s | jq -r '.token')
echo "$sonarqubeToken"

qualityGateId=$(curl -u "admin:$password" -X POST --url "$sonarHost/api/qualitygates/create?name=smart-fridge" -s | jq -r '.id')
echo "Created new quality gate."

curl -u "admin:$password" -X POST --url "$sonarHost/api/qualitygates/create_condition?gateId=$qualityGateId&metric=line_coverage&error=10&op=LT" -s
echo "Added condition 'line coverage' to quality gate."

curl -u "admin:$password" -X POST --url "$sonarHost/api/qualitygates/set_as_default?id=$qualityGateId" -s
echo "Set the quality gate 'smart-fridge' as default."

echo "Installing mandatory plugins."
curl -u "admin:$password" -X POST --url "$sonarHost/api/plugins/install?key=buildbreaker" -s
curl -u "admin:$password" -X POST --url "$sonarHost/api/plugins/install?key=java" -s
curl -u "admin:$password" -X POST --url "$sonarHost/api/plugins/install?key=dependencycheck" -s
curl -u "admin:$password" -X POST --url "$sonarHost/api/plugins/install?key=scmgit" -s
curl -u "admin:$password" -X POST --url "$sonarHost/api/plugins/install?key=jacoco" -s

echo "Restarting the server to install the plugins."
curl -u "admin:$password" -X POST --url "$sonarHost/api/system/restart" -s