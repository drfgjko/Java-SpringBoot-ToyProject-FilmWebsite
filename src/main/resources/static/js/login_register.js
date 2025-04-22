//两个表单处理逻辑合并了
$(function() {
    // 登录表单提交事件
    $("#loginForm").submit(function() {
        var email = $("input[name='email_login']").val();
        var password = $("input[name='password_login']").val();

        $.post("/index/doLogin", { "email": email, "password": password }, function(result) {
            alert(result);
            // 不管失败与否都是返回首页
            window.location.href = "/index";
        });
        return false;
    });

    // 注册表单提交事件
    $("#registerForm").submit(function() {
        // 表单验证逻辑
        var flag = true;
        flag = formObj.checkNull("email", "Email不能为空！") && flag;
        flag = formObj.checkNull("username", "用户名不能为空！") && flag;
        flag = formObj.checkNull("passw1", "密码不能为空！") && flag;
        flag = formObj.checkNull("passw2", "确认密码不能为空！") && flag;
        flag = formObj.checkEmail() && flag;
        flag = formObj.checkPassword() && flag;

        if (flag) {
            var email = $("input[name='email']").val();
            var passw1 = $("input[name='passw1']").val();
            var username = $("input[name='username']").val();
            $.post("/index/doRegister", { "email": email, "passw1": passw1, "username": username }, function(result) {
                if (result === "注册成功") {
                    alert(result);
                    window.location.href = "/index"; // 注册成功后重定向到首页
                } else {
                    alert(result); // 提示注册失败信息
                }
            });
        }
        return false;
    });

    // 表单验证对象
    var formObj = {
        "checkPassword": function() {
            // 检查密码逻辑
            var passw1 = $("input[name='passw1']").val();
            var passw2 = $("input[name='passw2']").val();
            if (passw1.length > 0 && passw2.length > 0) {
                if (passw1 !== passw2) {
                    alert("两次密码不一致");
                    return false;
                }
            }
            return true;
        },
        "checkEmail": function() {
            // 检查邮箱逻辑
            var email = $("input[name='email']").val();
            var regExp = /^\w+@\w+(\.\w+)+$/;
            if (email.length > 0) {
                if (!regExp.test(email)) {
                    alert("email格式不正确！");
                    return false;
                }
            }
            return true;
        },
        "checkNull": function(name, msg) {
            // 检查字段是否为空逻辑
            var value = $("input[name='" + name + "']").val();
            switch (value.length) {
                case 0:
                    alert(msg);
                    return false;
                default:
                    return true;
            }
        }
    };
});
