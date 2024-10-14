$(document).ready(function(){
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('id');
    var headFileLink = "http://localhost:8080/files/";
    var endPointType=""
    
    window.deleteImage = function(imageUrl) {
        if (confirm("Are you sure you want to delete this image?")) {
            $.ajax({
                url: `http://localhost:8080/products/images`,
                method: 'DELETE',
                contentType: 'application/json',
                data: JSON.stringify({
                    "imageUrl":imageUrl,
                    "productId":productId
                })
            })
            .done(function(msg){
                if(msg.success){
                    alert("Success")
                    location.reload();
                }else{
                    alert("Fail")
                }
            })
        }
    }
    function buildForm(data = {}) {
        var form = `
        <form id="detail">			
            <div class="d-flex w-100">
                <div class="form-group w-50 pr-2">
                    <label for="productName">Product Name</label>
                    <input type="text" name="productName" class="form-control input-default" placeholder="${data.productName || ''}">
                </div>
                <div class="form-group w-50 pl-2">
                    <label for="brand">Brand</label>
                    <input type="text" name="brand" class="form-control input-default" placeholder="${data.brand || ''}">
                </div>
            </div>

            <!-- Second row of inputs -->
            <div class="d-flex w-100">
                <div class="form-group w-50 pr-2">
                    <label for="price">Price</label>
                    <input type="text" name="price" class="form-control input-default" placeholder="${data.price || ''}">
                </div>
                <div class="form-group w-50 pl-2">
                    <label for="stock">Stock</label>
                    <input type="text" name="stock" class="form-control input-default" placeholder="${data.stock || ''}">
                </div>
            </div>

            <div class="form-group"></div>
            <label for="description">Description</label>
                <textarea placeholder="${data.description || ''}" name="description" class="form-control" rows="8" id="comment"></textarea>
            </div>
            </br>
            <button type="submit" class="btn btn-primary">Submit</button>
            </br>
            </br>
        </form>`;
        return form;
    }

    // Hàm thêm ảnh và các phần liên quan tới hình ảnh
    function addImageSection(images = []) {
        var imageSection = `<div class="mb-3" style="padding: 10px; border: 1px solid #ccc; border-radius: 5px; background-color: #f9f9f9;">
            <label for="formFile" class="form-label">Change Represent Image</label>
            <input class="form-control" type="file" id="representImageFile">
            </br>
            <button type="submit" class="btn btn-primary" id="changeRepresentImage">Change</button>
            <label for="">Represent Image</label>`;

            // Nếu có ảnh represent, hiển thị nó, nếu không thì chỉ hiện phần tải lên
            if (images.length > 0) {
                var representImage = images.find(image => image.description === "represent");
                imageSection += representImage ? 
                    `<img src="${headFileLink}${representImage.imageUrl}" alt="Current Image" class="img-thumbnail" style="max-width: 150px; max-height: 150px;">` :
                    `<p>No Represent Image Uploaded</p>`;
            }

        imageSection += `</br></br></br></div>`;

        imageSection += `<div class="form-group">
            <label for="">Additional Images</label>
            <div style="padding: 10px; border: 1px solid #ccc; border-radius: 5px; background-color: #f9f9f9;">
                </br>
                <label for="uploadNewImage" class="form-label">Upload New Image</label>
                <input class="form-control" type="file" id="additionalImageFile">
                </br>
                <label for="imageDescription" class="form-label">Image Description</label>
                <input type="text" id="additionalImageDescription" name="imageDescription" class="form-control input-default" placeholder="Non represent">
                </br>
                <button id="addAdditionalImage" class="btn btn-primary">Add</button>
                </div>
                <div class="d-flex flex-wrap">
                <!-- Các ảnh sẽ được hiển thị ở đây -->
                </div>`;

        if (images.length > 0) {
            const nonRepresentImages = images.filter(image => image.description !== "represent");
            nonRepresentImages.forEach(element => {
                imageSection += `<div class="m-2 text-center">
                    <img src="${headFileLink}${element.imageUrl}" alt="Additional Image" class="img-thumbnail" style="max-width: 100px; max-height: 100px;">
                    <button type="button" class="btn btn-danger btn-sm mt-1" onclick="deleteImage('${element.imageUrl}')">Delete</button>
                </div>`;
            });
        }

        imageSection += `</div></div>`;
        return imageSection;
    }

    if (productId == null) {
        endPointType="POST"
        $("#form").append(buildForm());
        $("#form").append(addImageSection());
    
        productDataRequest = $.Deferred().resolve().promise();
        imageDataRequest = $.Deferred().resolve().promise();
    } else {
        endPointType="PUT"
        productDataRequest = $.ajax({
            url: `http://localhost:8080/products/${productId}`,
            method: 'GET'
        });
    
        imageDataRequest = $.ajax({
            url: `http://localhost:8080/products/images/${productId}`,
            method: 'GET'
        });
        $.when(productDataRequest, imageDataRequest).then(function(productResponse, imageResponse) {
            $("#form").append(buildForm(productResponse[0].data));
            $("#form").append(addImageSection(imageResponse[0].data));
        });
    }

    function sendProductDetailRequest(data){
        $.ajax({
            url: 'http://localhost:8080/products',
            method: endPointType,
            contentType: 'application/json', 
            data: data
        })
        .done(function(msg){
            if(msg.success){
                alert("Success")
            }else{
                alert("Fail")
            }
        })
    }
    
    // Sử dụng $.when() để chờ cả hai promise hoàn thành
    $.when(productDataRequest, imageDataRequest).then(function() {
        let form = document.getElementById('detail');
        form.addEventListener('submit', function(event) {
            event.preventDefault(); 
            let formData = new FormData(form);
            let productName = formData.get('productName');
            let brand = formData.get('brand');
            let price = formData.get('price');
            let stock = formData.get('stock');
            let description = formData.get('description');
            var dataProductDetail=""
            if(endPointType==="PUT"){
                dataProductDetail=JSON.stringify({
                    'productName': productName,
                    'brand': brand,
                    'price': price,
                    'stock': stock,
                    'description': description,
                    'id':productId
                })
                sendProductDetailRequest(dataProductDetail)
            }
            else{
                var token=localStorage.getItem("token")
                let sellerId
                $.ajax({
                    type: "GET",
                    url: "http://localhost:8080/users",
                    headers: {
                        "Authorization": token 
                    }
                })
                .done(function(msg){
                    sellerId=msg.data.id
                    dataProductDetail=JSON.stringify({
                    'productName': productName,
                    'brand': brand,
                    'price': price,
                    'stock': stock,
                    'description': description,
                    'sellerId':sellerId
                    })
                    sendProductDetailRequest(dataProductDetail)
                })
            }
            
        });
        $("#addAdditionalImage").click(function() {
            if(productId===null){
                alert("No product is being selected!")
                return
            }
            let fileInput = document.getElementById('additionalImageFile');
            let imageDescription = $("#additionalImageDescription").val();
            let file = fileInput.files[0]; // Lấy file từ input
        
            if (!file) {
                alert("Please select an image file.");
                return;
            }
        
            let formData = new FormData();
            formData.append('file', file); // Thêm file vào FormData
            formData.append('productId', productId);
            formData.append('description', imageDescription);
        
            $.ajax({
                type: "POST",
                url: "http://localhost:8080/products/images",
                data: formData,
                processData: false,
                contentType: false,
                success: function(msg) {
                    if (msg.success) {
                        alert("Image added successfully.");
                        location.reload(); 
                    } else {
                        alert("Failed to add the image.");
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    alert("Error: " + textStatus + " - " + errorThrown);
                }
            });
        });
        $("#changeRepresentImage").click(function() {
            // Lấy file từ input
            let fileInput = $("#representImageFile")[0];
            let file = fileInput.files[0];
    
            // Kiểm tra nếu không có file được chọn
            if (!file) {
                alert("Please select an image to upload.");
                return;
            }
    
            // Kiểm tra nếu không có productId
            if (!productId) {
                alert("Product ID is missing.");
                return;
            }
    
            // Tạo đối tượng FormData để gửi file qua API
            let formData = new FormData();
            formData.append('file', file);
            formData.append('productId', productId);
    
            // Gọi API để đổi ảnh đại diện (represent image)
            $.ajax({
                type: "PUT",
                url: "http://localhost:8080/products/images",
                data: formData,
                processData: false, // Không xử lý dữ liệu gửi đi (FormData xử lý rồi)
                contentType: false, // Để mặc định cho multipart form
                success: function(response) {
                    if (response.success) {
                        alert("Represent image changed successfully!");
                        location.reload(); // Làm mới trang sau khi thay đổi thành công
                    } else {
                        alert("Failed to change represent image.");
                    }
                },
                error: function() {
                    alert("An error occurred while changing the represent image.");
                }
            });
        });
    })
});
