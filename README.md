# 🐾 Pettopia

Pettopia is a **Spring Boot REST API** designed to manage customer and pet services for a pet-related business. The application provides several CRUD operations, caching, scheduled tasks, and integration with third-party APIs for a comprehensive pet management system.

## 📋 Features

### Customer Management
- **Endpoints**:
  - Retrieve all customers
  - Get, add, update, or delete a customer by ID
  - Fetch orders by customer ID
- **Features**:
  - Responses cached; updates evict cache
  - Cache auto-cleared every 10 seconds (configurable)
  - JSON and XML support
  - HATEOAS links for customer-related requests

### Invoices
- Generate PDF invoices for specific orders or all customer orders.
![image](https://user-images.githubusercontent.com/38580052/228236887-703d8ce9-76d0-4a1a-8123-7f3060a134c7.png)
### Pet Health
- **Endpoints**:
  - Get pet by ID
  - Check pet health and schedule checkups
  - Send email reminders for upcoming checkups
- Track checkups and vaccinations.

### Pet Events
- Manage pet-related events (e.g., add events, generate posters/QR codes).

### Pet Media & News
- Fetch random dog images and latest pet-related news via third-party APIs.
Both of these features are making use of third party APIs ([Dog](https://thedogapi.com/), [News](https://newsapi.org/))

## 🚀 Technologies
- **Backend**: Java, Spring Boot
- **Caching**: Configurable scheduled tasks
- **PDF Generation**: iText
- **APIs**: Integrates with The Dog API and NewsAPI

## 🐍 Python Client Example
- Sample script provided to fetch all customers using Python's `requests` library.
```python
import requests
import json

url = "http://localhost:8080/customers"
response = requests.get(url)

response_json = response.json()
print(json.dumps(response_json, indent=4))
```

## 📜 License
This project is licensed under the MIT License.
