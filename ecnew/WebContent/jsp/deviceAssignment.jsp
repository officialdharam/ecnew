<%@page import="com.ec.dto.FieldExecutive"%>
<%@page import="com.ec.dto.Device"%>
<%@page import="java.util.List"%>
<%
    List<String> centers = (List<String>) request.getSession().getAttribute("centers");
    String warehouseName = (String) request.getSession().getAttribute("centerName");
    List<FieldExecutive> fes = (List<FieldExecutive>) request.getAttribute("fes");
    List<Device> devices = (List<Device>) request.getAttribute("devices");
%>
<div>
	<div>
		<h2>Assign Device to Field Executive</h2>

		<div class="inputarea">
			<label>Choose a Field Executive</label> <select name="fe" id="fes">
				<%
				    for (FieldExecutive fe : fes) {
				%>
				<option value='<%=fe.getId()%>' label='<%=fe.getName()%>'><%=fe.getName()%></option>

				<%
				    }
				%>
			</select> <label style="margin-left: 20px;">Choose from available
				Device</label> <select name="devices" id="devices">
				<%
				    for (Device d : devices) {
				%>
				<option value='<%=d.getDeviceID()%>' label='<%=d.getUniqueCode()%>'><%=d.getUniqueCode()%></option>

				<%
				    }
				%>
			</select>
		</div>
		<div class="inputarealeft" style="margin-bottom: 50px;">
			<div>
				<input type="button" value="Assign" id="asssignDevice" />
				<input type="button" value="Fetch Assigned Devices" id="asssignedDevice" />
			</div>
		</div>

		<hr />
		<div id="assignedDevices"></div>
	</div>
	
	<div id ="deassignmentMessage"></div>

</div>