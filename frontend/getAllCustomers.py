import requests
import json

url = "http://localhost:8080/customers"
response = requests.get(url)

response_json = response.json()
print(json.dumps(response_json, indent=4))