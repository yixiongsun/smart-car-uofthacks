const express = require('express')
const app = express()
const port = process.env.PORT || 3000
const auth = require('./routes/auth')
const vehicle = require('./routes/vehicles')
app.use('/login', auth)
app.use('/vehicle', vehicle)

app.get('/', (req, res) => res.send('Hello World!'))

app.listen(port, () => console.log(`Example app listening on port ${port}!`))