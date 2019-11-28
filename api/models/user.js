const mongoose = require('./../db');

const UserSchema = new mongoose.Schema({
    email : {type: String, required: true},
    pass : {type: String, required: true}
});

const User = mongoose.model('User', UserSchema);
module.exports = User;