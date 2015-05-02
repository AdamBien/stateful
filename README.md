# stateful
A RESTful statemachine using the [scxml w3c](http://www.w3.org/TR/scxml/) for configuration. Stateful uses: [Apache Commons SCXML v2](http://commons.apache.org/proper/commons-scxml/) as state machine.


# requirements
Drop the statefulapp.war into a Java EE 7 application server on Java 8 (tested on WildFly, GlassFish, Payara)

# usage
## create a state machine from SXML file
```
curl -i -H”Content-type: application/xml” -XPUT —data-binary @“src/test/resources/state.xml” http://localhost:8080/statefulapp/resources/machines/duke
```
Sample file:
```xml
<scxml xmlns=“http://www.w3.org/2005/07/scxml”
       version=“1.0”
       initial=“indexpage”>

    <state id=“indexpage”>
        <transition event=“login”   target=“authenticated”/>
    </state>

    <state id=“authenticated”>
        <transition event=“browse”   target=“browsing”/>
        <transition event=“logout”    target=“unauthenticated”/>
    </state>

    <state id=“browsing”>
        <transition event=“logout” target=“unauthenticated”/>
    </state>

    <state id=“unauthenticated”>
        <transition event=“authenticate”   target=“index page”/>
    </state>
</scxml>
```
Response:

```
HTTP/1.1 201 Created
Location: http://localhost:8080/statefulapp/resources/machines/duke
(…)
```

## retrieve SXML definition for a particular machine
```
curl http://localhost:8080/statefulapp/resources/machines/duke
```
Response:
```
HTTP/1.1 200 OK
Content-Type: application/json
(…)
[”duke”]% 
```

## list all installed state machine names
```
curl http://localhost:8080/statefulapp/resources/machines/ 
```
## retrieve the current event and next transitions

```
curl http://localhost:8080/statefulapp/resources/machines/duke/states 
```
Response:

```
HTTP/1.1 200 OK
Content-Type: application/json
(…)
```
```json
{“current-state”:[“index page”],”next-transitions”:[{“login”:[“authenticated”]}]}
```
## trigger a transition
```
curl -i -XPUT -H’Content-type: application/json’ -d’{“event”:”login”}’ http://localhost:8080/statefulapp/resources/machines/duke/states
```
Response:
```
HTTP/1.1 200 OK
Content-Type: application/json
(…)
```

```json
{“current-state”:[“authenticated”],”next-transitions”:[{“browse”:[“browsing”],”logout”:[“unauthenticated”]}]}
```
## reset state machine
```
curl -i -XDELETE http://localhost:8080/statefulapp/resources/machines/duke/states
```
Response:
```
HTTP/1.1 200 OK
```

```json
{“current-state”:[“index page”],
”next-transitions”:[{“login”:[“authenticated”]}]}
```