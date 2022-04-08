layui.use(['form', 'layer','laydate'], function () {
    var form = layui.form,
        laydate = layui.laydate,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    //常规用法
    laydate.render({
        elem: '#planDate'
    });

    /*添加或更新计划*/

/*    form.on("submit(addOrUpdateCusDevPlan)",function (data){
        //弹出loading层
        var index = top.layer.msg('数据正在提交',{icon:16,time: false,shade: 0.8});
        var url = ctx + "/cus_dev_plan/add";

        console.log(url);

        console.log($('[name="id"]').val());

        if ($('[name="id"]').val()){
            url = ctx + "/cus_dev_plan/update";
            console.log(url);
        }
        $.post(url,data.field,function (data){
            if (data.code==200){
                //关闭弹出层
                layer.close(index);
                //关闭iframe层
                layer.closeAll("iframe");
                //刷新父页面，将添加的新数据展示
                parent.location.reload();
            }else {
                layer.msg(data.code,{icon:5})
            }

        });
        return false;
    });*/

    /**
     * 添加或更新计划项
     */
    form.on("submit(addOrUpdateCusDevPlan)", function (data) {
        // 弹出loading层
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        var url = ctx+"/cus_dev_plan/add";

        console.log(url+"/添加");

        if($('[name="id"]').val()){
            url = ctx+"/cus_dev_plan/update";
            console.log(url+"/修改");
        }

        $.post(url,data.field,function (data){
            if(data.code == 200){
                //关闭弹出框
                layer.close(index);
                //关闭iframe层
                layer.closeAll("iframe");
                //刷新父页面，将添加的新数据展示
                parent.location.reload();
            }else{
                layer.msg(data.msg,{icon:5})
            }
        });
        return false; //阻止表单提交
    });

    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });

});