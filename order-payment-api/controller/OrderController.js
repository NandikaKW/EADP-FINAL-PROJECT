const OrderSchema = require("../model/OrderSchema");
// Place order
const placeOrder = async (req, res) => {
    try {
        const { date, cost, customer, products, paymentData, status } = req.body;
        const newOrder = new OrderSchema({
            date,
            cost,
            customer,
            products,
            paymentData,
            status
        });
        const savedOrder = await newOrder.save();
        res.status(201).json({
            message: 'Order placed successfully',
            order: savedOrder
        });
    } catch (error) {
        res.status(500).json({
            message: 'Failed to place order',
            error: error.message
        });
    }
};

// Load all orders
const loadAllOrders = async (req, res) => {
    try {
        const orders = await OrderSchema.find();
        res.status(200).json(orders);
    } catch (error) {
        res.status(500).json({
            message: 'Failed to load orders',
            error: error.message
        });
    }
};

// Update order status
const updateOrderStatus = async (req, res) => {
    try {
        const { id } = req.params;
        const { status } = req.body;
        const updatedOrder = await OrderSchema.findByIdAndUpdate(
            id,
            { status },
            { new: true } // Return the updated document
        );

        if (!updatedOrder) {
            return res.status(404).json({
                message: 'Order not found'
            });
        }

        res.status(200).json({
            message: 'Order status updated successfully',
            order: updatedOrder
        });
    } catch (error) {
        res.status(500).json({
            message: 'Failed to update order status',
            error: error.message
        });
    }
};

// Delete order by admin
const deleteOrderByAdmin = async (req, res) => {
    try {
        const { id } = req.params;
        const deletedOrder = await OrderSchema.findByIdAndDelete(id);

        if (!deletedOrder) {
            return res.status(404).json({
                message: 'Order not found'
            });
        }

        res.status(200).json({
            message: 'Order deleted successfully',
            order: deletedOrder
        });
    } catch (error) {
        res.status(500).json({
            message: 'Failed to delete order',
            error: error.message
        });
    }
};

module.exports = {
    placeOrder,
    loadAllOrders,
    updateOrderStatus,
    deleteOrderByAdmin
}