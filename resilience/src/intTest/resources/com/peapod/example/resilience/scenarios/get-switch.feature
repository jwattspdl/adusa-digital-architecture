@ignore
Feature:

  Scenario:
    Given path `/actuator/circuitbreakers`
    And header Content-Type = 'application/json; charset=utf-8'
    When method get
    Then status 200
    * def breakerState = karate.jsonPath(response, '$.circuitBreakers.' + breakerName + '.state')
