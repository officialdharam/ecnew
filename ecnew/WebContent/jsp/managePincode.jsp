<%@page import="com.ec.util.Constant"%>
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
				<input id="fetchPincodes" type="button" value="Fetch Pincodes" class="fetchBtn"/>
			</div>
		</div>

	</div>
</div>
<div id="rightcenterBig">
	<h3 id="currentWH"><%=warehouseName%></h3>
	<%
	    String result = request.getParameter("addPincodeResult");
		String result2 = (String) request.getParameter("transferPincodeResult");
	    if ("true".equals(result) || "true".equals(result2)) {
	%>
	<p class="respTxxt">
		<font color="green">Operation Successful.</font>
	</p>
	<%
	    } else if ("false".equals(result)) {
	%>
	<p class="respTxxt">
		<font color="red">Operation Failed. Either the pincode is already assigned or it is not present in the system.</font>
	</p >
	<%
	    }
	%>

	<div id="pincodesinwarehouse"></div>

</div>

<div id="deletionSuccessPopup"></div>

<div id="popupManagePincodes"></div>
<div class="inputarea" align="center" style="margin-left: 100px;">
	<input type="button" value="Add" id="addPincode" /> <input
		type="button" value="Delete" id="deletePincode" /> <input
		type="button" value="Transfer" id="transferPincode" />

</div>