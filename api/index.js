const express = require('express');
const app = express();
const bodyParser = require("body-parser");
const userController = require('./controllers/user');
const todoController = require('./controllers/todo');

app.use(express.static('static'));
app.use(bodyParser.urlencoded());
app.use(bodyParser.json());

app.use(log);


userController(app);
todoController(app);

var server = app.listen(3001, function () {
});
console.log('Server running on port 3001');


function log(req, res, next){
    var fullUrl = req.protocol + '://' + req.get('host') + req.originalUrl;
    console.log(`${req.method} ${fullUrl} \nBody: ${JSON.stringify(req.body)}\n`);
    next();
}