# :dog: Pettopia :dog:
- Spring Boot Application
## Customer CRUD :hammer_and_wrench:
#### Endpoints :books:
- Get all customers
- Get customer by ID
- Get orders by customer ID
- Delete customer by ID
- Add customer
- Edit customer by ID
#### Features :star2:
- All the endpoints interact with the caching. The GET endpoints cache the responses and the post, put, and delete endpoints evict the cache.
- There is a scheduled task to clear all cache every 10 seconds. The length can be configured in the `application.properties`
- HATEAOS is used to build the links on both customer get requests. 
- All endpoints support both JSON and XML.
#### Non-Java Client :snake:
Using Python and its Requests library, a script was built to consume two requests from the Pettopia server. Here is the code for the get all customers:
```python
import requests
import json

url = "http://localhost:8080/customers"
response = requests.get(url)

response_json = response.json()
print(json.dumps(response_json, indent=4))
```
This script sends a GET request to the server for all the customers, consumes the response and displays it in prettier JSON.
## Invoices :card_file_box:
There are two invoice endpoints available. One to get an invoice of a specific order, the other to get all orders from a customer for invoice.
The invoice is served up as a PDF and looks like this:
![image](https://user-images.githubusercontent.com/38580052/228236887-703d8ce9-76d0-4a1a-8123-7f3060a134c7.png)

## Diversification :bar_chart:
### Pet Health :ambulance:
#### Endpoints :books:
- Get pet by ID
- Get pet health by pet ID
- Check a pet into for a checkup
- Send checkup reminders via email

Each pet has its associated pet health entry. The pet health class holds all of the pets important information i.e. checkups, vaccinations, etc.
There is the ability to check a pet into a checkup. There is also an endpoint that will send an email to all pet owners that have a pet due a checkup within the next week.
### Pet Events :newspaper:
#### Endpoints :books:
- Get all events
- Add an event
- Get poster of upcoming events
- Get QR code of poster 
### Pet Media :camera_flash:
#### Endpoints :books:
- Get random photo of a dog
- Get 5 pet-related news articles

Both of these features are making use of third party APIs ([Dog](https://thedogapi.com/), [News](https://newsapi.org/))
