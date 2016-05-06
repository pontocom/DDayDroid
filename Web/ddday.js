/**
 * Created by cserrao on 18/02/15.
 */

var express = require('express'),
    uuid = require('node-uuid');

var _db = require('mongoskin').db('mongodb://localhost:27017/ddestinations');

//run mongod --dbpath ./data/

exports.getDestination = function(req, res) {
    // get a random record from the collection
    // this is not the best way to do this and may present a performance problem

    // count the number of records
    var random = 0;

    _db.collection('ddday').count(function(err, count) {
        console.log('There are ' + count + ' destinations in the database');

        random = Math.floor(Math.random()*count);
        console.log("Random =" + random);

        _db.collection('ddday').find({}).toArray(function(err, result) {
            console.log(result[random]);
            res.send(result[random]);
        });
    });

};

exports.shareDestination = function(req, res) {
    console.log(req.body);
    var date = new Date();

    var share = {
        UUID: req.body.UUID,
        email: req.body.email,
        facebook: req.body.FB,
        twitter: req.body.TW,
        POIid: req.body.POIid,
        date: date
    };

    console.log(share);

    _db.collection('shares').insert(share, function(err, result) {
        if(err) throw err;
        console.log(result);
    });

    res.send({status: "OK"});
};

exports.addNewDestination = function(req, res) {
    console.log(req.body);
    var uPOIid = uuid.v4();

    var dest = {
        POIid: uPOIid,
        POIname: req.body.POIname,
        POIdetails: req.body.POIdetails,
        POIlat: req.body.POIlat,
        POIlng: req.body.POIlng,
        POIimageURL: req.body.POIimageURL
    };

    _db.collection('ddday').insert(dest, function(err, result) {
        if(err) throw err;
        console.log(result);
    });

    res.send({status: "OK", message: "POI was added with ID = " + uPOIid});
};