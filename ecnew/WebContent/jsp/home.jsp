<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="includes.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/js/common.js"
	type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/operations.js"
	type="text/javascript"></script>

<%
    String changed = request.getParameter("passwordChanged");
%>
<script>
	var ctx = "${pageContext.request.contextPath}";
</script>
<body>



	<div id="headerContainer">
		<div id="headerdiv" class="headerdiv">
			<a href="http://extracarbon.com/"><img
				src="${pageContext.request.contextPath}/images/logo.png" /></a>
			<div id="nav">
				<div id="nav_wrapper">
					<ul>
						<li><a href="http://extracarbon.com/">Go Green</a>
							<ul>
								<li><a href="http://extracarbon.com/other/about_us">About
										Extracarbon</a></li>
								<li><a href="http://extracarbon.com/other/careers">Career</a></li>
								<li><a href="http://extracarbon.com/other/corp_clients">Corporate
										Goals</a></li>
								<li><a href="http://extracarbon.blogspot.in/">Blog</a></li>
							</ul></li>
						<li><a href="http://jhoomley.com/">Jhoomley</a></li>
						<li><a href="#">Sell Products</a></li>
						<li><a href="http://extracarbon.com/other/pickup">Request
								Pickup</a></li>
						<li><a href="#">Redeem Coupon</a></li>
						<li><a href="http://extracarbon.com/other/contact_us">Contact
								Us</a></li>
					</ul>

				</div>
			</div>
		</div>
	</div>

	<%
	    String loginError = (String) request.getAttribute("loginError");
	%>
	<div id="contentdiv">
		
		<div id="left">
			<div>
				<h1>Login</h1>
				<font size="4"> Welcome to <strong>Extra Carbon</strong></font>
				<p>You are here because the page you are trying to access is
					restricted</p>
				<p>Please login using your username and password</p>
				<p>If you are new to the system and require access, please drop
					a note to customerservice@extracarbon.com</p>

				<form action="${pageContext.request.contextPath}/service/login"
					method="post">
					<div id="logindiv">

						<div class="labeltxt">Username :</div>
						<input type="text" name="username" />
						<div class="labeltxt">Password :</div>
						<input type="password" name="password" /> <br /> <input
							type="submit" value="Login" />

						<%
						    if ("true".equals(loginError)) {
						%><p>
							<font color="red">Invalid login credentials. Please try
								again.</font>
						</p>
						<%
						    }
						%>

						<%
						    Object o = request.getAttribute("logout");

						    if (null != o && "true".equalsIgnoreCase((String) o)) {
						%><p>
							<font color="green">You have successfully logged out. </font>
						</p>
						<%
						    }
						%>
					</div>
				</form>
			</div>
		</div>
		<div id="right">
			<div>
				<h1>Bulk Search by Order</h1>
				<p>Search for order ids by providing the order ids. Please
					separate the ids with a comma, invalid ids will be ignored by the
					system.</p>
				<div id="searchorderdiv">
					<textarea rows="10" cols="40" id="orderids"
						placeholder="Search by order ids, multiple order ids must be comma separated"></textarea>
					<input type="submit" id="searchBulkOrders" value="Search" />
				</div>
			</div>
		</div>

		<div id="bulkOrders"></div>
	</div>


	<div id="footerdiv">
		<div></div>
		<div>Site by Dharmendra Prasad</div>
	</div>
</body>
</html>