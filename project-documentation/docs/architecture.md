# Design

[High-level description of the service's architecture.]

## TOC
- [Technologies](#technologies)
- [Data](#data)
- [Security](#security)
- [Dependencies](#dependencies)
  - [Dependency](#dependency)
- [Clients](#clients)
  - [Client](#client)
- Flows
  - [Flow](#flow) 
- [Decisions](#decisions)   

### Technologies

[This section should contain a list of the technologies used in the service, e.g., Spring Boot, Kotlin, Hazelcast, etc.]

### Data (optional)

[This section should contain a description of the data model used in the service, e.g., ERD, UML, etc.]
[Questions to answer]
- What data does this service store and why?
- What are the relationships between the data elements?
- Any notes on data storage, e.g., encryption, hashing, etc.
- Any use of data caching or data replication?
- Any use of data partitioning or sharding?
- Any use of data archiving or data purging?
- Any dependence on data import jobs, triggers, or stored procedures?

```mermaid
---
title: Customer example
---
erDiagram
    CUSTOMER ||--o{ PREFERENCES : has
    CUSTOMER ||--o{ DELIVERY_ADDRESS : has
    CUSTOMER ||..o{ ATTRIBUTE : has

    CUSTOMER {
        bigint id 
        string handle UK  "Must be email address"
        string email UK "Must be unique. Typically same as handle. Legacy Peapod customer excepting" 
        string loyaltyCard UK "Opco Loyalty Card - Can only belong to one web customer account" 
        string type "M - Merchant, C- Customer" 
    }
    PREFERENCES {
        string id
        string customerId
        string preference
    }
    DELIVERY_ADDRESS {
        string id
        string customerId
        string address
    } 
```

### Security

[This section should contain a description of the security model used in the service, e.g., OAuth2, JWT, etc.]
[Questions to answer:]
1. Does this service have any security requirements?
2. Does this data use encryption at rest or in transit?
3. Deos this service store PII or PCI data?
   a. If so, how is it protected?
   b. Links to the data protection policy? 


## Dependencies

[Diagram of the dependencies of the service]

```mermaid
graph LR
    subgraph System
       service[This Service]
       dep1[Dependency1]
       dep2[Dependency2]
            
       service --> dep1
       service --> dep2
    end
    extDep1[External Dependency1]

    service --> extDep1
```
### [Dependency]

[A detailed description of this service's dependence on another service, datastore, etc.]

[If Ahold Dependency, a URL to the Github code repository of the dependency]

Question that should be answered in this section:
- Why does my service depend upon this other service or datastore?
- What information does it need?
- What functionality of this service does the dependency support?
- If the dependency is unavailable, what happens to this service?
- Can this service function without the dependency?
  - If so, what does the functionality look like, alternative sources of data, empty responses, errors?
 

## Clients

[Diagram of the clients of the service]

```mermaid
graph LR
    subgraph Prism
        service[This Service]    
        client1[Client 1]
        client2[Client 2]
            
        client1 --> service  
        client2 --> service
    end
    extClient1[External Client 1]
    extClient1 --> service
```

### [Client]

[A detailed description of the clients of this service]

[If Ahold Client, a URL to the Github code repository of the client]

Questions that should be answered in this section:
- Why is the client using this service? 
- What information does the client need?
- What functionality of this client is this service supporting?
- Can the client function without this service?

## Decisions

[List of the links to architectural decision records (ADRs) for this service]

- [ADR](./adr.md)
