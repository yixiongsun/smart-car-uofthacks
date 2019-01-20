var express = require('express')
var router = express.Router()
var smartcar = require('smartcar')

router.get('/vehicles', function (req, res) {
    var token = req.query.token
    if (!token) {
        return res.send(400, "Invalid arguments")
    }
    return smartcar.getVehicleIds(token)
        .then(async function (data) {
            // the list of vehicle ids
            try {
                var vehicles = []
                for (var i = 0; i < data.vehicles.length; i++) {

                    const vehicle = new smartcar.Vehicle(data.vehicles[i], token)
                    let v = await vehicle.info()
                    vehicles.push(v)
                }
                res.send(vehicles)
            } catch (error) {
                console.log(error)
                return res.send(error)
            }
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

router.get('/location', async function (req, res) {
    var token = req.query.token
    var vehicleId = req.query.vehicleId
    if (!token || !vehicleId) {
        return res.send(400, "Invalid arguments")
    }
    try {
        const vehicle = new smartcar.Vehicle(vehicleId, token);
        let out = await vehicle.location()
        res.send(out.data)
    } catch (error) {
        res.send(200)
    }
})

router.get('/odometer', async function (req, res) {
    var token = req.query.token
    var vehicleId = req.query.vehicleId
    if (!token || !vehicleId) {
        return res.send(400, "Invalid arguments")
    }
    try {
        const vehicle = new smartcar.Vehicle(vehicleId, token);
        let out = await vehicle.odometer()
        res.send(out.data)
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
        res.send(200)
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