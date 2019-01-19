var express = require('express')
var router = express.Router()
var smartcar = require('smartcar')

// TODO: Authorization Step 1a: Launch Smartcar authentication dialog
const client = new smartcar.AuthClient({
    clientId: "9bc9e1a5-e91a-4fc1-beda-858090985de4",
    clientSecret: "174828ce-3a3b-4854-8e25-e28d397795c9",
    redirectUri: "http://localhost:3000/login/exchange",
    scope: ['read_vehicle_info'],
    testMode: true,
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

router.get('/exchange', function (req, res) {
    // TODO: Authorization Step 1b: Launch Smartcar authentication dialog
    // TODO: Authorization Step 3: Handle Smartcar response
    const code = req.query.code;
    return client.exchangeCode(code)
    .then(function (_access) {
        // in a production app you'll want to store this in some kind of persistent storage
        access = _access;
        
        res.sendStatus(200, access);
    })
})

router.get('/refresh', function(req, res) {
    const token = req.query.token

    return client.exchangeRefreshToken(token)
        .then(function (_access) {
            // in a production app you'll want to store this in some kind of persistent storage
            access = _access;

            res.sendStatus(200, access);
        })
})

module.exports = router