const admin = require('firebase-admin');

var serviceAccount = require('../smart-car-ufothacks-5ecdd9b6ef4d.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

var db = admin.firestore();

module.exports  = {
    // either creates new doc or adds/updates data in doc
    addData: function (appId, vehicles) {
        var docRef = db.collection('users').doc(appId)
        let doc = docRef.set(vehicles, {merge: true})
        
    },
    
    addOdometerData: function (appId, vehicleId, data) {
        var docRef = db.collection('users').doc(appId)
        var ref = vehicleId + ".odometer" 
        let doc = docRef.update({ref: admin.firestore.FieldValue.arrayUnion(data)}) 
    },

    addLocationData: function (appId, vehicleId, data) {
        var docRef = db.collection('users').doc(appId)
        var ref = vehicleId + ".location" 
        let doc = docRef.update({ref: admin.firestore.FieldValue.arrayUnion(data)}) 
    }
}

