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
				<input id="fetchExecutives" type="button" value="Fetch Executive" class="fetchBtn"/>
			</div>
		</div>

	</div>
</div>
<div id="rightcenterBig">
	<h3 id="currentWH"><%=warehouseName%></h3>
	<%
	    String result = (String) request.getParameter("addFEResult");
		String result2 = (String) request.getParameter("transferFEResult");
	    if ("true".equalsIgnoreCase(result) || "true".equalsIgnoreCase(result2)) {
	%>
	<p class="respTxxt">
		<font color="green">Operation Successful.</font>
	</p>
	<%
	    } else if ("false".equals(result)) {
	%>
	<p class="respTxxt">
		<font color="red">Operation Failed.</font>
	</p>
	<%
	    }
	%>

	<div id="feinwarehouse"></div>

</div>

<div id="deletionSuccessPopup"></div>

<div id="popupManageFE"></div>
<div class="inputarea" align="center" style="margin-left: 100px;">
	<input type="button" value="Add" id="addFE" /> <input
		type="button" value="Delete" id="deleteFE" /> <input
		type="button" value="Transfer" id="transferFE" />

</div>