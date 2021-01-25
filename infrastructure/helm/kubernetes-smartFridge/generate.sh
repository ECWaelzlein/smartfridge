#!/bin/bash

chartName=$1

sed -i "" "s/<chartName>/$chartName/g" Chart.yaml