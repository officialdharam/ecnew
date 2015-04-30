
<%
    java.util.List<String> centers = (java.util.List<String>) request.getSession().getAttribute("centers");
    String warehouseName = request.getParameter("warehouse");
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
			<div>
				<input id="fetchDevices" type="button" value="Fetch Devices" class="fetchBtn"/>
			</div>
		</div>

	</div>
</div>
<div id="rightcenterBig">
	<h3 id="currentWH"><%=warehouseName%></h3>
	<%
	    String result = (String) request.getParameter("addDeviceResult");
	    String result2 = (String) request.getParameter("transferDeviceResult");
	    if ("TRUE".equalsIgnoreCase(result) || "TRUE".equalsIgnoreCase(result2)) {
	%>
	<p class="respTxxt">
		<font color="green" >Operation Successful.</font>
	</p>
	<%
	    } else if ("false".equals(result)) {
	%>
	<p class="respTxxt">
		<font color="red">Operation Failed.</font>
	</p>
	<%
	    } else if ("DEVICE ALREADY EXISTS".equals(result)) {
	%>
	<p class="respTxxt">
		<font color="red">Device Already Exists.</font>
	</p>
	<%
	    }
	%>

	<div id="devicesinwarehouse"></div>
	<div id="errorMsgDevice"></div>
</div>

<div id="deletionSuccessPopup"></div>

<div id="popupManageDevices"></div>
<div class="inputarea" align="center" style="margin-left: 100px;">
	<input type="button" value="Add" id="addDevice" /> <input
		type="button" value="Delete" id="deleteDevice" /> <input
		type="button" value="Transfer" id="transferDevice" />

</div>