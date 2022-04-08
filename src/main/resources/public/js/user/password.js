layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);



    form.on('submit(saveBtn)',function (data){
        console.log(data.field);

        var oldPassword = $('[name="old_password"]').val();
        var newPassword = $('[name="new_password"]').val();
        var confirmPassword = $('[name="again_password"]').val();

        //新密码不能和原始密码一致
        if(newPassword == oldPassword){
            layer.msg("新密码不能和原始密码一致",{icon:5});
            return false;
        }

        //新密码要和确认密码一致
        if(newPassword != confirmPassword){
            layer.msg("新密码要和确认密码一致",{icon:5});
            return false;
        }


        $.ajax({
            type:"post",
            url : ctx + "/update",
            data:{
                oldPassword: oldPassword,
                newPassword: newPassword ,
                confirmPassword: confirmPassword
            },
            dataType: "json",
            success: function (data){
                if (data.code==200){
                    layer.msg("密码修改成功",function (){
                        $.removeCookie("userId",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain: "localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});

                        window.parent.location.href = ctx + "/index";
                    });
                }else {
                    layer.msg(data.msg);
                }
            }

        });
        return false;

    });







});