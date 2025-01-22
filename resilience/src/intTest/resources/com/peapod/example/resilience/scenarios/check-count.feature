@ignore
Feature:

  Scenario:
    Given url mockServerUrl
    And path '/callCount'
    When method get
    Then status 200
    * def count = response.callCount
