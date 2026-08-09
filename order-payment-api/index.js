const express = require('express');
const mongoose = require('mongoose');
const OrderRoutes = require('./routes/OrderRoutes');
const bodyParser = require("body-parser");

const app = express();
const PORT = 3000;

// Middleware
app.use(express.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// MongoDB connection
mongoose.connect("mongodb://localhost:27017/eadp", {

}).then(() => {
    console.log("Connected to MongoDB");
}).catch((error) => {
    console.log("Failed to connect to MongoDB", error);
});

// Routes
app.use("/api", OrderRoutes);

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});