const fs = require('fs');
const jwt = require('jsonwebtoken');

class JWT{
    constructor(){
        this.pvk = fs.readFileSync('./keys/key.private');
        this.pbk = fs.readFileSync('./keys/key.public');
        var i = 'AUT';
        var s = 'a@b.c';
        var a = 'asd';
        this.signOptions = {
            issuer: i,
            subject: s,
            audience: a,
            expiresIn: "12h",
            algorithm: 'RS256'
        }
        this.verifyOptions = {
            issuer:  i,
            subject:  s,
            audience:  a,
            expiresIn:  "12h",
            algorithm:  ["RS256"]
           };
    }

    generate(payload){
        return jwt.sign(payload, this.pvk, this.signOptions);
    }

    verify(token){
        try{
            return jwt.verify(token, this.pbk, this.verifyOptions);
        }catch(err){
            return -1;
        }
    }
}

module.exports = new JWT();