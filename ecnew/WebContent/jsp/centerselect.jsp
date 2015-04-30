<%
    java.util.List<String> centers = (java.util.List<String>) request.getSession().getAttribute("centers");
%>

<div id="leftcenter">
	<div>
		<h1>Select a center</h1>

		<div id="selectcenterdiv">
			<form method="post" action="${pageContext.request.contextPath}/service/login">
				<div class="labeltxt">Center :</div>

				<select name="warehouse" style="width: 100px;">
					<%
					    for (String center : centers) {
					%>
					<option value='<%=center%>' label='<%=center%>'><%=center%></option>

					<%
					    }
					%>
				</select>
				<div>
					<input type="submit" value="Submit" />
				</div>
			</form>
		</div>

	</div>
</div>
<div id="rightcenter">
	<div>
		<h1>Instructions</h1>
		<p>This page allows you to select the center where you are working
			from.</p>
		<p>
			<strong><font size="4">Center:</font></strong> Please select the
			center from the list for which you are currently working.
		</p>

		<p>For any issues or clarification, drop an email to
			tech.admin@extracarbbon.com</p>
	</div>
</div>
