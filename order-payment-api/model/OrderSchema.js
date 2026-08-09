const mongoose = require("mongoose");
const OrderSchema = new mongoose.Schema({
    date: {
        type: Date,
        required: true
    },
    cost:{
        type: Number,
        required: true
    },
    customer:{
        type: Object,
        required: true
    },
    products: {
        type: Array,
        required: true
    },
    status: {
        type: Boolean,
        required: true
    },
    paymentData: {
        type: Object,
        required: true
    }
});

module.exports = mongoose.model("Order", OrderSchema);

