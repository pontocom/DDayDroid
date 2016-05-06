/**
 * Created by cserrao on 18/02/15.
 */
var express = require('express'),
    bodyParser = require('body-parser'),
    ddday = require('./ddday.js');

var app = express();

app.use(bodyParser.urlencoded({
    extended: true
}));
app.use(bodyParser.json());

app.get('/ddday', ddday.getDestination);
app.post('/ddday', ddday.shareDestination);
app.post('/ddday/add', ddday.addNewDestination);

var server = app.listen(3000, function () {

    var host = server.address().address
    var port = server.address().port

    console.log('Listening at http://%s:%s', host, port)

});
