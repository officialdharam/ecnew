<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.List"%>
<%@page import="com.ec.dto.UILink"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<jsp:include page="includes.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/js/common.js"
	type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/admin.js"
	type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/management.js"
	type="text/javascript"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css" type="text/css" />
<% List<String> groups = (List<String>)session.getAttribute("groups");
 String group = (String)session.getAttribute("group");
 java.util.List<String> centers = (java.util.List<String>) request.getSession().getAttribute("centers");
%>
<script>
	var ctx = "${pageContext.request.contextPath}";
	var groups = "<%=groups%>";
	var group = "<%=group%>";
	var centers = "<%=centers%>";
</script>
<body>
	<div id="headerContainer">
		<div id="headerdiv" class="headerdiv">
			<a href="http://extracarbon.com/"><img
				src="${pageContext.request.contextPath}/images/logo.png" /></a>
				
				
			<div id="nav">

				<div id="nav_wrapper">
					<div style="display: inline; margin-left: 10px;">
						<font color="#ccc"> Welcome <%=(String) session.getAttribute("username")%>!!
						</font>
						
					</div>
					<div style="display: inline; margin-left: 20px;">
						<font color="#ccc"> 	<%
						String chooseCenter = (String) request.getSession().getAttribute("centerName");
						if (chooseCenter != null && chooseCenter != "null"){ %> <b id="currentCenterName"><%=chooseCenter %><%} %></b></font>
						
					</div>
					
					<ul>
						<%
						    Set<UILink> links = (Set<UILink>) request.getSession().getAttribute("uiLinks");
						    for (UILink link : links) {
						%>
						<li><a href="<%=link.getHref()%>"><%=link.getDisplayName()%></a>
							<%
							    Set<UILink> children = link.getChildren();
									if (children != null && children.size() > 0) {
							%><ul>
								<%
								    for (UILink childLink : children) {
								%>
								<li><a
									href="${pageContext.request.contextPath}/service/navigate?nextPage=<%=childLink.getHref()%>"><%=childLink.getDisplayName()%></a></li>
								<%
								    }
								%>
							</ul></li>
						<%
						    }

						    }
						%>

						<li><a href="#">My Account</a>
							<ul>
								<li><a id="changePasswordLink" href="#">Change Password</a></li>
								<li><a id="signout" href="#">Sign Out</a></li>
							</ul></li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<div id="contentdiv">


		<%
		    
		    if (!"CUSTOMER_SERVICE".equalsIgnoreCase(group) && (chooseCenter == null || chooseCenter.length() <= 0)) {
		%>

		<jsp:include page="centerselect.jsp"></jsp:include>
		<%
		    } else {
				String contentPage = (String) request.getSession().getAttribute("contentPage");
		%>
		<div>

		</div>
		<jsp:include page="<%=contentPage%>"></jsp:include>
		<%
		    }
		%>

	</div>
	<div id="orderSummary" title="Order Summary" class="orderSummary">
	</div>

	<div id="changePassword" title="Change Password" class="changepassword">

	</div>
</body>
</html>
