$(document).ready(function(){
    const currentURL = window.location.href;
    const newURL = currentURL.replace(/\/[^/]*$/, '/login.html');
    var loginBox=`<a href="${newURL}"><span>Login or Register</span> <img
    src="images/header/user_icon.png" alt="Login" title="Login" class="img-responsive"></a>`;
    var token=localStorage.getItem("token")
    if (token != null) {  
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/users",
            headers: {
                "Authorization": token 
            }
        })
        .done(function(msg){
            let displayName="";
            if(msg.data.firstName) displayName+=`${msg.data.firstName}`;
            if(msg.data.lastNameName) displayName+=`${msg.data.lastName}`;
            if(msg.success){
                loginBox=`<a href="${newURL}"><span>${displayName}</span> <img
    src="images/header/user_icon.png" alt="Login" title="Login" class="img-responsive"></a>`;
            
            }
            $(".ss_login_box").append(loginBox)
        })
        
    }
})