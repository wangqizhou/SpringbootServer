function checkName(){
    var name;
    name = document.getElementById("name").value;
    if(name.length == 0){
        document.getElementById('nameErr').style.display='inline';
    }else{
        document.getElementById('nameErr').style.display='none';
    }
}

function checkPwd(){
    var pwd;
    pwd = document.getElementById("pwd").value;
    if(pwd.length == 0){
        document.getElementById('pwdErr').style.display='inline';
    }else{
        document.getElementById('pwdErr').style.display='none';
    }
}

function login(){
    var name; var pwd;
    name = document.getElementById("name").value;
    pwd = document.getElementById("pwd").value;
    if(name.length == 0 || pwd.length == 0){
        alert("用户名和密码不能为空！");
        if((name.length == 0 && pwd.length != 0)||(name.length == 0 && pwd.length == 0)){
            $("#name").focus();
        }else if(name.length != 0 && pwd.length == 0){
            $("#pwd").focus();
        }
    }else{
        document.getElementById('loginForm').submit();
    }
}

function confirmPwd() {
    var pwd = $('#password').val();
    var con = $("#confirm").val();
    // alert("pwd-->"+pwd +"    "+"con-->"+con);
    if (con != pwd) {
        $('#commonModalBody p').html("两次输入的密码不相同!");
        $('#commonModal').modal('show');
        $('#commonModalSuccess').on("click", function () {
            $('#commonModal').modal('hide');
        });
        // alert("两次输入的密码不相同！");
    }
}

function checkNull() {
    var name = $('#username').val();
    var pwd = $('#password').val();
    var con = $("#confirm").val();
    var email = $("#email").val();
    var auth = $("#authority").val();

    if (name != '' && pwd != '' && con != '' && email != '' && auth != '') {
        $('#register').attr("disabled",false);
    }
}
