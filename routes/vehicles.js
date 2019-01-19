var express = require('express')
var router = express.Router()
var smartcar = require('smartcar')

router.get('/vehicles', function (req, res) {
    var token = req.token
    return smartcar.getVehicleIds(token)
        .then(function (data) {
            // the list of vehicle ids
            res.send(200, data.vehicles)
        })
})

router.get('/info', function (req, res) {
    var token = req.token
    var vehicleId = req.vehicleId
    const vehicle = new smartcar.Vehicle(vehicleId, token);
    res.send(200, vehicle.info())
})