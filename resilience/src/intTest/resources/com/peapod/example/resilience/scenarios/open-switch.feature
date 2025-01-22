@ignore
Feature:

  Scenario:
    Given path `/actuator/circuitbreakers/${breakerName}`
    And header Content-Type = 'application/json; charset=utf-8'
    And request { "updateState": "FORCE_OPEN" }
    When method post
    Then status 200