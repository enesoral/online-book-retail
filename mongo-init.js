db = new Mongo().getDB("bookRetailDB");

db.createCollection('book', {capped: false});
db.book.createIndex({"isbn": 1}, {unique: true});
db.book.createIndex({"name": 1}, {unique: true});

db.createCollection('user', {capped: false});
db.customer.createIndex({"email": 1}, {unique: true});

db.createCollection('statistic', {capped: false});

db.createCollection('order', {capped: false});