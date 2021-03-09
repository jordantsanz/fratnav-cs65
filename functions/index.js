// import firebase functions modules
const functions = require('firebase-functions');
// import admin module
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.pushNotificationLike = functions.database.ref('/houses/{houseId}/posts')
    .onWrite(async (change) => {

    console.log(change, 'change');

    return change.after.ref.parent.once("value").then(snap => {
    const house = snap.val();

        console.log('Push notification event triggered');
        console.log(change, 'change');

        console.log(house, 'valueObject');
        console.log(house.houseName, 'houseName');
            // Create a notification
            const payload = {
              notification: {
                title: 'New Post',
                body: `A new post was made by ${house.houseName}!`,
              },
            };

                // Create an options object that contains the time to live for the notification and the priority
                const options = {
                  priority: 'high',
                  timeToLive: 60 * 60 * 24,
                };

                return admin.messaging().sendToTopic(house.houseName, payload, options);

    });

  });