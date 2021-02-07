#!/bin/bash

password=$1

echo "New password is $password"

echo "Setting a new password for the sonarqube admin user."
curl -u "admin:admin" -X POST --url "https://sonar.dev.g2.myvirtualfridge.net/api/users/change_password?login=admin&password=$password&previousPassword=admin" -s

echo "Generating the admin user token to use it as a service connection."
sonarqubeToken=$(curl -u "admin:$password" -X POST --url "https://sonar.dev.g2.myvirtualfridge.net/api/user_tokens/generate?name=sonarqube-smart-fridge-token" -s | jq -r '.token')
echo "$sonarqubeToken"

qualityGateId=$(curl -u "admin:$password" -X POST --url "https://sonar.dev.g2.myvirtualfridge.net/api/qualitygates/create?name=smart-fridge" -s | jq -r '.id')
echo "Created new quality gate."

curl -u "admin:$password" -X POST --url "https://sonar.dev.g2.myvirtualfridge.net/api/qualitygates/create_condition?gateId=$qualityGateId&metric=line_coverage&error=10&op=LT" -s
echo "Added condition 'line coverage' to quality gate."

curl -u "admin:$password" -X POST --url "https://sonar.dev.g2.myvirtualfridge.net/api/qualitygates/set_as_default?id=$qualityGateId" -s
echo "Set the quality gate 'smart-fridge' as default."

echo "Installing mandatory plugins."
curl -u "admin:$password" -X POST --url "https://sonar.dev.g2.myvirtualfridge.net/api/plugins/install?key=buildbreaker" -s
curl -u "admin:$password" -X POST --url "https://sonar.dev.g2.myvirtualfridge.net/api/plugins/install?key=java" -s
curl -u "admin:$password" -X POST --url "https://sonar.dev.g2.myvirtualfridge.net/api/plugins/install?key=dependencycheck" -s
curl -u "admin:$password" -X POST --url "https://sonar.dev.g2.myvirtualfridge.net/api/plugins/install?key=scmgit" -s
curl -u "admin:$password" -X POST --url "https://sonar.dev.g2.myvirtualfridge.net/api/plugins/install?key=jacoco" -s

echo "Restarting the server to install the plugins."
curl -u "admin:$password" -X POST --url "https://sonar.dev.g2.myvirtualfridge.net/api/system/restart" -s