const mongoose = require('./../db');


//STATUS
//0 - active
//1 - done

const TodoSchema = new mongoose.Schema({
    name : {type: String, required: true},
    status : {type: Number, required: true},
    date : {type: Date, required: true},
    user: {type: mongoose.Schema.Types.ObjectId, required: true, ref: 'User'}
}, {toObject: {getters: true}, toJSON: {virtuals: true}});

TodoSchema.virtual('timestamp_ms').get(function() {
    return this.date.getTime();
});

const Todo = mongoose.model('Todo', TodoSchema);
module.exports = Todo;