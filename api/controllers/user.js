const User = require('./../models/user');
const JWT = require('./../jwt');

function addUser(req, res){
    let u = new User({email: req.body.email, pass: req.body.pass});
    u.save().then(function(){
        res.json(u);
    }).catch(function(err){
        res.json(err).status(500);
    })
}

function login(req, res){
    let u = User.findOne({email: req.body.email}).then(function(u){
        if(u){
            if(u.pass == req.body.pass)
                res.json({token:JWT.generate({id: u._id})});
            else
                res.status(406).json({message: "Wrong email or password"});
        }else{
            res.status(406).json({message: "Wrong email or password"});
        }
    }).catch(function(err){
        res.json(err).status(500);
    })
}

//routes
module.exports = function(app){
    app.put('/user', addUser);
    app.post('/login', login);
}; 