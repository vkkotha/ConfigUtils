# Configuration Utilities
[![Build](https://github.com/vkkotha/ConfigUtils/actions/workflows/build.yml/badge.svg)](https://github.com/vkkotha/ConfigUtils/actions/workflows/build.yml)
[![Github All Releases](https://img.shields.io/github/downloads/vkkotha/ConfigUtils/total.svg)]()
[![GitHub Issues](https://img.shields.io/github/issues/vkkotha/ConfigUtils.svg)](https://github.com/vkkotha/ConfigUtls/issues)

This project has tools to generate differences between two Properties file. 
This is to easily identify which configuration entries are matching, differing and missing.

## Features
- **Order properties and introduce new lines**, so it's easy to compare property files with your local diff tool
<br><br>
![image](https://user-images.githubusercontent.com/4230336/133955547-9a83344e-e57d-43db-98d9-87ce8c7cd652.png)
- **Generate diff output to console**, identifying matching, mismatch, only in source1 and only in source2 clearly
<br><br>
![image](https://user-images.githubusercontent.com/4230336/133955976-aeb27265-3221-4308-bd5a-865605caecae.png)
- **Generate an HTML report** with difference between two property files, that you can open anywhere. Generated HTML has capabilities to filter/sort properties
<br><br>
![image](https://user-images.githubusercontent.com/4230336/133956204-4aaa56de-4097-463e-8e0e-5ed184e3339a.png)
 
## Getting Started
### Prerequisites
- JDK 8+
### Install
Download *ConfigUtils-${version}-app.jar*
### Install from source
Clone/Download git repo, and build from the source.
```bash
$ ./gradlew build
```
Copy *build/libs/ConfigUtils-${version}-app.jar* to desired directory and use it.
### Usage
```bash
usage: java -jar ConfigUtils-${version}-app.jar <file1> <file2> [OPTIONS]
 -d <arg>    Use diff tool [diff executable]
 -h,--help   Print help
 -r <arg>    Generate report [text | html]
```
Run ConfigUtils on command line with to generate a text report
```bash
$ java -jar ConfigUtils-${version}-app.jar <file1> <file2>
```

## Contributing
### Submit code
- Fork Repository and Clone.
- Create feature branch and commit.
- push changes to Cloned repo.
- Create pull request and submit.
### Report Defects
- Submit new issues [Here](https://github.com/vkkotha/ConfigUtils/issues/new)

## License
ConfigUtils is provided under the [MIT License.](https://github.com/vkkotha/ConfigUtils/blob/master/LICENSE)
