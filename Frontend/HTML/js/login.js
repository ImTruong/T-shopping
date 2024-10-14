$(document).ready(function(){
    let form = document.getElementById('signInForm');
    form.addEventListener('submit', function(event) {
        event.preventDefault(); 
        let formData = new FormData(form);
        let userName = formData.get('userName');
        let password = formData.get('password');
        $.ajax({
            url: 'http://localhost:8080/users/login',
            method: 'POST',
            data: {
                "userName": userName,
                "password": password
            }
        })
        .done(function(msg){
            if(msg.success){
                alert("Success to login")
                console.log(msg)
                window.localStorage.setItem("token", msg.data);
            }else{
                alert(msg.data)
            }
        })
    });
})