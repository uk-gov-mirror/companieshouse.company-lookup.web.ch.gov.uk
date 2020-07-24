# company-lookup.web.ch.gov.uk
Web application to control user journeys that lookup company details directly then proceed on to complete a specific journey. This application is written using the [Spring Boot](http://projects.spring.io/spring-boot/) Java framework.

## Requirements
In order to run the Service locally you'll need the following installed on your machine:

- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/downloads)

## Connecting to the Service
To start using this service you need to hit the following URL:
_ENVIRONMENT_/company-lookup/search?forward=_FORWARDING_URL_

Name                   | Description                                                                                                  | Mandatory
---------------------- | ------------------------------------------------------------------------------------------------------------ | ---------
ENVIRONMENT            | The URL for the environment you are currently deploying to                                                   | ✓
FORWARDING_URL         | The path the user will be forwarded to after the search has been completed and the normal journey continues. | ✓
