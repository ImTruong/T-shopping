$(document).ready(function() {
    const token = localStorage.getItem("token");
    const headFileLink = "http://localhost:8080/files/";

    function calculateTotal() {
        let subTotal = 0;
        $('.product-check:checked').each(function() {
            const row = $(this).closest('tr');
            const price = parseFloat($(this).data('price'));
            const quantity = parseInt(row.find('.product-quantity').val());
            const total = price * quantity;
            subTotal += total;
        });
        $('.cart_btn_cntnt span').text(`$${subTotal.toFixed(2)}`);
    }

    function updateRowPrice(row) {
        const price = parseFloat(row.find('.product-check').data('price'));
        const quantity = parseInt(row.find('.product-quantity').val());
        const total = price * quantity;
        row.find('.cart_page_totl').text(`$${total.toFixed(2)}`);
    }

    function fetchCartData() {
        $.ajax({
            url: `http://localhost:8080/carts/users`,
            method: 'GET',
            headers: {
                'Authorization': `${token}`
            }
        })
        .done(function(response) {
            if (response.success) {
                updateCartUI(response.data);
            } else {
                alert("Failed to load cart data");
            }
        });
    }

    function updateCartUI(cart) {
        let html = '';
        let count = 1;

        cart.productList.forEach(element => {
            const totalPrice = element.price * element.quantity;

            html += `
                <tr>
                    <td style="padding: 10px;">
                        <input type="checkbox" class="product-check" data-product-id="${element.id}" data-price="${element.price}" data-quantity="${element.quantity}" />
                        ${count++}
                    </td>
                    <td style="padding: 10px;">
                        <div style="display: flex; align-items: center;">
                            <figure style="margin: 0 10px;">
                                <img src="${headFileLink}${element.images[0]}" alt="${element.productName}" style="width: 66px; height: 65px;" />
                            </figure>
                            <div>
                                <h1 style="font-size: 16px; margin: 0;">${element.productName}</h1>
                            </div>
                        </div>
                    </td>
                    <td class="cart_page_price" style="padding: 10px;">$${element.price.toFixed(2)}</td>
                    <td style="padding: 10px;">
                        <input value="${element.quantity}" type="number" class="product-quantity" style="width: 50px;" min="1" />
                    </td>
                    <td class="cart_page_totl" style="padding: 10px;">$${totalPrice.toFixed(2)}</td>
                    <td style="padding: 10px;">
                        <button class="remove-btn" data-product-id="${element.id}" style="background-color: #f44336; color: white; border: none; padding: 5px 10px; cursor: pointer;">x</button>
                    </td>
                </tr>`;

        });

        html += `
            <tr>
                <td></td>
                <td class="shop_btn_wrapper shop_car_btn_wrapper" style="padding: 10px;">
                    <ul style="list-style-type: none; padding: 0; margin: 0;">
                        <li><a href="#" class="checkout-btn" style="text-decoration: none; padding: 10px 20px; background-color: #f0c040; color: white;">Checkout</a></li>
                    </ul>
                </td>
                <td></td>
                <td></td>
                <td></td>
                <td class="cart_btn_cntnt" style="padding: 10px;">Sub Total: <span>$0.00</span></td>
                <td></td>
            </tr>`;

        $('#itemCart').html(html);
        calculateTotal(); // Update the subtotal on load

        // Event listeners
        $('.product-check').change(calculateTotal);
        $('.product-quantity').change(function() {
            const row = $(this).closest('tr');
            updateRowPrice(row);
            calculateTotal(); // Update subtotal when quantity changes
        });

        // Handle the removal of items
        $('.remove-btn').click(function(e) {
            e.preventDefault();
            let productId = $(this).data('product-id');
            $.ajax({
                url: `http://localhost:8080/carts/users/${productId}`,
                method: 'DELETE',
                headers: {
                    'Authorization': `${token}`
                }
            })
            .done(function(msg) {
                if (msg.success) {
                    fetchCartData();  // Refresh cart data after deletion
                } else {
                    alert("Failed to remove item from cart");
                }
            });
        });

        // Handle checkout button click
        $('.checkout-btn').click(function(e) {
            e.preventDefault();
            const selectedItems = [];
            $('.product-check:checked').each(function() {
                const row = $(this).closest('tr');
                const productId = $(this).data('product-id');
                const quantity = row.find('.product-quantity').val();
                selectedItems.push({ id: productId, quantity: parseInt(quantity) });
            });

            // Gửi request checkout
            $.ajax({
                url: `http://localhost:8080/orders/temporary`,
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(selectedItems),
                headers: {
                    'Authorization': `${token}`
                }
            })
            .done(function(msg) {
                if (msg.success) {
                    alert("Checkout successful! Order ID: " + msg.data);
                    localStorage.setItem("temporaryOrderId",msg.data)
                } else {
                    alert("Checkout failed!");
                }
            })
            .fail(function() {
                alert("An error occurred during checkout.");
            });
        });
    }

    // Khởi tạo dữ liệu giỏ hàng
    fetchCartData();
});
