const express = require("express");
const router = express.Router();
const OrderController = require("../controller/OrderController");

router.post("/place-order", OrderController.placeOrder);
router.get("/load-all-orders", OrderController.loadAllOrders);
router.put("/update-order-status/:id", OrderController.updateOrderStatus);
router.delete("/delete-order-by-admin/:id", OrderController.deleteOrderByAdmin);

module.exports = router;
