
<%
    java.util.List<String> centers = (java.util.List<String>) request.getSession().getAttribute("centers");
    String warehouseName = centers.get(0);
%>

<div class="leftcenterSmall">

	<div>
		<h1>Select a center</h1>

		<div id="selectcenterdiv no-border">
			<div class="labeltxt">Center :</div>

			<select name="warehouse" id="warehouseList">
				<%
				    for (String center : centers) {
				%>
				<option <%if (center.equalsIgnoreCase(warehouseName)) {%>
					selected="selected" <%}%> value='<%=center%>' label='<%=center%>'><%=center%>
				</option>

				<%
				    }
				%>
			</select>
		</div>

	</div>
</div>
<div id="rightcenterBig">
	<h3 id="currentWH" style="margin: 0px"><%=warehouseName%></h3>
	<div id="users" style="overflow-y: auto;max-height: 440px;"></div>

</div>

<div id="successLabel"></div>

<div id="popupManageDevices"></div>
<div class="inputarea" align="center" style="margin-left: 50px;">
	<input type="button" id="editEmployeeBtn" value="Update Employee"
		style="display: none;" /> <input type="button" value="Fetch Users"
		id="fetchUser" /> <input type="button" value="Add User" id="addUser" />

</div>