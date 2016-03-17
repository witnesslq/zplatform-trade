<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<title>代付接口  Mock</title>
<link type="text/css" rel="stylesheet" href="<%=basePath %>/bootstrap3/css/bootstrap.min.css">
<%-- <link type="text/css" rel="stylesheet" href="<%=basePath %>/bootstrap3/css/bootstrap-theme.min.css"> --%>
<script type="text/javascript" src="<%=basePath %>js/jquery-2.2.0.js"></script>
<script type="text/javascript" src="<%=basePath %>/bootstrap3/js/bootstrap.min.js"></script>


<style type="text/css">
#Container{
	height:100%;
    width:100%;
    margin:0 auto;
/*     background:#CF3; */
}
#Header{
/* 	height:1%;  */ 
     height:80px; 
/*     background:#093; */
}
#logo{
    padding-left:50px;
    padding-top:20px;
    padding-bottom:50px;
}
#Content{
    height:85%;
    margin-top:20px;
/*     background:#0FF; */
     
}
#Content-Left{
/*     height:400px; */
    width:10%;
    margin:20px;
    float:left;
/*     background:#90C; */
}
#Content-Main{
/*     height:400px; */
    width:80%;
    margin:20px;
    float:left;
/*     background:#90C; */
}

#Footer{ 
    height:10%;
/*     background:#90C; */
/*     margin-top:20px; */
    text-align:center;
}
.Clear{
    clear:both;
}
</style>
</head>

<body>


<div id="Container">
    <div id="Header">
        <div id="logo">
  			<h1>钱包API<small>&nbsp;&nbsp;&nbsp;&nbsp;mock for test</small></h1>      
        </div>
    </div>
    <div id="Content" class="contents">
        <div id="Content-Left"> 
		    <ul class="nav nav-pills nav-stacked">
		      <li onclick="changeTab('10086')"><a href="#">模拟客户端</a></li>
			  <li class=active   onclick="changeTab('21')"><a href="#">实名认证</a></li>
			  <li  onclick="changeTab('02')"><a href="#">添加白名单</a></li>
			  <li  onclick="changeTab('03')"><a href="#">批量代付</a></li>
			</ul>
        </div>
        <div id="Content-Main">
        	<div id="10086" class="functions">
        		<div class="panel panel-success">
	        		<div class="panel-heading">
				      <h3 class="panel-title">模拟客户端</h3>
				   </div>
				   <div class="panel-body">
				     	<form id="f10086" target="_blank">
			        		<div class="input-group col-xs-5">
							  <span class="input-group-addon" id="basic-addon1">订单号</span>
							  <input name="loginId"  type="text" class="form-control">
							</div><br>
			        		<div class="input-group col-xs-5">
							  <span class="input-group-addon" id="basic-addon1">商户名</span>
							  <input name="passWd"  type="text" class="form-control" aria-describedby="basic-addon1" ><br>
							</div><br>
			        		<div class="input-group col-xs-5">
							  <span class="input-group-addon" id="basic-addon1">商户号</span>
							  <input name="passWd"  type="text" class="form-control" aria-describedby="basic-addon1" ><br>
							</div><br>
			        		<div class="input-group col-xs-5">
							  <span class="input-group-addon" id="basic-addon1">金额</span>
							  <input name="passWd"  type="text" class="form-control" aria-describedby="basic-addon1" ><br>
							</div>
							<br>
							<button type="button" class="btn btn-danger" onclick="sub('10086')">提交</button>
		        		</form>
				   </div>
				</div>
        	</div>
        	<div id="21" class="functions">
        		<div class="panel panel-success">
	        		<div class="panel-heading">
				      <h3 class="panel-title">查询</h3>
				   </div>
				   <div class="panel-body">
				     	<form id="f21">
			        		<div class="input-group col-xs-5"> 
							  <span class="input-group-addon" id="basic-addon1">商户号</span>
							  <input type="text" class="form-control" name="loginId" value="13426342943">
							</div><br>
							<button type="button" class="btn btn-danger" onclick="sub('21')">提交</button>
		        		</form>
				   </div>
				</div>
        	</div>
        	<div id="03" class="functions">
        		<div class="panel panel-success">
	        		<div class="panel-heading">
				      <h3 class="panel-title">登陆</h3>
				   </div>
				   <div class="panel-body">
				     	<form id="f03">
			        		<div class="input-group col-xs-5">
							  <span class="input-group-addon" id="basic-addon1">登陆名</span>
							  <input name="loginId"  type="text" class="form-control"><br>
							</div><br>
			        		<div class="input-group col-xs-5">
							  <span class="input-group-addon" id="basic-addon1">密码</span>
							  <input name="passWd"  type="text" class="form-control" aria-describedby="basic-addon1" ><br>
							</div>
							<br>
							<button type="button" class="btn btn-danger" onclick="sub('03')">提交</button>
		        		</form>
				   </div>
				</div>
        	</div>
        	<div id="02" class="functions">
        		<div class="panel panel-success">
	        		<div class="panel-heading">
				      <h3 class="panel-title">注册</h3>
				   </div>
				   <div class="panel-body">
				     	<form id="f02">
			        		<div class="input-group col-xs-5">
							  <span class="input-group-addon" id="basic-addon1">登陆名</span>
							  <input name="loginId"  type="text" class="form-control"><br>
							</div><br>
			        		<div class="input-group col-xs-5">
							  <span class="input-group-addon" id="basic-addon1">手机号</span>
							  <input name="phoneNo"  type="text" class="form-control"><br>
							</div><br>
			        		<div class="input-group col-xs-5">
							  <span class="input-group-addon" id="basic-addon1">短信验证码</span>
							  <input name="smsCode"  type="text" class="form-control"><br>
							</div><br>
			        		<div class="input-group col-xs-5">
							  <span class="input-group-addon" id="basic-addon1">密码</span>
							  <input name="passWd"  type="text" class="form-control" aria-describedby="basic-addon1" ><br>
							</div>
							<br>
							<button type="button" class="btn btn-danger" onclick="sub('02')">提交</button>
		        		</form>
				   </div>
				</div>
        	</div>
        </div>
    </div>
    <div class="Clear"><!--如何你上面用到float,下面布局开始前最好清除一下。--></div>
    <div id="Footer"><h1><small>证联金融</small></h1></div>
    
    <!-- 模态框（Modal） -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" data-backdrop="static"
	   aria-labelledby="myModalLabel" aria-hidden="true">
	   <div class="modal-dialog">
	      <div class="modal-content">
	         <div class="modal-header">
	            <button type="button" class="close" 
	               data-dismiss="modal" aria-hidden="true">
	                  &times;
	            </button>
	            <h4 class="modal-title" id="myModalLabel">正在支付</h4>
	         </div>
	         <div class="modal-body">
		            订单号：10086<br>
		    商户名称：证联金融<br>
		    商品名称：iphone9<br>
		    订单金额：6000.00<br>
		         </div>
		         <div class="modal-footer">
		            <button type="button" class="btn btn-success"  data-dismiss="modal">支付完成 </button>
		            <button type="button" class="btn btn-danger" data-dismiss="modal">支付遇到问题</button>
		         </div>
		      </div><!-- /.modal-content -->
		</div><!-- /.modal -->
	</div>

</div>
<script type="text/javascript">
	$(".functions").hide();
	function changeTab(divid) {
		$(".functions").hide();
		$("#"+divid).show();//显示div 
	}
	function sub(divid) {
		if (divid=='10086') {
			$("#myModal").modal('show');
			$('#f'+divid).attr("action", '<%=basePath %>mock/'+ divid+'.htm');
			$('#f'+divid).submit();
			return false;
		}
		$.ajax({
			dataType: 'json',
// 			contentType: "application/json;charset=utf-8",
		    url:'<%=basePath %>mock/'+ divid+'.htm',   
		    type:'post',   
		    data:$('#f'+divid).serialize(),
		    async : false, //默认为true 异步   
// 		    error:function(){
// 		       alert('error');   
// 		    }, 
		    error: function(XMLHttpRequest, textStatus, errorThrown) {
		    	 alert(XMLHttpRequest.status);
		    	 alert(XMLHttpRequest.readyState);
		    	 alert(textStatus);
		    	   },
		    success:function(data){
		    	var dataStr;
		    	var output='<br/>-----------------------------------------------<br>';
// 		    	alert(data);
		    	
		    	$.each(data, function(key, value) {
		    	      if (key == 'data') {
		    	    	  dataStr = value;
		    	      }
		    	     });
		    	var obj = jQuery.parseJSON(dataStr);
		    	$.each(obj, function(key, value) {
		    		output = output+key+"->"+value+'<br/>';
		    	     });
		       $("#f"+divid).append(output);
		    }
		});
	}
	$ (function ()
	{
	    $ ('.contents li').click (function (){
		    var that = $ (this);
		    that.addClass ('active').siblings ('li').removeClass ('active');
	    })
	})
</script>
</body>
</html>