#!/bin/bash
cd "$(dirname "$0")"
java -cp libs\*;build\libs\*;cli\libs\* com.experitest.cloud.cli.CloudCli $@