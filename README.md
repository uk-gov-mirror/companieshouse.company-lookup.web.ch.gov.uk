# company-lookup.web.ch.gov.uk
Web application to control user journeys that lookup company details directly then proceed on to complete a specific journey. This application is written using the [Spring Boot](http://projects.spring.io/spring-boot/) Java framework.

## Requirements
In order to run the Service locally you'll need the following installed on your machine:

- [Java 21](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/downloads)

## Connecting to the Service
To start using this service you need to hit the following URL:
_ENVIRONMENT_/company-lookup/search?forward=_FORWARDING_URL_

Name                   | Description                                                                                                  | Mandatory
---------------------- | ------------------------------------------------------------------------------------------------------------ | ---------
ENVIRONMENT            | The URL for the environment you are currently deploying to.                                                  | ✓
FORWARDING_URL         | The path the user will be forwarded to after the search has been completed and the normal journey continues. | ✓

### Endpoints

 | Method   | Path                                                                    | Description                                                   |
 | -------- | ----------------------------------------------------------------------- | ------------------------------------------------------------- |
 | GET      | `/company-lookup/search`                                                | Company lookup page                                           |
 | POST     | `/company-lookup/search`                                                | Company lookup page                                           |
 | GET      | `/company-lookup/no-number`                                             | Company lookup no company page                                |

### Example of journey incorporating this service

  | Method | Path                                                                                                                                                                             | Response                                                      |
  | ------ | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------- |
  | GET    | /efs-submission/start                                                                                                                                                            | 200 OK                                                        |
  | GET    | /efs-submission/new-submission                                                                                                                                                   | 302 Found                                                     |
  | GET    | /efs-submission/690b270a1356c02de11335e5/companyLookup?forward=%2Fefs-submission%2F690b270a1356c02de11335e5%2Fcompany%2F%7BcompanyNumber%7D%2Fdetails&userEmail=demo%40ch.gov.uk | 302 Found                                                     |
  | GET    | /company-lookup/search?forward=%2Fefs-submission%2F690b270a1356c02de11335e5%2Fcompany%2F%7BcompanyNumber%7D%2Fdetails&userEmail=demo%40ch.gov.uk                                 | 200 OK                                                        |
  | POST   | /company-lookup/search?forward=%2Fefs-submission%2F690b270a1356c02de11335e5%2Fcompany%2F%7BcompanyNumber%7D%2Fdetails&userEmail=demo%40ch.gov.uk                                 | 302 Found                                                     |
  | GET    | /efs-submission/690b270a1356c02de11335e5/company/00006400/details                                                                                                                | 200 OK                                                        |

## Terraform ECS

### What does this code do?

The code present in this repository is used to define and deploy a dockerised container in AWS ECS.
This is done by calling a [module](https://github.com/companieshouse/terraform-modules/tree/main/aws/ecs) from terraform-modules. Application specific attributes are injected and the service is then deployed using Terraform via the CICD platform 'Concourse'.


Application specific attributes | Value                                | Description
:---------|:-----------------------------------------------------------------------------|:-----------
**ECS Cluster**        | public-data                                     | ECS cluster (stack) the service belongs to
**Load balancer**      | {env}-chs-chgovuk                               | The load balancer that sits in front of the service
**Concourse pipeline**     |[Pipeline link](https://ci-platform.companieshouse.gov.uk/teams/team-development/pipelines/company-lookup.web.ch.gov.uk) <br> [Pipeline code](https://github.com/companieshouse/ci-pipelines/blob/master/pipelines/ssplatform/team-development/company-lookup.web.ch.gov.uk)                                  | Concourse pipeline link in shared services


### Contributing
- Please refer to the [ECS Development and Infrastructure Documentation](https://companieshouse.atlassian.net/wiki/spaces/DEVOPS/pages/4390649858/Copy+of+ECS+Development+and+Infrastructure+Documentation+Updated) for detailed information on the infrastructure being deployed.

### Testing
- Ensure the terraform runner local plan executes without issues. For information on terraform runners please see the [Terraform Runner Quickstart guide](https://companieshouse.atlassian.net/wiki/spaces/DEVOPS/pages/1694236886/Terraform+Runner+Quickstart).
- If you encounter any issues or have questions, reach out to the team on the **#platform** slack channel.

### Vault Configuration Updates
- Any secrets required for this service will be stored in Vault. For any updates to the Vault configuration, please consult with the **#platform** team and submit a workflow request.

### Useful Links
- [ECS service config dev repository](https://github.com/companieshouse/ecs-service-configs-dev)
- [ECS service config production repository](https://github.com/companieshouse/ecs-service-configs-production)

