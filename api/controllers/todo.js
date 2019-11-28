const Todo = require('./../models/todo');
const JWT = require('./../jwt');

function addTodo(req, res){
    let t = new Todo({name: req.body.name, status: 0, date: new Date(), user: req.userId});
    t.save().then(function(t){
        res.json(t);
    }).catch(function(err){
        res.json(err).status(500);
    })
}

function deleteTodo(req, res){
    let t = Todo.findById(req.params.todoId).then(function(t){
        if(t.user == req.userId){
            t.delete();
            res.json({message:'delete complete'});
        }
        else
            unauthorized(res);
    }).catch(function(err){
        res.json(err).status(500);
    })
}

function setTodoStatus(req, res){
    let t = Todo.findById(req.params.todoId).then(function(t){
        if(t.user == req.userId){
            t.status = req.body.status;
            t.save();
            res.json({message: 'Status change done'});
        }
        else
            unauthorized(res);
    }).catch(function(err){
        res.json(err).status(500);
    })
}

function getTodoList(req, res){
    let t = Todo.find({user: req.userId}).then(function(t){
        res.send(t);
    }).catch(function(err){
        res.json({message: 'Error while looking for todos'}).status(500);
    })
}

function authenticate(req, res, next){
    if(req.headers.authorization === undefined){
        unauthorized(res);
    }else{
        let token = req.headers.authorization.replace('Bearer ', '');
        let v = JWT.verify(token);
        if(v == -1)
            unauthorized(res);
        else{
            req.userId = v.id;
            next();
        }
    }
}

function unauthorized(res){
    res.json({message: 'unathorized'}).status(403);
}

//routes
module.exports = function(app){
    app.put('/todo', authenticate, addTodo);
    app.delete('/todo/:todoId', authenticate, deleteTodo);
    app.post('/setStatus/:todoId', authenticate, setTodoStatus);
    app.get('/todos', authenticate, getTodoList);
}