const mongoose = require('mongoose');

const uri = 'mongodb://127.0.0.1:27017/MOBWEB';

mongoose.connect(uri, 
    {
        useNewUrlParser: true,
        useUnifiedTopology: true
    }, 
    (err) =>{
    if(err){
        console.log('err.message');
    }else{
        console.log('db connection succesfull');
    }
})

module.exports = mongoose;