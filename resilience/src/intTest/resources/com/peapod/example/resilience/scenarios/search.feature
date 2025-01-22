@ignore
Feature:

  Scenario:
    Given path '/products/search'
    And header Content-Type = 'application/json; charset=utf-8'
    And request { serviceLocationId: '#(svcLocationId)' , text: '#(searchText)' }
    * def req = { serviceLocationId: '#(svcLocationId)' , text: '#(searchText)' }
    * print req
    When method post
    Then status 200
    * def products = karate.jsonPath(response, '$.products')
    * def total = karate.jsonPath(response, '$.total')



