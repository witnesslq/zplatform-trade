<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>订单提交</title>


</head>
<body onload="submitForm()">
<br><br><br><br><br><br><br>

<form name="tj" id="tj" action="https://dlink.test.bank.ecitic.com/pec/coplaceorder.do" method="post" >
<table>
<tr><td><input type="hidden" id="SIGNREQMSG" name="SIGNREQMSG" value="${signedMsg} "></td><td>
</table>
</form>
<script type="text/javascript">
	function submitForm(){
		document.forms[0].submit();
	}
</script>
</body>
</html>