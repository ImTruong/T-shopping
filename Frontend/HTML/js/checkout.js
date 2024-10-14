$(document).ready(function () {
    var token = localStorage.getItem("token");
    var temporaryOrderId = localStorage.getItem("temporaryOrderId");  // Retrieve the temporary order ID from local storage
    var billingAddressId = null; // Initialize to store the addressId

    // Fetch the user information and billing details
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/users",
        headers: {
            "Authorization": token
        }
    })
    .done(function (msg) {
        var user = msg.data;

        if (!user.addressId) {
            disableBillingAddress();
            alert('Billing information is incomplete. Please provide a shipping address.');
            $('input[name="ship-to"][value="different-address"]').prop('checked', true);
        } else {
            billingAddressId = user.addressId; // Store the addressId

            // Populate billing information fields
            $('#billing-first-name').val(user.firstName);
            $('#billing-last-name').val(user.lastName);
            $('#billing-phone-number').val(user.phoneNumber);

            // Fetch the user's address details
            $.ajax({
                type: "GET",
                url: `http://localhost:8080/address/${billingAddressId}`,
                headers: {
                    "Authorization": "Bearer " + token
                }
            })
            .done(function (msg) {
                var address = msg.data;
                $('#billing-street-address').val(address.streetAddress);
                $('#billing-city').val(address.city);
                $('#billing-country').val(address.country);

                // Set the hidden input field with the addressId
                $('#billing-address-id').val(billingAddressId);

                validateBillingAddress();
            });
        }
    });

    // Fetch the temporary order details from Redis
    $.ajax({
        type: "GET",
        url: `http://localhost:8080/orders/temporary/${temporaryOrderId}`,
        headers: {
            "Authorization": token
        }
    })
    .done(function (msg) {
        var orderData = msg.data;

        if (orderData && orderData.productList && orderData.productList.length > 0) {
            var productListHTML = '';
            var subtotal = orderData.totalPrice;

            // Loop through the product list and build the HTML with quantity
            orderData.productList.forEach(function (product) {
                productListHTML += `
                    <li class="clearfix" style="display: flex; justify-content: space-between;">
                        <div style="width: 50%;"><strong>${product.productName}</strong></div>
                        <div style="width: 20%; text-align: center;">${product.quantity}</div>
                        <div style="width: 30%; text-align: right;">$${product.price.toFixed(2)}</div>
                    </li>
                `;
            });

            // Update the subtotal, shipping, and total
            var shippingCost = "Free Shipping";  // Modify this if needed
            var total = subtotal;  // Assuming free shipping

            productListHTML += `
                <li class="clearfix" style="display: flex; justify-content: space-between;">
                    <div style="width: 50%;"><strong>Sub Total</strong></div>
                    <div style="width: 30%; text-align: right;">$${subtotal.toFixed(2)}</div>
                </li>
                <li class="clearfix" style="display: flex; justify-content: space-between;">
                    <div style="width: 50%;"><strong>Shipping</strong></div>
                    <div style="width: 30%; text-align: right;" class="free">${shippingCost}</div>
                </li>
                <li class="clearfix" style="display: flex; justify-content: space-between;">
                    <div style="width: 50%;"><strong>TOTAL</strong></div>
                    <div style="width: 30%; text-align: right;">$${total.toFixed(2)}</div>
                </li>
            `;

            // Insert the HTML into the order-box section
            $('.order-box ul').html(productListHTML);
        } else {
            alert('No products found in the order.');
        }
    })
    .fail(function (jqXHR, textStatus, errorThrown) {
        console.log("Failed to fetch order: " + textStatus + ", " + errorThrown);
        alert('Failed to load order details.');
    });

    // Disable billing address fields if information is incomplete
    function disableBillingAddress() {
        const billingFields = [
            $('#billing-first-name'),
            $('#billing-last-name'),
            $('#billing-street-address'),
            $('#billing-city'),
            $('#billing-country'),
            $('#billing-phone-number')
        ];

        billingFields.forEach(field => field.prop('disabled', true));
        $('input[name="ship-to"][value="same-address"]').prop('disabled', true);
        $('input[name="ship-to"][value="different-address"]').prop('checked', true);
        $('#billing-explanation').html('<p class="alert alert-info">Billing information is incomplete. Please provide a shipping address.</p>');
    }

    // Validate the billing address fields to ensure all required information is present
    function validateBillingAddress() {
        const billingFields = [
            $('#billing-first-name').val(),
            $('#billing-last-name').val(),
            $('#billing-street-address').val(),
            $('#billing-city').val(),
            $('#billing-country').val(),
            $('#billing-phone-number').val()
        ];

        const sameAddressOption = $('input[name="ship-to"][value="same-address"]');
        const differentAddressOption = $('input[name="ship-to"][value="different-address"]');

        let billingComplete = billingFields.every(field => field !== '');

        if (!billingComplete) {
            disableBillingAddress();
        } else {
            sameAddressOption.prop('disabled', false); 
        }
    }

    // Handle order form submission (Place Order button click)
    $('#placeOrderButton').click(function (e) {
        e.preventDefault();  // Prevent default form submission

        var useBillingAddress = $('input[name="ship-to"]:checked').val() === "same-address";
        var orderData;

        if (useBillingAddress) {
            // Use the user's saved billing address
            orderData = {
                temporaryOrderId: temporaryOrderId,
                addressId: billingAddressId  // Use the stored addressId
            };
        } else {
            // Use the shipping address from the form
            orderData = {
                temporaryOrderId: temporaryOrderId,
                streetAddress: $('#shipping-street-address').val(),
                city: $('#shipping-city').val(),
                country: $('#shipping-country').val(),
                firstName: $('#shipping-first-name').val(),
                lastName: $('#shipping-last-name').val(),
                phoneNumber: $('#shipping-phone-number').val()
            };
        }

        // Send the order data to the backend
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/orders",
            headers: {
                "Authorization": token,
                "Content-Type": "application/json"
            },
            data: JSON.stringify(orderData),
            success: function(response) {
                if (response.success) {
                    alert("Order placed successfully!");
                    // Redirect or perform other actions as necessary
                } else {
                    alert("Failed to place order. Please try again.");
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("Failed to place order: " + textStatus + ", " + errorThrown);
                alert("An error occurred while placing the order.");
            }
        });
    });

});
