Cloud CLI interface
===================
Enable cli access and management of Experitest SeeTest cloud environment

How to use
----------
1. Download the project:
```
git clone https://github.com/ExperitestOfficial/SeeTestCli.git
```
2. build
```
cd SeeTestCli
gradlew build customFatJar
```
3. Use
```
cd cli
cc
```
4. Configure your cloud
```
cc configure add -u <Cloud URL> -k <access key> -n <cloud name>
```
5 Example use (show all my iOS devices)
```
cc devices list -f ios
```

To get more information on the capabilities execute the cc (CloudCli) command.
