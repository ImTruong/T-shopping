$(document).ready(function(){
    var token=localStorage.getItem("token")
    var headFileLink="http://localhost:8080/files/"
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/products/seller",
        headers: {
            "Authorization": token 
        }
    })
    .done(function(msg) {
        let productListHtml = ``;
        msg.data.forEach(element => {
            productListHtml += `<tr>
                                    <td>`;
            // Check if images array exists and has a valid first image
            if (element.images && element.images.length > 0 && element.images[0] !== null && element.images[0] !== '') {
                productListHtml += `<img class="rounded-circle" width="35" src="${headFileLink}${element.images[0]}" alt="">`;
            } else {
                // Add a placeholder image if there's no image
                productListHtml += `<img class="rounded-circle" width="35" src="/path/to/placeholder/image.png" alt="No Image">`;
            }
            productListHtml += `</td>
                                <td>${element.productName}</td>
                                <td>${element.categoryName}</td>
                                <td>${element.price}</td>
                                <td>${element.stock}</td>
                                <td>${element.brand}</td>
                                <td>
                                    <div class="d-flex">
                                        <a href="#" class="btn btn-primary shadow btn-xs sharp me-1"><i class="fa fa-pencil"></i></a>
                                        <a href="#" class="btn btn-danger shadow btn-xs sharp"><i class="fa fa-trash"></i></a>
                                    </div>
                                </td>
                            </tr>\n`;
        });
        $(`#productList`).append(productListHtml);
    });
    
})