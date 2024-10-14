$(document).ready(function () {
    var token = localStorage.getItem("token");
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/users",
        headers: {
            "Authorization": token
        }
    })
    .done(function(msg){
        if (msg.success) {
            var formProfile = `
                <form id="updateForm">
                    <input type="hidden" name="id" value="${msg.data.id}">
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label>Email</label>
                            <input name="email" type="email" placeholder="${msg.data.email}" class="form-control">
                        </div>
                        <div class="form-group col-md-6">
                            <label>Password</label>
                            <input name="password" type="password" placeholder="Password" class="form-control">
                        </div>
                        <div class="form-group col-md-6">
                            <label>FirstName</label>
                            <input name="firstName" type="text" placeholder="${msg.data.firstName}" class="form-control">
                        </div>
                        <div class="form-group col-md-6">
                            <label>LastName</label>
                            <input name="lastName" type="text" placeholder="${msg.data.lastName}" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label>PhoneNumber</label>
                        <input name="phoneNumber" type="text" placeholder="${msg.data.phoneNumber}" class="form-control">
                    </div>
                    <div id="addressFields"></div>
                    <button class="btn btn-primary" type="submit">Update</button>
                    <input type="hidden" name="addressId" value="${msg.data.addressId}">
                </form>`;
            
            $(`#profileSetting`).append(formProfile);

            if (msg.data.addressId) {
                $.ajax({
                    type: "GET",
                    url: `http://localhost:8080/address/${msg.data.addressId}`,
                    headers: {
                        "Authorization": token
                    }
                })
                .done(function(addressMsg) {
                    if (addressMsg.success) {
                        var addressFields = `
                            <div class="row">
                                <div class="form-group col-md-4">
                                    <label>Street Address</label>
                                    <input name="streetAddress" type="text" placeholder="${addressMsg.data.streetAddress}" class="form-control">
                                </div>
                                <div class="form-group col-md-4">
                                    <label>City</label>
                                    <input name="city" type="text" placeholder="${addressMsg.data.city}" class="form-control">
                                </div>
                                <div class="form-group col-md-4">
                                    <label>Country</label>
                                    <input name="country" type="text" placeholder="${addressMsg.data.country}" class="form-control">
                                </div>
                            </div>`;
                        $('#addressFields').html(addressFields);
                    }
                });
            } else {
                var emptyAddressFields = `
                    <div class="row">
                        <div class="form-group col-md-4">
                            <label>Street Address</label>
                            <input name="streetAddress" type="text" placeholder="Street Address" class="form-control">
                        </div>
                        <div class="form-group col-md-4">
                            <label>City</label>
                            <input name="city" type="text" placeholder="City" class="form-control">
                        </div>
                        <div class="form-group col-md-4">
                            <label>Country</label>
                            <input name="country" type="text" placeholder="Country" class="form-control">
                        </div>
                    </div>`;
                $('#addressFields').html(emptyAddressFields);
            }

            let form = document.getElementById('updateForm');
            form.addEventListener('submit', function(event) {
                event.preventDefault();
                let formData = new FormData(form);
                let firstName = formData.get('firstName');
                let lastName = formData.get('lastName');
                let phoneNumber = formData.get('phoneNumber');
                let password = formData.get('password');
                let email = formData.get('email');
                let id = formData.get('id');
                let addressId=formData.get('addressId');
                let streetAddress = formData.get('streetAddress');
                let city = formData.get('city');
                let country = formData.get('country');

                $.ajax({
                    type: "PUT",
                    url: "http://localhost:8080/users",
                    contentType: 'application/json',
                    data: JSON.stringify({
                        "firstName": firstName,
                        "lastName": lastName,
                        "phoneNumber": phoneNumber,
                        "email": email,
                        "password": password,
                        "id": id,
                        "addressId":addressId,
                        "streetAddress": streetAddress,
                        "city": city,
                        "country": country
                    })
                })
                .done(function(msg){
                });
            });
        }
    });
});
