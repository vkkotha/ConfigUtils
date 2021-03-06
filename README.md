# Configuration Utilities
[![Build](https://github.com/vkkotha/ConfigUtils/actions/workflows/ci.yml/badge.svg)](https://github.com/vkkotha/ConfigUtils/actions/workflows/ci.yml)
[![Github All Releases](https://img.shields.io/github/downloads/vkkotha/ConfigUtils/total.svg)](https://github.com/vkkotha/ConfigUtils/releases)
[![GitHub Issues](https://img.shields.io/github/issues/vkkotha/ConfigUtils.svg)](https://github.com/vkkotha/ConfigUtils/issues)

This project has tools to generate differences between two Properties file. 
This is to easily identify which configuration entries are matching, differing and missing.

## Features
- **Order properties and introduce new lines**, so it's easy to compare property files with your local diff tool
<br><br>
![image](docs/images/readme.1.png)
- **Generate diff output to console**, identifying matching, mismatch, only in source1 and only in source2 clearly
<br><br>
![image](docs/images/readme.2.png)
- **Generate an HTML report** with difference between two property files, that you can open anywhere. Generated HTML has capabilities to filter/sort properties
<br><br>
![image](docs/images/readme.3.png)
 
## Getting Started
### Prerequisites
- JDK 8+
### Install
Download *config-utils-${version}-all.jar*
### Install from source
Clone/Download git repo, and build from the source.
```bash
$ ./gradlew build
```
Copy *build/libs/config-utils-${version}-all.jar* to desired directory and use it.
### Usage
```bash
usage: java -jar config-utils-${version}-all.jar <file1> <file2> [OPTIONS]
 -d <arg>    Use diff tool [diff executable]
 -h,--help   Print help
 -r <arg>    Generate report [text | html]
```
Run ConfigUtils on command line with to generate a text report
```bash
$ java -jar config-utils-${version}-all.jar <file1> <file2>
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
