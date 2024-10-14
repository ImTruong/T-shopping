$(document).ready(function(){
    let form = document.getElementById('signupForm');
    form.addEventListener('submit', function(event) {
        event.preventDefault(); 
        let formData = new FormData(form);
        let firstName = formData.get('firstName');
        let lastName = formData.get('lastName');
        let phoneNumber = formData.get('phoneNumber');
        let userName = formData.get('userName');
        let password = formData.get('password');
        
        $.ajax({
            url: 'http://localhost:8080/users',
            method: 'POST',
            contentType: 'application/json', 
            data: JSON.stringify({
                "firstName": firstName,
                "lastName": lastName,
                "phoneNumber": phoneNumber,
                "userName": userName,
                "password": password
            })
        })
        .done(function(msg){
            if(msg.success){
                alert("Success to register")
                window.location.href = 'http://localhost:8080/login.html';
            }else{
                alert("Fail to register")
            }
        })
    });
})