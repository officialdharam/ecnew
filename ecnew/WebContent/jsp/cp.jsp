<script type="text/javascript">
	function redirect(url) {
		var e = document.getElementById("warehouseSelect");
		var selectedWarehouse = e.options[e.selectedIndex].text;
		document.location.href = url + "&warehouse=" + selectedWarehouse;
	}
</script>

<%
    java.util.List<String> centers = (java.util.List<String>) request.getSession().getAttribute("centers");
%>

<div id="leftmanage" class="leftmanage">
	<div>
		<h1>Select a center</h1>

		<div id="">
			<div class="labeltxt">Center :</div>

			<select name="warehouse" id="warehouseSelect" style="width: 100px;">
				<%
				    for (String center : centers) {
				%>
				<option value="<%=center%>" label="<%=center%>"><%=center%>
				</option>

				<%
				    }
				%>
			</select>
		</div>

	</div>
</div>
<div id="rightmanage" class="rightmanage">
	<div class="innermanage">
		<input type="button" value="Manage Field Executives" id="manageFE"
			onclick="redirect('${pageContext.request.contextPath}/service/navigate?nextPage=manageFE.jsp')" />
		<input type="button" value="Manage Devices" id="manageDevices"
			onclick="redirect('${pageContext.request.contextPath}/service/navigate?nextPage=manageDevice.jsp')" />
		
	</div>

	<hr>

	<div class="innermanage">
		<input type="button" value="Manage Pincode" id="manageLocations"
			onclick="redirect('${pageContext.request.contextPath}/service/navigate?nextPage=managePincode.jsp')" />
		<input type="button" value="Edit Details"
			onclick="redirect('${pageContext.request.contextPath}/service/navigate?nextPage=editDetails.jsp')" />
	</div>
</div>
