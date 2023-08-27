import requests
import json
url = "http://localhost:8080/customers/add"
customer = {
        'customerId': 123455,
        'firstName': "Pierce",
        'lastName': "Doherty",
        'email': "pdoh@gmail.com",
        'phone': "0871234567",
        'address': "123 Main Street",
        'city': "Dublin",
        'country': "Ireland",
        'postcode': "D01 AB23",
        'reviewCollection': [],
        'ordersCollection': []
}
response = requests.post(url,json = customer)
print(response.status_code,response.text)