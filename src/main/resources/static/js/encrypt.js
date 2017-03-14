function encrypt(input) {
    var publickKey = "-----BEGIN PUBLIC KEY-----"
        + "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDU3huJ0HLG7vQ6c3NwUwKkmU/9"
        + "oAGrnVf1ZjrlpXFgfM0TtpgnLWR3mJ1Gkj1uwfVF2TNpAlS2fRPR41Vtelnq+WpD"
        + "jG+dEPx2Jk/8sA5WQLQ+mOD3CeiZqYBRApNH2bUmc4YPPulbSDDkwoJulLlfvWxH"
        + "R+3+hc8Gqpnv01XnBwIDAQAB"
        + "-----END PUBLIC KEY-----";

    var encrypt = new JSEncrypt();
    encrypt.setPublicKey(publickKey);
    var output = encrypt.encrypt(input);
    return output;
}

//绑定login.html中form表单submit事件
$(".form-login").bind("submit", function() {
        var name = $('#username').val();
        var ps =  $('#password').val();
        if (name != "" && ps != "") {
            var encrypted = encrypt(ps);
            $('#password').val(encrypted);
            return true;
        }

        return false;
});