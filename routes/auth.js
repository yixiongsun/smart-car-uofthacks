var express = require('express')
var router = express.Router()
var smartcar = require('smartcar')

// TODO: Authorization Step 1a: Launch Smartcar authentication dialog
const client = new smartcar.AuthClient({
    clientId: "9bc9e1a5-e91a-4fc1-beda-858090985de4",
    clientSecret: "174828ce-3a3b-4854-8e25-e28d397795c9",
    redirectUri: "https://smartcarconnect.net/login/exchange",
    scope: ['read_vehicle_info', 'read_location', 'read_odometer', 'control_security', 'control_security:unlock', 'control_security:lock', 'read_vin'],
    testMode: process.env.testMode | false,
});

router.get('/', function (req, res) {
    // TODO: Authorization Step 1b: Launch Smartcar authentication dialog
    try {
        const link = client.getAuthUrl();
        res.send(link)
    } catch (error) {
        if (error) {
            res.send(500)
        }
    }
})

router.get('/exchange', async function (req, res) {
    // TODO: Authorization Step 1b: Launch Smartcar authentication dialog
    // TODO: Authorization Step 3: Handle Smartcar response
    const code = req.query.code;

    try {
        let access = await client.exchangeCode(code)
        res.send(access);
    } catch (error) {
        console.log(error)
        res.send(400)
    }
})

router.get('/refresh', async function(req, res) {
    const token = req.query.token
    try {
        let access = await client.exchangeRefreshToken(token)
        res.send(access)
    } catch (error) {
        console.log(error)
        res.send(400)
    }
})

module.exports = router