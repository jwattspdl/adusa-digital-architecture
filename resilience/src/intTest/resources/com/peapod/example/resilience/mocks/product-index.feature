Feature: User Service

  Background:
    * def callCount = 0

  Scenario: pathMatches('/{serviceLocationId}/search')
    * def products = read('classpath:com/peapod/example/resilience/mocks/products.json')
    * def callCount = callCount + 1
    * print request.searchText
    * def foundProducts = products.filter(prod => prod.name.startsWith(request.searchText))
    * print foundProducts
    * def responseStatus = foundProducts && foundProducts.length > 0 ? 200 : 404
    * if (responseStatus == 404) karate.abort()
    * def responseStatus = foundProducts[0].error ? 500 : 200
    * if (responseStatus == 500) karate.abort()
    * def responseDelay = foundProducts[0].delay ? foundProducts[0].delay : 0
    * def responseHeaders = { 'Content-Type': 'application/json' }
    * def response = { totalFound: '#(foundProducts.length)', products: '#(foundProducts)' }


  Scenario: pathMatches('/callCount') && methodIs('get')
    * def response = { callCount: '#(callCount)' }

  Scenario: pathMatches('/callCount') && methodIs('post')
    * def callCount = 0
