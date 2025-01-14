# Operations and SRE Notes

This document provides the details on deploying, monitoring, and maintaining [the deployed] service.

## TOC
  - [App Controls](#app-controls)
    - [Circuit Breakers](#circuit-breakers)
      - [Breaker Name or ID](#breaker-name-or-id)
    - [Feature Toggles](#feature-toggles)
      - [Ops](#ops)
        - [Feature Toggle Name](#feature-toggle-name)
      - [Release](#release)
        - [Release Toggle Name](#release-toggle-name)
    - [SLAs](#slas)
      - [Incoming Dependencies](#incoming-dependencies)
      - [Outgoing Dependencies](#outgoing-dependencies)
    - [Monitoring/Logs](#monitoring-logs)
    - [Troubleshooting](#troubleshooting)

## App Controls

[This section details the needs what is available to control the activity of the running service or application,
e.g., circuit breakers, feature toggles, etc.]

### Circuit Breakers

#### [Breaker Name or ID]

[This section should include:]
- Description of what the circuit breaker is protecting and why it is needed.
- What is the criteria for the breaker opening?
- Does the breaker attempt to close automatically?
    - If so, what is the criteria for closing?
    - If not, what is the procedure for closing the breaker manually?
- Is there a fallback when the breaker is open?
    - What is the fallback and how does it affect the Prism system, e.g., degraded site functionality, missing products?
        - Details on all affected features and any notes on customer experience must be included.
    - Does the fallback result in any potential data loss or data corruption?
        - It shouldn't, but if it does what is the recovery procedure?
- Any notes or links to SRE checks or monitoring of the breaker.
- Any notes on logs messages associated with the breaker state.
    - Logs specific to the fallback
    - Logs that would indicate the cause of the circuit breaker opening (errors or slow responses)
    - Logs that would indicate any attempts to close the breaker

### Feature Toggles

#### Ops

[Ops toggles are long-lived feature toggles that can be used to deactivate some functionality, e.g., feature or service.
Yes, Ops toggles serve a similar purpose to circuit breakers, but typically, toggles are intended to shut off whole features
versus automatically handling details like communication issues with a downstream service. As an example, an Ops toggle would be
used to shut off the MFA feature, and a circuit breaker would be used to return an empty list of the ads to incorporate
with search results if a third-party ad provider is down.]

##### [Feature Toggle Name]

[This section must include:]
- Description of what the toggle controls
    - Effects on customer experience
    - Effects on Prism system
- The circumstances under which the toggle be turned off (or considered for turning off)
- Any notes on logs messages associated with the toggle.
    - Logs specific to the toggle on
    - Logs specific to the toggle off

#### Release

[Release toggles support CI by allow incomplete code changes (or complete code changes that are not yet activated/released)
to be deployed to environments (especially production). They should be off until the change or feature is to be release,
and after release, they should be removed.]

##### [Release Toggle Name]

[This section must include:]
- Description of the functionality that the release toggle controls
- If available, the Jira Ticket numbers associated with the changes controlled by the toggle
- The estimated release date for the changes controlled by the toggle
- Notes on the expected behavior of the feature when the toggle is turned on, e.g., log message, new API endpoints or return codes, etc.

## SLAs

### Incoming Dependencies

#### [Dependency Name]


### Outgoing Dependencies

#### [Dependency Name]

[For each endpoint used by this service, the following must be documented:]
- Expected connection time
- Expected response time
- Expected error rate

## Monitoring/Logs

[This section may include:]
- Links to Datadog dashboards, service pages, or other monitoring tools that are used to monitor the service.
  - If you do include links to Datadog or other monitoring tool, please group the links out by environment
- All synthetic monitoring that should be done for the service.
    - Note, this doesn't need to include links to specific monitoring tools for an environment, but it should include
      what should be monitored and why.
- Any specific logs messages or patterns of log messages that are used for error detection and altering


### Troubleshooting (optional)

[The devs should use this section to log issues that have arisen and how they were resolved. This will help the SRE
team to build a playbook for the service.]



