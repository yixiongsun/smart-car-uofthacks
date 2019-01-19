var express = require('express')
var router = express.Router()
var smartcar = require('smartcar')

router.get('/vehicles', function (req, res) {
    var token = req.query.token
    if (!token) {
        return res.send(400, "Invalid arguments")
    }
    return smartcar.getVehicleIds(token)
        .then(function (data) {
            // the list of vehicle ids
            res.send(data.vehicles)
        })
})

router.get('/info', function (req, res) {
    var token = req.query.token
    var vehicleId = req.query.vehicleId
    if (!token || !vehicleId) {
        return res.send(400, "Invalid arguments")
    }
    try {
        const vehicle = new smartcar.Vehicle(vehicleId, token);
        res.send(vehicle.info())
    } catch (error) {
        console.log(error)
        res.send(200)
    }
})

router.get('/location', function (req, res) {
    var token = req.query.token
    var vehicleId = req.query.vehicleId
    if (!token || !vehicleId) {
        return res.send(400, "Invalid arguments")
    }
    try {
        const vehicle = new smartcar.Vehicle(vehicleId, token);
        res.send(vehicle.location())
    } catch (error) {
        res.send(200)
    }
})

router.get('/disconnect', function (req, res) {
    var token = req.query.token
    var vehicleId = req.query.vehicleId
    if (!token || !vehicleId) {
        return res.send(400, "Invalid arguments")
    }
    try {
        const vehicle = new smartcar.Vehicle(vehicleId, token);
        vehicle.disconnect()
    } catch (error) {
        console.log(error)
        res.send(400)
    }
})

router.get('/unlock', function (req, res) {
    var token = req.query.token
    var vehicleId = req.query.vehicleId
    if (!token || !vehicleId) {
        return res.send(400, "Invalid arguments")
    }
    try {
        const vehicle = new smartcar.Vehicle(vehicleId, token);
        vehicle.unlock()
        res.send(200)
    } catch (error) {
        console.log(error)
        res.send(400)
    }
})

router.get('/lock', function (req, res) {
    var token = req.query.token
    var vehicleId = req.query.vehicleId
    if (!token || !vehicleId) {
        return res.send(400, "Invalid arguments")
    }
    try {
        const vehicle = new smartcar.Vehicle(vehicleId, token);
        vehicle.lock()
        res.send(200)
    } catch (error) {
        console.log(error)
        res.send(400)
    }
})

module.exports = router