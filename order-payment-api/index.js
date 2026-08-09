const express = require('express');
const mongoose = require('mongoose');
const OrderRoutes = require('./routes/OrderRoutes');
const bodyParser = require("body-parser");
const {Eureka} = require("eureka-js-client");

const app = express();
const PORT = 3000;
const EUREKA_SERVER = 'localhost';

// Middleware
app.use(express.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// Eureka Client
const eurekaClient = new Eureka({
    instance: {
        app: 'order-payment-api',
        instanceId: `order-payment-api:${PORT}`,
        hostName: 'localhost',
        ipAddr: '127.0.0.1',
        port: {
            '$': PORT,
            '@enabled': true
        },
        vipAddress: 'order-payment-api',
        statusPageUrl: `http://localhost:${PORT}/status`,
        dataCenterInfo: {
            '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
            name: 'MyOwn'
        }

    },

    eureka: {
        host: EUREKA_SERVER,
        port: 8761,
        servicePath: '/eureka/apps/'
    }
});


// MongoDB connection
mongoose.connect("mongodb://localhost:27017/eadp", {

}).then(() => {
    console.log("Connected to MongoDB");
}).catch((error) => {
    console.log("Failed to connect to MongoDB", error);
});

// Routes
app.use("/api", OrderRoutes);

// Start server
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});


// Start Eureka Client
eurekaClient.start((error) => {
    if (error) {
        console.log('Eureka registration failed:', error);
    } else {
        console.log('Order Payment API registered with Eureka');
    }
});