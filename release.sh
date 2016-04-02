#!/bin/bash
set -e

rm -rf dist/
mkdir dist

mvn clean package
cp target/gocd-slack-task*.jar dist/
