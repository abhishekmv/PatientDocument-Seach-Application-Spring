# Patient Document Search Application
This is a simple search application over a set of patient records. Using this we can case insensitive partial word search over patient data. We can also do other operations such as CRUD patients and for each patient, we can CRUD 'n' medical documents.

### Build and start the docker service on port 8080
```
bash start_app.sh
```

### Stop all the docker service on port 8080
```
bash stop_app.sh
```

## GET: Health API Endpoint
```
curl http://localhost:8080/patients/health; echo
curl http://localhost:8080/documents/health; echo
```

## GET: Swagger API Endpoint
```
curl http://localhost:8080/swagger-ui.html#; echo
curl http://localhost:8080/v2/api-docs; echo
```
# Below are available API endpoints:
## All patient API endpoints

### GET: Word search all the patient details
```
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/patients/search?query=patient'
```

### GET: All the patient details
```
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/patients'
```

### GET: A patient a single patient detail
```
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/patients/1'
```

### POST: Add a new patient
```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"patientname": "Tom smith"}' 'http://localhost:8080/patients/'
```

### PUT: Update existing patient details
```
curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"patientname": "Johnny Bravo"}' 'http://localhost:8080/patients/1'
```

### DELETE: Delete a single patient and all their documents
```
curl -X DELETE --header 'Accept: */*' 'http://localhost:8080/patients/1'
```

## All patient documents API endpoints
### GET: All the patients documents

```
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/documents/all'
```

### GET: All the documents for a single patient
```
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/patient/1/documents'
```

### GET: A patient document for a single patient
```
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/patient/4/document/2'
```

### POST: Create a patient document
```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"title": "Blood check report","document": " This is the report of a patient named Tim got his blood check today."}' 'http://localhost:8080/patient/5/documents'
```

### PUT: Update a patient document
```
curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"title": "Physical report","document": "This is the physical report of the patient Jhonny"}' 'http://localhost:8080/patient/1/document/4'
```

### DELETE: Delete a patient document
```
curl -X DELETE --header 'Accept: application/json' 'http://localhost:8080/patient/3/document/7'
```

## Cleanup metadata and dangling docker containers
#### NOTE: This cleanup script will remove all the running docker container. Don't run this if you have any other personal containers running.
```
bash cleanup.sh
```
