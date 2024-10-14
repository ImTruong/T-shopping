$(document).ready(function() {
    var token = localStorage.getItem("token");
    var headFileLink="http://localhost:8080/files/"
    fetchCartData();

    $(document).on('click', '.ss_btn', function(e) {
        e.preventDefault();
        
        let productId = $(this).closest('.ss_featured_products_box').find('input[name="productId"]').val();
        if(productId==null){
            let urlParams = new URLSearchParams(window.location.search);
            productId = urlParams.get('id');

        }
        let quantity=1
        let quantityVal = $("#input-quantity").val();
        if(quantityVal!=null)quantity=quantityVal;
        $.ajax({
            url: `http://localhost:8080/carts/users`,
            method: 'POST',
            headers: {
                'Authorization': `${token}`
            },
            data: {
                "productId":productId,
                "quantity":quantity
            }
        })
        .done(function(msg) {
            if (msg.success) {
                alert("Add to cart success!")
                fetchCartData();  
            } else {
                alert("Failed to add to cart");
            }
        });
    });

    $(document).on('click', '.remove-btn', function(e) {
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
                fetchCartData();  
            } else {
                alert("Failed to remove item from cart");
            }
        });
    });

    function fetchCartData() {
        $.ajax({
            url: `http://localhost:8080/carts/users`,
            method: 'GET',  
            headers: {
                'Authorization': `${token}`
            }
        })
        .done(function(msg) {
            if (msg.success) {
                updateCartUI(msg.data);
            } else {
                alert("Failed to load cart data");
            }
        });
    }

    function updateCartUI(cart) {
        // Calculate total price
        let totalPrice = cart.productList.reduce((acc, item) => acc + (item.price * item.quantity), 0);

        $('.ss_cart_value p').text(cart.totalItems);
        $('.ss_cart_value ins').text(`$ ${totalPrice.toFixed(2)}`);

        $('.dropdown-menu').empty();

        cart.productList.forEach(item => {
            let cartItemHtml = `
                <li style="display: flex; align-items: center; margin-bottom: 10px; padding: 5px; border-bottom: 1px solid #ddd;">
                    <div style="display: flex; align-items: center;">
                        ${item.images && item.images.length > 0 ? `
                            <div class="ss_cart_img_wrapper" style="width: 100px; height: 100px; overflow: hidden; margin-right: 10px;">
                                <img src="${headFileLink}${item.images[0]}" alt="cart_img" style="width: 100%; height: 100%; object-fit: cover;">
                            </div>
                        ` : `
                            <div class="ss_cart_img_wrapper" style="width: 100px; height: 100px; margin-right: 10px;">
                                <!-- No image placeholder -->
                            </div>
                        `}
                        <div class="ss_cart_img_cont_wrapper">
                            <h3 style="margin: 0; font-size: 16px;">${item.productName}</h3>
                            <h4 style="margin: 5px 0; font-size: 14px;">Quantity: ${item.quantity}</h4>
                            <div style="margin: 0; font-size: 14px; font-weight: bold;">Total: $ ${(item.price * item.quantity).toFixed(2)}</div>
                        </div>
                        <button class="remove-btn" data-product-id="${item.id}" style="margin-left: auto; background-color: #f44336; color: white; border: none; padding: 5px 10px; cursor: pointer;">
                            x
                        </button>
                    </div>
                </li>
            `;
            $('.dropdown-menu').append(cartItemHtml);
        });

        $('.dropdown-menu').append(`
            <li>
                <a href="shopping_cart.html" class="ss_check_btn">Checkout</a>
            </li>
        `);
    }
});