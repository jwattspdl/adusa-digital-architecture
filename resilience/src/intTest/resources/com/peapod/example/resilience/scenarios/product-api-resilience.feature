Feature: Product API Resilience

    Background:
        * def resilienceProductAPISvcLocationId = 1234
        * def performSearches =
          """
          function(count, svcLocationId, searchText) {
            console.log("performing searches")
            for (var i = 0; i < count; i++) {
                console.log("Performing search for " + searchText + " at location " + svcLocationId)
                var result = karate.call('classpath:com/peapod/example/resilience/scenarios/search.feature', { svcLocationId: svcLocationId, searchText: searchText })
            }
            return true
          }
          """

    @resilience-error-scenario
    Scenario: Verify that errors will trip the breaker
      * call read('classpath:com/peapod/example/resilience/scenarios/close-switch.feature') { 'breakerName': 'productApi' }
      * call read('classpath:com/peapod/example/resilience/scenarios/reset-count.feature')
      * def status = call read('classpath:com/peapod/example/resilience/scenarios/get-switch.feature') { 'breakerName': 'productApi' }
      * match status.breakerState == 'CLOSED'
      # 7 searches, all failures. breaker should open on 5th failed call
      * def performedSearches = performSearches(7, resilienceProductAPISvcLocationId, "Error")
      * def status = call read('classpath:com/peapod/example/resilience/scenarios/get-switch.feature') { 'breakerName': 'productApi' }
      # circuit breaker should have opened on the fifth call and the downstream service should have only been called 5 times
      * match status.breakerState == 'OPEN'
      * def countResponse = call read('classpath:com/peapod/example/resilience/scenarios/check-count.feature')
      * match countResponse.count == 5

    @resilience-error-scenario-force-closed
    Scenario: Verify that a breaker forced closed will open on subsequent errors
      # Given a break was opened due to previous errors
      * call read('classpath:com/peapod/example/resilience/scenarios/close-switch.feature') { 'breakerName': 'productApi' }
      * call read('classpath:com/peapod/example/resilience/scenarios/reset-count.feature')
      * def status = call read('classpath:com/peapod/example/resilience/scenarios/get-switch.feature') { 'breakerName': 'productApi' }
      * match status.breakerState == 'CLOSED'
      # 7 searches, all failures. breaker should open on 5th failed call
      * def performedSearches = performSearches(7, resilienceProductAPISvcLocationId, "Error")
      * def status = call read('classpath:com/peapod/example/resilience/scenarios/get-switch.feature') { 'breakerName': 'productApi' }
      # circuit breaker should have opened on the fifth call and the downstream service should have only been called 5 times
      * match status.breakerState == 'OPEN'
      # When the switch is forced closed
      * call read('classpath:com/peapod/example/resilience/scenarios/close-switch.feature') { 'breakerName': 'productApi' }
      * def status = call read('classpath:com/peapod/example/resilience/scenarios/get-switch.feature') { 'breakerName': 'productApi' }
      * match status.breakerState == 'CLOSED'
      # Then request will go through to the downstream service and the breaker will open on a subsequent set of errors
      * def performedSearches2 = performSearches(7, resilienceProductAPISvcLocationId, "Error")
      * def status = call read('classpath:com/peapod/example/resilience/scenarios/get-switch.feature') { 'breakerName': 'productApi' }
      * match status.breakerState == 'OPEN'
      * def countResponse = call read('classpath:com/peapod/example/resilience/scenarios/check-count.feature')
      * match countResponse.count == 10

    @resilience-slow-scenario
    Scenario: Verify that slow calls will trip the breaker
      # Given that the breaker is closed
      Given call read('classpath:com/peapod/example/resilience/scenarios/close-switch.feature') { 'breakerName': 'productApi' }
      And call read('classpath:com/peapod/example/resilience/scenarios/reset-count.feature')
      And def status = call read('classpath:com/peapod/example/resilience/scenarios/get-switch.feature') { 'breakerName': 'productApi' }
      * match status.breakerState == 'CLOSED'
      # 7 searches, all slower than allowed. breaker should open on 5th failed call
      # The mock is set to return only after 800 milliseconds. The breaker is set to consider a call slow if it takes more than 100 milliseconds
      * def performedSearches = performSearches(7, resilienceProductAPISvcLocationId, "Slow")
      * def status = call read('classpath:com/peapod/example/resilience/scenarios/get-switch.feature') { 'breakerName': 'productApi' }
      # circuit breaker should have opened on the fifth call and the downstream service should have only been called 5 times
      * match status.breakerState == 'OPEN'
      * def countResponse = call read('classpath:com/peapod/example/resilience/scenarios/check-count.feature')
      * match countResponse.count == 5

    @scenario-force-open-and-recover
    Scenario: Verify that the breaker is disabled if it is forced open
      # Given the switch is forced open and the permittedNumberOfCallsInHalfOpenState is set to 2
      Given call read('classpath:com/peapod/example/resilience/scenarios/open-switch.feature') { 'breakerName': 'productApi' }
      # And the maxWaitDurationInHalfOpenState is 0, which means during normal operation, 2 calls must be successful to close the circuit
      And def status = call read('classpath:com/peapod/example/resilience/scenarios/get-switch.feature') { 'breakerName': 'productApi' }
      * match status.breakerState == 'FORCED_OPEN'
      And call read('classpath:com/peapod/example/resilience/scenarios/reset-count.feature')
      # When the client makes what should be three successful calls
      When def performedSearches = performSearches(3, resilienceProductAPISvcLocationId, "Product")
      Then def status = call read('classpath:com/peapod/example/resilience/scenarios/get-switch.feature') { 'breakerName': 'productApi' }
      # Then the circuit breaker should still be in FORCED_OPEN state and the downstream service should not have been called
      And match status.breakerState == 'FORCED_OPEN'
      And def countResponse = call read('classpath:com/peapod/example/resilience/scenarios/check-count.feature')
      And match countResponse.count == 0







