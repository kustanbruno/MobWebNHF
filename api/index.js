const express = require('express');
const app = express();
const bodyParser = require("body-parser");
const userController = require('./controllers/user');
const todoController = require('./controllers/todo');

app.use(express.static('static'));
app.use(bodyParser.urlencoded());
app.use(bodyParser.json());


userController(app);
todoController(app);

var server = app.listen(3001, function () {
});
console.log('Server running on port 3001');