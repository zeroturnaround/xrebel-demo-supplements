1. Start MongoDB

   mongod --dbpath db

2. Generate test data

   use mydb;
   db.supplements.insert({"name":"apples", "price_id":1});
   db.supplements.insert({"name":"oranges", "price_id":2});
   db.supplements.insert({"name":"potatos", "price_id":3});
   db.supplement_prices.insert({"id":1, "value": 10});
   db.supplement_prices.insert({"id":2, "value": 12});
   db.supplement_prices.insert({"id":3, "value": 15});

