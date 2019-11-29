const Todo = require('./../models/todo');
const JWT = require('./../jwt');

function addTodo(req, res){
    let t = new Todo({name: req.body.name, status: 0, date: new Date().getTime(), user: req.userId});
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

function editTodoName(req, res){
    let t = Todo.findById(req.params.todoId).then(function(t){
        if(t.user == req.userId){
            t.name = req.body.name;
            t.save();
            res.json({message: 'Name change done'});
        }else{
            unauthorized(res);
        }
    }).catch(function(err){
        res.json(err).status(500);
    })
}

function getTodoList(req, res){
    let t = Todo.find({user: req.userId}).sort([['date', -1], ['status', 1]]).then(function(t){
        res.send({todos: t});
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
    res.status(403).json({message: 'unathorized'});
}

//routes
module.exports = function(app){
    app.put('/todo', authenticate, addTodo);
    app.delete('/todo/:todoId', authenticate, deleteTodo);
    app.patch('/setStatus/:todoId', authenticate, setTodoStatus);
    app.get('/todos', authenticate, getTodoList);
    app.patch('/todoName/:todoId', authenticate, editTodoName);
}