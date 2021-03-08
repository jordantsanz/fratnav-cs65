// import firebase functions modules
const functions = require('firebase-functions');
// import admin module
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.pushNotificationLike = functions.database.ref('/houses/{houseId}')
    .onWrite(async (change, context) => {
    console.log('Push notification event triggered');
    console.log(context, 'context');
    console.log(change, 'change');

    //  Get the current value of what was written to the Realtime Database.
    const valueObject = change.after.val();
    console.log(valueObject, 'valueObject');
    console.log(valueObject.houseName, 'houseName');

    // Create a notification
    const payload = {
      notification: {
        title: 'New Post',
        body: `A new post was made by ${valueObject.houseName}!`,
      },
    };

        // Create an options object that contains the time to live for the notification and the priority
        const options = {
          priority: 'high',
          timeToLive: 60 * 60 * 24,
        };

        return admin.messaging().sendToTopic(valueObject.houseName, payload, options);
  });